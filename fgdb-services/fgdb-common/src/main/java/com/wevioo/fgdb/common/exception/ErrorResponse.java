package com.wevioo.fgdb.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

	private int order;
	private String message;
	private String code;
	private String fieldName;

}
