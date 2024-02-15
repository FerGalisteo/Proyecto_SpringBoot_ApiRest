package com.dwes.security.error;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.dwes.security.dto.response.error.ErrorDetailsResponse;
import com.dwes.security.error.exception.LibroNotFoundException;


@ControllerAdvice
public class GlobalExceptionHandler {
	/**
     * #############################################
     * #       BAD_REQUEST. 400 EXCEPTION          ##
     * ##############################################
     */
	 @ExceptionHandler(IllegalArgumentException.class)
	    public ResponseEntity<ErrorDetailsResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		 ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
	            new Date(),
	            ex.getMessage(),
	            request.getDescription(false));
	        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	    }
    
    /**
     * ###################################################
     * #        Libro 404  Not Found Exception           ##
     * ###################################################
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(LibroNotFoundException.class)
    public ResponseEntity<ErrorDetailsResponse> handleLibroNotFoundException(LibroNotFoundException ex, WebRequest request) {
    	ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
            new Date(),
            ex.getMessage(),
            request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    /**
     * ####################################################
     * #       "Ruta no encontrada" Exception 404        ##
     * #####################################################
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetailsResponse> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
    	ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
            new Date(),
            "Ruta no encontrada",
            ex.getRequestURL());

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    /**
     * ####################################################
     * #     "Error interno del servidor" Exception 500   ##
     * #####################################################
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailsResponse> handleGlobalException(Exception ex, WebRequest request) {
    	ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
            new Date(),
            "Error interno del servidor",
            request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    /**
     * ####################################################
     * #     "Acceso denegado" Exception 403             ##
     * #####################################################
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetailsResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
            new Date(),
            "Acceso denegado",
            request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

}