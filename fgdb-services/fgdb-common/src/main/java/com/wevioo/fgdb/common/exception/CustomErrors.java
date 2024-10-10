package com.wevioo.fgdb.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BeanPropertyBindingResult;

/**
 * Custom Errors
 */
@Getter
@Setter
public class CustomErrors extends BeanPropertyBindingResult {

	/**
	 * Serial Number
	 */
	private static final long serialVersionUID = -4883110156932248778L;

	/**
	 *  CustomErrors
	 * @param target
	 * @param objectName
	 */
	public CustomErrors(Object target, String objectName) {
		super(target, objectName);
	}


}
