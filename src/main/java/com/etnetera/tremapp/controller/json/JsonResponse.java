package com.etnetera.tremapp.controller.json;

public class JsonResponse {

	public enum Status {
		SUCCESS, ERROR
	}
	
	private Status status;
	
	private Object result;

	public JsonResponse() {
	}
	
	public JsonResponse(Object result) {
		this(Status.SUCCESS, result);
	}
	
	public JsonResponse(boolean status, Object result) {
		this(status ? Status.SUCCESS : Status.ERROR, result);
	}
	
	public JsonResponse(Status status, Object result) {
		this.status = status;
		this.result = result;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
}
