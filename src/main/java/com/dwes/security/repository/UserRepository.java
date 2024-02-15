package com.dwes.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dwes.security.entities.Usuario;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Long> {
    // Since email is unique, we'll find users by email
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Long id);
    Boolean existsByEmail(String email);
}
