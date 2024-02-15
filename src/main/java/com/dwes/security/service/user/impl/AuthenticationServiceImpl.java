package com.dwes.security.service.user.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dwes.security.dto.request.SignUpRequest;
import com.dwes.security.dto.request.SigninRequest;
import com.dwes.security.dto.response.user.JwtAuthenticationResponse;
import com.dwes.security.entities.Role;
import com.dwes.security.entities.Usuario;
import com.dwes.security.repository.UserRepository;
import com.dwes.security.service.user.AuthenticationService;
import com.dwes.security.service.user.JwtService;

import lombok.Builder;

@Builder
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private UserRepository userRepository; // Asegúrate de que UserRepository esté inyectado correctamente
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Constructor para inyección de dependencias (si usas Spring)
    public AuthenticationServiceImpl(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     JwtService jwtService,
                                     AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }
        // Corrige la forma de construir el objeto 'User'
        Usuario user = new Usuario();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(Role.ROLE_USER); // Asegúrate de que Role.USER esté definido correctamente
        userRepository.save(user);
        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) {
        // Maneja la autenticación
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        
       // SecurityContextHolder.getContext().setAuthentication(authentication);

        Usuario user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        String jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
