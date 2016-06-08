package cz.etnetera.reesmo.repository.mongodb.view;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import cz.etnetera.reesmo.model.datatables.view.ViewDT;
import cz.etnetera.reesmo.model.mongodb.view.View;

import java.util.List;

/**
 * View repository custom methods
 */
public interface ViewRepositoryCustom {

	public List<View> findByProject(String projectId);
	
    public DataSet<ViewDT> findViewsForProjectWithCriterias(DatatablesCriterias criterias, String projectId);

}
