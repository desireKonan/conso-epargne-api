package org.marketplace_lea.common.common.validation;

/**
 * Erreur de validation.
 */
public class Error {
	/** Message de l'erreur */
	private final String message;

	/** Le type de l'erreur (i.e. 'NotNull', 'NotBlank' etc.) */
	private final String errorType;

	/** La valeur ayant entraîné l'erreur */
	private final Object errorValue;

	public Error(String errorType, String message, Object errorValue) {
		this.errorType = errorType;
		this.message = message;
		this.errorValue = errorValue;
	}

	public String getMessage() {
		return message;
	}

	public String getErrorType() {
		return errorType;
	}

	public Object getErrorValue() {
		return errorValue;
	}
}