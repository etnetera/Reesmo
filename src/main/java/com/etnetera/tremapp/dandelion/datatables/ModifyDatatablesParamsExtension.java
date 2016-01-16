package com.etnetera.tremapp.dandelion.datatables;

import com.github.dandelion.datatables.core.extension.AbstractExtension;
import com.github.dandelion.datatables.core.html.HtmlTable;

public class ModifyDatatablesParamsExtension extends AbstractExtension {

	private static final String NAME = "modifyParams";
	
	@Override
	public String getExtensionName() {
		return NAME;
	}

	@Override
	public void setup(HtmlTable table) {
		
		//appendToAfterStartDocumentReady(afterStartDocumentReady);
	}

}
