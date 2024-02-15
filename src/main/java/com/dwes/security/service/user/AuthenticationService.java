package com.dwes.security.service.user;

import com.dwes.security.dto.request.SignUpRequest;
import com.dwes.security.dto.request.SigninRequest;
import com.dwes.security.dto.response.user.JwtAuthenticationResponse;

public interface AuthenticationService {
	
	/** REGISTRO */
    JwtAuthenticationResponse signup(SignUpRequest request);
    /** ACCESO a Token JWT */
    JwtAuthenticationResponse signin(SigninRequest request);
}
