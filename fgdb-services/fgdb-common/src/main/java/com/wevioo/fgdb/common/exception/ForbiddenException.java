package com.wevioo.fgdb.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Entity already exist exception
 */
public class ForbiddenException extends RestException {
	/**
	 * Serial Number
	 */
	private static final long serialVersionUID = -3260745311457353808L;

	/**
	 * Constructor with code
	 * 
	 * @param code error code
	 */
	public ForbiddenException(String code) {
		super(HttpStatus.FORBIDDEN, code);
	}

	/**
	 * Constructor with code
	 *
	 * @param code error code
	 */
	public ForbiddenException(String code , String message) {
		super(HttpStatus.FORBIDDEN, code , message);
	}
}
