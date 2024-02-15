package com.dwes.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dwes.security.entities.Libro;
import com.dwes.security.entities.Oferta;
import com.dwes.security.entities.Reserva;
import com.dwes.security.entities.Usuario;
import com.dwes.security.error.exception.LibroNotFoundException;
import com.dwes.security.error.exception.OfertaNotFoundException;
import com.dwes.security.repository.OfertaRepository;
import com.dwes.security.service.OfertaService;

import jakarta.validation.Valid;

public class OfertaServiceImpl implements OfertaService{
	
	@Autowired
	private OfertaRepository ofertaRepository;

	@Override
	public Oferta agregarOferta(Oferta oferta) {
		// TODO Auto-generated method stub
		return ofertaRepository.save(oferta);
	}

	@Override
	public Page<Oferta> listarTodasLasOfertas(Pageable pageable) {
		return ofertaRepository.findAll(pageable);
	}

	@Override
	public Oferta obtenerOfertaPorId(Long id) {
		   return ofertaRepository.findById(id)
	                .orElseThrow(() -> new OfertaNotFoundException("Oferta no encontrada"));
	    }

	@Override
	public Oferta actualizarOferta(Long id, @Valid Oferta detalleOferta) {
		 Oferta oferta = obtenerOfertaPorId(id);
	        oferta.setTitulo(detalleOferta.getTitulo());
	        oferta.setPrecio(detalleOferta.getPrecio());
	        oferta.setDescripcion(detalleOferta.getDescripcion());
	        oferta.setLugar(detalleOferta.getLugar());
	        oferta.setFechaComienzo(detalleOferta.getFechaComienzo());
	        // Actualiza otros campos necesarios
	        return ofertaRepository.save(oferta);
	}

	@Override
	public void eliminarOferta(Long id) {
		ofertaRepository.deleteById(id);
		
	}

	@Override
	public Page<Libro> listarOfertaPorUsuario(Long usuarioId, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reserva reservarOferta(Oferta oferta, Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void devolverLibro(Long reservaId) {
		// TODO Auto-generated method stub
		
	}

	
}
