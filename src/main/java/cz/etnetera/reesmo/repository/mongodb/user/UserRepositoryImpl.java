package cz.etnetera.reesmo.repository.mongodb.user;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import cz.etnetera.reesmo.common.ObjectWrapper;
import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.project.ProjectGroupUserDT;
import cz.etnetera.reesmo.model.datatables.project.ProjectUserDT;
import cz.etnetera.reesmo.model.datatables.project.ProjectUserFromGroupsDT;
import cz.etnetera.reesmo.model.datatables.user.UserDT;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.project.ProjectGroup;
import cz.etnetera.reesmo.model.mongodb.user.ManualUser;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.user.User;
import cz.etnetera.reesmo.repository.mongodb.MongoDatatables;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User repository custom method implementation
 */
public class UserRepositoryImpl implements UserRepositoryCustom {

	@Autowired
	private MongoOperations mongoTemplate;

	@Autowired
	private ManualUserRepository manualUserRepository;

	@Autowired
	private ProjectGroupRepository projectGroupRepository;

	@Autowired
	private Localizer localizer;

	@PostConstruct
	private void init() {
		// create super admin if there is no one
		if (!manualUserRepository.hasAnyAdmin()) {
			ManualUser u = new ManualUser();
			u.setLabel("Super Admin");
			u.setUsername("admin");
			u.setPassword(new BCryptPasswordEncoder().encode("admin"));
			u.setEmail("admin@reesmo.local");
			u.setEnabled(true);
			u.setSuperadmin(true);
			manualUserRepository.save(u);
		}
	}

	@Override
	public User findOneById(String id) {
		return mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), User.class);
	}

	@Override
	public User findOneByUsername(String username) {
		return mongoTemplate.findOne(Query.query(Criteria.where("username").is(username)), User.class);
	}

	@Override
	public DataSet<UserDT> findWithDatatablesCriterias(DatatablesCriterias criterias, Locale locale) {
		DataSet<User> users = findUsersWithDatatablesCriterias(criterias, null);

		return new DataSet<UserDT>(
				users.getRows().stream().map(u -> new UserDT(u, localizer, locale)).collect(Collectors.toList()),
				users.getTotalRecords(), users.getTotalDisplayRecords());
	}

	@Override
	public DataSet<ProjectUserDT> findProjectUsersWithDatatablesCriterias(DatatablesCriterias criterias,
			Project project, Locale locale) {
		DataSet<User> users = findUsersWithDatatablesCriterias(criterias, project.getUsers().keySet());

		return new DataSet<ProjectUserDT>(users.getRows().stream().map(u -> {
			return new ProjectUserDT(u, project, project.getUsers().get(u.getId()), localizer, locale);
		}).collect(Collectors.toList()), users.getTotalRecords(), users.getTotalDisplayRecords());
	}

	@Override
	public DataSet<ProjectUserFromGroupsDT> findProjectUsersFromGroupsWithDatatablesCriterias(
			DatatablesCriterias criterias, Project project, Locale locale) {
		// get all project groups with given project
		List<ProjectGroup> projectGroups = projectGroupRepository.findByProjectId(project.getId());

		// create unique list of involved users IDs (included in project groups)
		Set<String> usersIds = new HashSet<>();
		projectGroups.forEach(pg -> usersIds.addAll(pg.getUsers().keySet()));

		// find all involved users in project groups
		DataSet<User> users = findUsersWithDatatablesCriterias(criterias, usersIds);

		return new DataSet<ProjectUserFromGroupsDT>(users.getRows().stream().map(u -> {
			ObjectWrapper<Permission> permWrapper = new ObjectWrapper<>();
			// collect project groups which belongs to specific user, gathering most prioritized permission
			List<ProjectGroup> userProjectGroups = projectGroups.stream().filter(pg -> {
				Permission projectGroupPermission = pg.getUsers().get(u.getId());
				if (projectGroupPermission != null) {
					if (permWrapper.getValue() == null || projectGroupPermission.isGreaterThan(permWrapper.getValue())) {
						permWrapper.setValue(projectGroupPermission);
					}
					return true;
				}
				return false;
			}).collect(Collectors.toList());
			return new ProjectUserFromGroupsDT(u, project, permWrapper.getValue(), userProjectGroups, localizer,
					locale);
		}).collect(Collectors.toList()), users.getTotalRecords(), users.getTotalDisplayRecords());
	}
	
	@Override
	public DataSet<ProjectGroupUserDT> findProjectGroupUsersWithDatatablesCriterias(DatatablesCriterias criterias,
			ProjectGroup projectGroup, Locale locale) {
		DataSet<User> users = findUsersWithDatatablesCriterias(criterias, projectGroup.getUsers().keySet());

		return new DataSet<ProjectGroupUserDT>(users.getRows().stream().map(u -> {
			return new ProjectGroupUserDT(u, projectGroup, projectGroup.getUsers().get(u.getId()), localizer, locale);
		}).collect(Collectors.toList()), users.getTotalRecords(), users.getTotalDisplayRecords());
	}

	private DataSet<User> findUsersWithDatatablesCriterias(DatatablesCriterias criterias, Collection<String> userIds) {
		Criteria allCrit = null;
		if (userIds == null) {
			allCrit = Criteria.where("_id").exists(true);
		} else if (userIds.isEmpty()) {
			return new DataSet<User>(new ArrayList<>(), 0L, 0L);
		} else {
			allCrit = Criteria.where("_id").in(userIds);
		}

		Criteria crit = MongoDatatables.getCriteria(criterias, allCrit);

		Query query = Query.query(crit);
		MongoDatatables.sortQuery(query, criterias);
		MongoDatatables.paginateQuery(query, criterias);

		List<User> users = mongoTemplate.find(query, User.class);

		Long count = mongoTemplate.count(Query.query(allCrit), User.class);
		Long countFiltered = mongoTemplate.count(Query.query(crit), User.class);

		return new DataSet<User>(users, count, countFiltered);
	}

}
