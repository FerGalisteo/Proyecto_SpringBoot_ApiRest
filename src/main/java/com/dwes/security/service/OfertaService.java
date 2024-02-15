package com.dwes.security.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dwes.security.entities.Libro;
import com.dwes.security.entities.Oferta;
import com.dwes.security.entities.Reserva;
import com.dwes.security.entities.Usuario;

public interface OfertaService {

    Oferta agregarOferta(Oferta oferta);

    Page<Oferta> listarTodasLasOfertas(Pageable pageable);

    Oferta obtenerOfertaPorId(Long id);

    Oferta actualizarOferta(Long id, Oferta oferta);

    void eliminarOferta(Long id);

    Page<Libro> listarOfertaPorUsuario(Long usuarioId, Pageable pageable);

    Reserva reservarOferta(Oferta oferta, Usuario usuario);

    void devolverLibro(Long reservaId);

   
}