package com.dwes.security.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dwes.security.entities.Libro;
import com.dwes.security.entities.LugarDisponible;
import com.dwes.security.entities.Oferta;
import com.dwes.security.entities.Reserva;
import com.dwes.security.entities.Usuario;

public interface OfertaService {

    Oferta agregarOferta(Oferta oferta);

    Page<Oferta> listarTodasLasOfertas(Pageable pageable);

    Oferta obtenerOfertaPorId(Long id);

    Oferta actualizarOferta(Long id, Oferta oferta);
    
    Page<Libro> listarOfertaPorUsuario(Long usuarioId, Pageable pageable);

    Reserva reservarOferta(Oferta oferta, Usuario usuario);


	void eliminarOfertaAdmin(Long id);

	void eliminarOferta(Long ofertaId, String username);

	Oferta crearOferta(Long ofertaId, String titulo, Long usuarioId, String descripcion, Double precio,
			LocalDate fechaCreacion, LocalDate fechaComienzo, LugarDisponible lugar);

	void guardarOferta(Oferta oferta, String username);

	
   
}