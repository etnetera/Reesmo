package com.etnetera.tremapp.controller.result;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etnetera.tremapp.controller.MenuActivityController;
import com.etnetera.tremapp.model.datatables.result.ResultDT;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.repository.elasticsearch.result.ResultRepository;
import com.etnetera.tremapp.user.UserManager;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class ResultController implements MenuActivityController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ResultRepository resultRepository;
	
	@Override
	public String getActiveMenu() {
		return "results";
	}
	
	@RequestMapping(value = "/results", method = RequestMethod.GET)
	public String results() {
		return "page/result/results";
	}

	@RequestMapping(value = "/dt/results")
	public @ResponseBody DatatablesResponse<ResultDT> findAllForDataTables(HttpServletRequest request, Locale locale) {
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ResultDT> results = resultRepository.findWithDatatablesCriterias(criterias, userManager.getAllowedProjectIds(Permission.BASIC), locale);
		return DatatablesResponse.build(results, criterias);
	}

}
