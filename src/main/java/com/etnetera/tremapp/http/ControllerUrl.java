package com.etnetera.tremapp.http;

public class ControllerUrl {

	private String uri;

	public static ControllerUrl factory(String uri) {
		return new ControllerUrl(uri);
	}
	
	public ControllerUrl(String uri) {
		this.uri = uri;
	}
	
	public ControllerUrl param(String name, Object value) {
		uri.replaceAll("{" + name + "}", String.valueOf(value));
		return this;
	}
	
	public String compose() {
		return uri;
	}
	
}
