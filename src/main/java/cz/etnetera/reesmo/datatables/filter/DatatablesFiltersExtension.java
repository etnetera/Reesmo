package cz.etnetera.reesmo.datatables.filter;

import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlTable;

public class DatatablesFiltersExtension extends AbstractExtension {

	private static final String NAME = "filters";
	
	@Override
	public String getExtensionName() {
		return NAME;
	}

	@Override
	public void setup(HtmlTable table) {
		StringBuilder sb = new StringBuilder();
		sb.append("Reesmo.dataTables.filters.dtInitAjaxData(");
		sb.append("'").append(table.getId()).append("'");
		sb.append(", ");
		sb.append("oTable_").append(table.getId());
		sb.append(", ");
		sb.append("oTable_").append(table.getId()).append("_params");
		sb.append(");");
		appendToAfterStartDocumentReady(sb.toString());
	}

}
