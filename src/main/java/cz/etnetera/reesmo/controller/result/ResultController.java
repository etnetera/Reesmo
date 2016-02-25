package cz.etnetera.reesmo.controller.result;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import com.mongodb.gridfs.GridFSDBFile;

import cz.etnetera.reesmo.controller.MenuActivityController;
import cz.etnetera.reesmo.controller.json.JsonResponse;
import cz.etnetera.reesmo.datatables.filter.FilteredDatatablesCriterias;
import cz.etnetera.reesmo.http.ControllerModel;
import cz.etnetera.reesmo.http.exception.NotFoundException;
import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.result.ResultDT;
import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import cz.etnetera.reesmo.model.elasticsearch.result.ResultAttachment;
import cz.etnetera.reesmo.model.form.result.ResultDeleteCommand;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.repository.elasticsearch.result.ResultRepository;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.user.UserManager;

@Controller
public class ResultController implements MenuActivityController, ResultFilteredController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResultController.class);

	@Autowired
	private UserManager userManager;

	@Autowired
	private ResultRepository resultRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private Localizer localizer;

	@Override
	public String getActiveMenu() {
		return "results";
	}

	@RequestMapping(value = "/results", method = RequestMethod.GET)
	public String results(Model model, Locale locale) {
		injectFiltersDefinition(model, localizer, locale);
		return "page/result/results";
	}

	@RequestMapping(value = "/dt/results")
	public @ResponseBody DatatablesResponse<ResultDT> findAllForDataTables(HttpServletRequest request, Locale locale) throws Exception {
		FilteredDatatablesCriterias criterias = FilteredDatatablesCriterias.getFromRequest(request);
		DataSet<ResultDT> results = resultRepository.findWithFilteredDatatablesCriterias(criterias,
				userManager.getAllowedProjectIds(Permission.BASIC), locale);
		return DatatablesResponse.build(results, criterias.getCriterias());
	}

	@RequestMapping(value = "/result/home/{resultId}", method = RequestMethod.GET)
	public String resultHome(@PathVariable String resultId) {
		return "redirect:/result/detail/" + resultId;
	}

	@RequestMapping(value = "/result/detail/{resultId}", method = RequestMethod.GET)
	public String showResult(@PathVariable String resultId, Model model) {
		Result result = resultRepository.findOne(resultId);
		ControllerModel.exists(result, Result.class);
		userManager.checkProjectPermission(result.getProjectId(), Permission.BASIC);
		Project project = projectRepository.findOne(result.getProjectId());
		ControllerModel.exists(project, Project.class);
		model.addAttribute("result", result);
		model.addAttribute("project", project);
		return "page/result/resultDetail";
	}

	@RequestMapping(value = "/a/result/detail/{resultId}", method = RequestMethod.GET)
	public String showResultDetail(@PathVariable String resultId, Model model) {
		Result result = resultRepository.findOne(resultId);
		ControllerModel.exists(result, Result.class);
		userManager.checkProjectPermission(result.getProjectId(), Permission.BASIC);
		Project project = projectRepository.findOne(result.getProjectId());
		ControllerModel.exists(project, Project.class);
		model.addAttribute("result", result);
		model.addAttribute("project", project);
		return "fragments/result/resultDetail :: detail (single=false)";
	}

	@RequestMapping(value = "/results/delete", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JsonResponse deleteResults(@Valid ResultDeleteCommand removeCommand,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new JsonResponse(false, bindingResult.getAllErrors());
		}
		int i = 0;
		for (String resultId : removeCommand.getResultIds()) {
			Result result = resultRepository.findOne(resultId);
			if (result == null || !userManager.isAllowedForProject(result.getProjectId(), Permission.ADMIN))
				continue;
			resultRepository.deleteResult(result);
			i++;
		}
		return new JsonResponse(JsonResponse.Status.SUCCESS, i);
	}

	@RequestMapping(value = "/result/attachment/view/{resultId}/**", method = RequestMethod.GET)
	public ResponseEntity<byte[]> viewResultAttachment(@PathVariable String resultId, HttpServletRequest request) {
		return serveResultAttachment(resultId, request, false);
	}

	@RequestMapping(value = "/result/attachment/download/{resultId}/**", method = RequestMethod.GET)
	public ResponseEntity<byte[]> downloadResultAttachment(@PathVariable String resultId, HttpServletRequest request) {
		return serveResultAttachment(resultId, request, true);
	}

	@RequestMapping(value = "/a/result/attachment/view/{resultId}/**", method = RequestMethod.GET)
	public String viewResultAttachmentDetail(@PathVariable String resultId, Model model, HttpServletRequest request) {
		Result result = resultRepository.findOne(resultId);
		ControllerModel.exists(result, Result.class);
		userManager.checkProjectPermission(result.getProjectId(), Permission.BASIC);

		String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String path = new AntPathMatcher().extractPathWithinPattern(pattern, request.getPathInfo());
		ResultAttachment attachment = result.getAttachmentByPath(path);
		ControllerModel.exists(attachment, ResultAttachment.class);

		if (!attachment.getContentType().contains("text")) {
			throw new IllegalArgumentException(
					"Inline view for content type " + attachment.getContentType() + " is not supported!");
		}

		GridFSDBFile file = resultRepository.getAttachmentFile(attachment);
		ControllerModel.exists(file, GridFSDBFile.class);

		model.addAttribute("result", result);
		model.addAttribute("attachment", attachment);
		model.addAttribute("file", file);
		try {
			model.addAttribute("fileString", IOUtils.toString(file.getInputStream()));
		} catch (IOException e) {
			LOGGER.error("Cannot read byte[] for file {}", file.getId(), e);
			throw new NotFoundException("File does not exists!");
		}
		return "fragments/result/resultAttachmentDetail :: detail";
	}

	private ResponseEntity<byte[]> serveResultAttachment(@PathVariable String resultId, HttpServletRequest request,
			boolean download) {
		Result result = resultRepository.findOne(resultId);
		ControllerModel.exists(result, Result.class);
		userManager.checkProjectPermission(result.getProjectId(), Permission.BASIC);

		String pattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String path = new AntPathMatcher().extractPathWithinPattern(pattern, request.getPathInfo());
		ResultAttachment attachment = result.getAttachmentByPath(path);
		if (attachment == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		GridFSDBFile file = resultRepository.getAttachmentFile(attachment);
		if (file == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setContentType(MediaType.parseMediaType(file.getContentType()));
			if (download) {
				responseHeaders.setContentDispositionFormData(file.getFilename(), file.getFilename());
			}
			responseHeaders.setContentLength(file.getLength());
			responseHeaders.setCacheControl("max-age=7200, must-revalidate");

			return new ResponseEntity<>(IOUtils.toByteArray(file.getInputStream()), responseHeaders, HttpStatus.OK);
		} catch (IOException e) {
			LOGGER.error("Cannot read byte[] for file {}", file.getId(), e);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}
