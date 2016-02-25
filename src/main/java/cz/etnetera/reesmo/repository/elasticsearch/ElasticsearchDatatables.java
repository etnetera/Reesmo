package cz.etnetera.reesmo.repository.elasticsearch;

import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import com.github.dandelion.datatables.core.ajax.ColumnDef.SortDirection;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;

public class ElasticsearchDatatables {

	public static Pageable getPageable(DatatablesCriterias criterias) {
		return new PageRequest(criterias.getStart() / criterias.getLength(), criterias.getLength());
	}

	public static void sortQueryBuilder(NativeSearchQueryBuilder queryBuilder, DatatablesCriterias criterias) {
		if (criterias.hasOneSortedColumn()) {
			criterias.getSortedColumnDefs().forEach(columnDef -> {
				queryBuilder.withSort(new FieldSortBuilder(columnDef.getName())
						.order(getElasticSortDirection(columnDef.getSortDirection())));
			});
		}
	}

	public static SortOrder getElasticSortDirection(SortDirection sd) {
		switch (sd) {
		case DESC:
			return SortOrder.DESC;
		default:
			return SortOrder.ASC;
		}
	}

}
