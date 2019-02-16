package com.helo.exception;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.Nullable;

public class NoTracksException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Throwable getMostSpecificCause() {
		Throwable rootCause = getRootCause();
		return (rootCause != null ? rootCause : this);
	}

	@Nullable
	public Throwable getRootCause() {
		return NestedExceptionUtils.getRootCause(this);
	}

}
