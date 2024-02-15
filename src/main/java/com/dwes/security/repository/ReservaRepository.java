package com.dwes.security.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dwes.security.entities.EstadoReserva;
import com.dwes.security.entities.Libro;
import com.dwes.security.entities.Reserva;
import com.dwes.security.entities.Usuario;

public interface ReservaRepository extends JpaRepository<Reserva, Long>{
	@Query("SELECT r FROM Reserva r WHERE r.usuario.id = :usuarioId")
    Page<Libro> findLibrosPrestadosPorUsuarioId(@Param("usuarioId") Long usuarioId, Pageable pageable);
	boolean existsByLibroAndUsuarioAndEstadoReserva(Libro libro, Usuario usuario, EstadoReserva estadoReserva);
}

