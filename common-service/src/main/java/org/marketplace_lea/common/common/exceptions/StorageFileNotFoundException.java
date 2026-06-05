package org.marketplace_lea.common.common.exceptions;

public class StorageFileNotFoundException extends DataNotFoundException {

	public StorageFileNotFoundException(String message) {
		super(message);
	}

	public StorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
