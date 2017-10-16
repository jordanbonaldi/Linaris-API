package net.neferett.linaris.api;

public class InvalidTypeException extends IllegalArgumentException {
	
	private static final long serialVersionUID = 1L;

	public InvalidTypeException() {
	}

	public InvalidTypeException(String s) {
		super(s);
	}

	public InvalidTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidTypeException(Throwable cause) {
		super(cause);
	}
}
