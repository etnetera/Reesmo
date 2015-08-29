package com.etnetera.projects.testreporting.webapp.restapi.output;

import java.util.List;

import org.springframework.data.domain.Page;

public class RestApiList<T> {
	
	public PageData page;
	
	public List<T> data;
	
	public RestApiList(Page<T> page) {
		this.page = new PageData(page);
		data = page.getContent();
	}
	
	public class PageData {
		
		public int number;
		
		public int size;
		
		public int totalPages;
		
		public long totalElements;
		
		public PageData(Page<T> page) {
			number = page.getNumber();
			size = page.getSize();
			totalPages = page.getTotalPages();
			totalElements = page.getTotalElements();
		}
		
	}
	
}
