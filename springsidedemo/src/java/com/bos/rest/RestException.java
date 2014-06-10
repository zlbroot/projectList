package com.bos.rest;

import org.springframework.http.HttpStatus;

/**
 * 专用于Restful Service的异常.
 */
public class RestException extends RuntimeException {
	private static final long serialVersionUID = 7648859223001474916L;
	
	public HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
	public RestException() {
	}

	public RestException(HttpStatus status) {
		this.status = status;
	}

	public RestException(String message) {
		super(message);
	}

	public RestException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}
}
