package com.dwes.security.error.exception;

public class OfertaNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public OfertaNotFoundException(String message) {
        super(message);
    }
}