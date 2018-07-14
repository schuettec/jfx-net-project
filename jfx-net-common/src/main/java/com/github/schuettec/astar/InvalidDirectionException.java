package com.github.schuettec.astar;

public class InvalidDirectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidDirectionException() {
	}

	public InvalidDirectionException(String message) {
		super(message);
	}

	public InvalidDirectionException(Throwable cause) {
		super(cause);
	}

	public InvalidDirectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidDirectionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
