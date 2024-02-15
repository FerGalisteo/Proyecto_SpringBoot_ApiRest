package com.dwes.security.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dwes.security.entities.Oferta;
@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {
	/* @Query("SELECT p.libro FROM Prestamo p WHERE p.usuario.id = :usuarioId")
	 List<Libro> findLibrosPrestadosPorUsuario(Integer usuarioId);*/
}
