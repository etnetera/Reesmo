package cz.etnetera.reesmo.repository.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.github.dandelion.core.util.StringUtils;
import com.github.dandelion.datatables.core.ajax.ColumnDef.SortDirection;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

public class MongoDatatables {

	public static Criteria getCriteria(DatatablesCriterias criterias) {
		return getCriteria(criterias, null);
	}
	
	public static Criteria getCriteria(DatatablesCriterias criterias, Criteria basicCriteria) {
		Criteria c = new Criteria();
		List<Criteria> crits = new ArrayList<>();
		if (basicCriteria != null) {
			crits.add(basicCriteria);
		}

		if (StringUtils.isNotBlank(criterias.getSearch()) && criterias.hasOneSearchableColumn()) {
			List<Criteria> cs = new ArrayList<>();
			
			criterias.getColumnDefs().forEach(columnDef -> {
				if (columnDef.isSearchable() && !columnDef.isFiltered()) {
					cs.add(Criteria.where(columnDef.getName()).regex(criterias.getSearch(), "i"));
				}
			});
			
			if (!cs.isEmpty()) {
				crits.add(new Criteria().orOperator(cs.toArray(new Criteria[cs.size()])));
			}
		}

		if (criterias.hasOneSearchableColumn() && criterias.hasOneFilteredColumn()) {
			List<Criteria> cs = new ArrayList<>();
			
			criterias.getColumnDefs().forEach(columnDef -> {
				if (columnDef.isSearchable()) {
					if (StringUtils.isNotBlank(columnDef.getSearchFrom())) {
						cs.add(Criteria.where(columnDef.getName()).gte(columnDef.getSearchFrom()));
					}
					if (StringUtils.isNotBlank(columnDef.getSearchTo())) {
						cs.add(Criteria.where(columnDef.getName()).lt(columnDef.getSearchTo()));
					}
					if (StringUtils.isNotBlank(columnDef.getSearch())) {
						cs.add(Criteria.where(columnDef.getName()).regex(columnDef.getSearch(), "i"));
					}
				}
			});
			
			if (!cs.isEmpty()) {
				crits.add(new Criteria().andOperator(cs.toArray(new Criteria[cs.size()])));
			}
		}
		
		if (!crits.isEmpty()) {
			c.andOperator(crits.toArray(new Criteria[crits.size()]));
		}

		return c;
	}

	public static void sortQuery(Query query, DatatablesCriterias criterias) {
		if (criterias.hasOneSortedColumn()) {
			criterias.getSortedColumnDefs().forEach(columnDef -> {
				query.with(new Sort(getSpringSortDirection(columnDef.getSortDirection()), columnDef.getName()));
			});
		}
	}
	
	public static void paginateQuery(Query query, DatatablesCriterias criterias) {
		query.skip(criterias.getStart());
		query.limit(criterias.getLength());
	}

	public static Sort.Direction getSpringSortDirection(SortDirection sd) {
		switch (sd) {
		case DESC:
			return Sort.Direction.DESC;
		default:
			return Sort.Direction.ASC;
		}
	}

}
