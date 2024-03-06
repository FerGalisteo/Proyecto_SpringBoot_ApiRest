package com.dwes.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dwes.security.entities.Oferta;

public interface OfertaService {


	Page<Oferta> listarTodasLasOfertas(Pageable pageable);

	Oferta obtenerOfertaPorId(Long id);

	Oferta actualizarOferta(Long id, Oferta oferta, String username);
	Oferta actualizarOfertaAdmin(Long id, Oferta oferta);

	Page<Oferta> listarOfertaPorUsuario(String username, Pageable pageable);

	//Reserva reservarOferta(Oferta oferta, Usuario usuario);

	void eliminarOfertaAdmin(Long id);

	void eliminarOferta(Long ofertaId, String username);

	// Oferta crearOferta(Long ofertaId, String titulo, Long usuarioId, String
	// descripcion, Double precio,LocalDate fechaCreacion, LocalDate fechaComienzo,
	// LugarDisponible lugar);

	void guardarOferta(Oferta oferta, String username);
	public Page<Oferta> listarOfertasPorPrecioMaximo(Double precioMax, Pageable pageable);

}