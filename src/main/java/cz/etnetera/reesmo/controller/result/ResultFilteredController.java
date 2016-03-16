package cz.etnetera.reesmo.controller.result;

import java.util.Locale;

import org.springframework.ui.Model;

import cz.etnetera.reesmo.datatables.filter.DatatablesFiltersDefinition;
import cz.etnetera.reesmo.datatables.filter.impl.DatatablesFilterDateRange;
import cz.etnetera.reesmo.datatables.filter.impl.DatatablesFilterNumberRange;
import cz.etnetera.reesmo.datatables.filter.impl.DatatablesFilterSelect;
import cz.etnetera.reesmo.datatables.filter.impl.DatatablesFilterText;
import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.elasticsearch.result.TestCategory;
import cz.etnetera.reesmo.model.elasticsearch.result.TestSeverity;
import cz.etnetera.reesmo.model.elasticsearch.result.TestStatus;
import cz.etnetera.reesmo.model.elasticsearch.result.TestType;

public interface ResultFilteredController {

	public default String injectFiltersDefinition(Model model, Localizer localizer, Locale locale) {
		DatatablesFiltersDefinition datatablesFiltersDef = new DatatablesFiltersDefinition()
				.addFilter(new DatatablesFilterText("name", "result.name", localizer, locale), true)
				.addFilter(new DatatablesFilterSelect("status", "result.status", TestStatus.values(),
						"result.status.value.", localizer, locale), true)
				.addFilter(new DatatablesFilterNumberRange("length", "result.length", localizer, locale), true)
				.addFilter(new DatatablesFilterSelect("severity", "result.severity", TestSeverity.values(),
						"result.severity.value.", localizer, locale), true)
				.addFilter(new DatatablesFilterDateRange("startedAt", "result.startedAt", localizer, locale), true)
				.addFilter(new DatatablesFilterText("suite", "result.suite", localizer, locale))
				.addFilter(new DatatablesFilterText("suiteId", "result.suiteId", localizer, locale))
				.addFilter(new DatatablesFilterText("job", "result.job", localizer, locale))
				.addFilter(new DatatablesFilterText("jobId", "result.jobId", localizer, locale))
				.addFilter(new DatatablesFilterText("milestone", "result.milestone", localizer, locale))
				.addFilter(new DatatablesFilterText("description", "result.description", localizer, locale))
				.addFilter(new DatatablesFilterText("environment", "result.environment", localizer, locale))
				.addFilter(new DatatablesFilterText("author", "result.author", localizer, locale))
				.addFilter(new DatatablesFilterSelect("categories", "result.categories", TestCategory.getDeclaredValues(),
						localizer, locale))
				.addFilter(new DatatablesFilterSelect("types", "result.types", TestType.getDeclaredValues(),
						localizer, locale))
				.addFilter(new DatatablesFilterText("labels", "result.labels", localizer, locale))
				.addFilter(new DatatablesFilterText("notes", "result.notes", localizer, locale))
				.addFilter(new DatatablesFilterText("errors", "result.errors", localizer, locale));
		model.addAttribute("datatablesFiltersDef", datatablesFiltersDef);
		return "page/result/results";
	}
	
}
