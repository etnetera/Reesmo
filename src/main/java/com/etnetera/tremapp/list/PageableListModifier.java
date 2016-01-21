package com.etnetera.tremapp.list;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Holds data for pageable list modification.
 */
public class PageableListModifier extends ListModifier {
	
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_SIZE = 20;
	
	private int page = -1;
	
	private int size = -1;
	
	public PageableListModifier() {}
	
	public PageableListModifier(int page, int size) {
		this.page = page;
		this.size = size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public PageableListModifier applyPageable(Pageable pageable) {
		page = pageable.getPageNumber();
		size = pageable.getPageSize();
		return this;
	}
	
	@Override
	public PageableListModifier join(ListModifier modifier) {
		return (PageableListModifier) super.join(modifier);
	}

	public PageableListModifier join(PageableListModifier modifier) {
		if (modifier.page != -1) page = modifier.page;
		if (modifier.size != -1) size = modifier.size;
		return (PageableListModifier) super.join(modifier);
	}

	public Pageable getPageable() {
		return new PageRequest(page == -1 ? DEFAULT_PAGE : page, size == -1 ? DEFAULT_SIZE : size);
	}
	
}
