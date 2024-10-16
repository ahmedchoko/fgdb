package com.wevioo.fgdb.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;

/**
 * Global Exception.
 */
@Getter
public class RestException extends RuntimeException {
	/**
	 * Serial Number
	 */
	private static final long serialVersionUID = -8041597068126222870L;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
	private final transient Object data;
	private final transient Errors errors;
	private final String fieldName;



	public RestException(HttpStatus httpStatus, String code) {
		super(code);
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = null;
		this.data = null;
		this.errors=null;
		this.fieldName=null;

	}

	public RestException(HttpStatus httpStatus, String code, String message) {
		super(message);
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
		this.data = null;
		this.errors=null;
		this.fieldName=null;
	}

	public RestException(HttpStatus httpStatus, String code, String message, Object data) {
		super(message);
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
		this.data = data;
		this.errors=null;
		this.fieldName=null;
	}

	public RestException(HttpStatus httpStatus, String message, Errors errors) {
		super(message);
		this.httpStatus = httpStatus;
		this.code = null;
		this.message = message;
		this.data = null;
		this.errors = errors;
		this.fieldName=null;
	}
	
	public RestException(HttpStatus httpStatus,String code, String message, String fieldName) {
		super(message);
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
		this.data = null;
		this.errors = null;
		this.fieldName=fieldName;
	}


}
