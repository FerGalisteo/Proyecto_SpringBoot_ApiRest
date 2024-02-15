package com.dwes.security.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.dwes.security.dto.response.user.UsuarioResponse;

public interface UserService {
    UserDetailsService userDetailsService();
    List<UsuarioResponse> getAllUsers();
}
