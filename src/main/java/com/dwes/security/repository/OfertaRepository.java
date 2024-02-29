package com.dwes.security.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dwes.security.entities.Oferta;
import com.dwes.security.entities.Usuario;
@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {
	
	Page<Oferta> findByUsuarioCreador(Usuario usuario, Pageable pageable);
	Page<Oferta> findByPrecioLessThanEqual(Double precioMax, Pageable pageable);
	
	/* @Query("SELECT p.libro FROM Prestamo p WHERE p.usuario.id = :usuarioId")
	 List<Libro> findLibrosPrestadosPorUsuario(Integer usuarioId);*/
}
