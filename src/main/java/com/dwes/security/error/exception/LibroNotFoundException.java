package com.dwes.security.error.exception;

public class LibroNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

	public LibroNotFoundException(String message) {
        super(message);
    }
}