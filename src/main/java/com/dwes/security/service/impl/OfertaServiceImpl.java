package com.dwes.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dwes.security.entities.Libro;
import com.dwes.security.entities.Oferta;
import com.dwes.security.entities.Reserva;
import com.dwes.security.entities.Usuario;
import com.dwes.security.error.exception.OfertaNotFoundException;
import com.dwes.security.repository.OfertaRepository;
import com.dwes.security.repository.UserRepository;
import com.dwes.security.service.OfertaService;

import jakarta.validation.Valid;
@Service
public class OfertaServiceImpl implements OfertaService{
	
	@Autowired
	private OfertaRepository ofertaRepository;

	@Override
	public Oferta agregarOferta(Oferta oferta) {
		// TODO Auto-generated method stub
		return ofertaRepository.save(oferta);
	}
	
	@Autowired
    private UserRepository usuarioRepositorio;
	
	
	 
	 //Este método para guardar una oferta es como el de arriba pero más simplificado
	 public void guardarOferta(Oferta oferta, String username) {
	        Usuario usuarioCreador = usuarioRepositorio.findByEmail(username)
	        		 .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));
	        oferta.setUsuarioCreador(usuarioCreador);
	        ofertaRepository.save(oferta);
	    }
 boolean puedeCrearOferta(Long usuarioId, Oferta oferta) {
		// TODO Auto-generated method stub
		return oferta.getUsuarioCreador().getId().equals(usuarioId);
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
//TODO Actualizar el método para que compruebe que se tienen permisos para actualizar la oferta.
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
	public void eliminarOfertaAdmin(Long id) {
		ofertaRepository.deleteById(id);
		
	}
	
	//Método para eliminar una oferta SI LA HA CREADO EL QUE ESTÁ LOGEADO
	@Override
	public void eliminarOferta(Long ofertaId, String username) {
        // Obtenemos la oferta por ID
        Oferta oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow(() -> new IllegalArgumentException("Oferta no encontrada"));

        // Verificar si el usuario tiene permisos para eliminar la oferta.
        if (!puedeEliminarOferta(username, oferta)) {
            throw new AccessDeniedException("No tienes permisos para eliminar esta oferta");
        }

       
        ofertaRepository.deleteById(ofertaId);
    }

	/**
	 * Método para comprobar si el usuario logueado coincide con el que ha creado la publicación
	 * @param username
	 * @param oferta
	 * @return
	 */
    private boolean puedeEliminarOferta(String username, Oferta oferta) {
        // Verifica si el usuario logueado coincide con el usuario que creó la oferta
        return oferta.getUsuarioCreador().getUsername().equals(username);
    }

	@Override
	public Reserva reservarOferta(Oferta oferta, Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * MÉTODO FILTRADO POR USUARIO
	 */
	@Override
	public Page<Oferta> listarOfertaPorUsuario(String username, Pageable pageable) {
		Usuario usuario = usuarioRepositorio.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con nombre de usuario: " + username));
        return ofertaRepository.findByUsuarioCreador(usuario, pageable);
	}
	/**
	 * FILTRADO POR PRECIO MAX
	 * @param precioMax
	 * @param pageable
	 * @return
	 */
	public Page<Oferta> listarOfertasPorPrecioMaximo(Double precioMax, Pageable pageable) {
	    if (precioMax != null) {
	        return ofertaRepository.findByPrecioLessThanEqual(precioMax, pageable);
	    } else {
	        return ofertaRepository.findAll(pageable);
	    }
	}


	

	
}
