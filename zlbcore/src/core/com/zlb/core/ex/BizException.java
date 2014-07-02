package com.zlb.core.ex;

public class BizException extends RuntimeException {
	private static final long serialVersionUID = 6368601197928340154L;
	private int code = -1;
	private String extMsg;

	public BizException() {
		super();
	}

	public BizException(String message) {
		super(message);
	}

	public BizException(int code, String message) {
		super(message);
		this.code = code;
	}

	public BizException(int code, String message, String extMsg) {
		super(message);
		this.code = code;
		this.extMsg = extMsg;
	}

	public int getCode() {
		return code;
	}

	public String getExtMsg() {
		return extMsg;
	}
}
