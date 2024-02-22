package com.dwes.security.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dwes.security.entities.Libro;
import com.dwes.security.entities.LugarDisponible;
import com.dwes.security.entities.Oferta;
import com.dwes.security.entities.Reserva;
import com.dwes.security.entities.Usuario;
import com.dwes.security.error.exception.OfertaNotFoundException;
import com.dwes.security.error.exception.UserNotFoundException;
import com.dwes.security.repository.OfertaRepository;
import com.dwes.security.repository.UserRepository;
import com.dwes.security.service.OfertaService;

import jakarta.transaction.Transactional;
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
	
	
	
	
	//Este crear Oferta es una alternativa pero demasiado engorroso para lo que queremos
	 @Override
	    @Transactional
	    public Oferta crearOferta(Long ofertaId, String titulo, Long usuarioId, String descripcion, Double precio,  LocalDate fechaCreacion, LocalDate fechaComienzo, LugarDisponible lugar) {
	    

	    	
		// Obtenemos la oferta por ID
	        Oferta oferta1 = ofertaRepository.findById(ofertaId)
	                .orElseThrow(() -> new IllegalArgumentException("Oferta no encontrada"));

	        // Verificar si el usuario tiene permisos para crear la oferta.
	        if (!puedeCrearOferta(usuarioId, oferta1)) {
	            throw new AccessDeniedException("No tienes permisos para eliminar esta oferta");
	        }

	        // Verificar si el usuario existe
	        Usuario usuario = usuarioRepositorio.findById(usuarioId)
	                        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

	   	
	        // Crear la nueva reserva
	        Oferta oferta = new Oferta();
	        oferta.setTitulo(titulo);;
	        oferta.setUsuarioCreador(usuario);
	        oferta.setDescripcion(descripcion);
	        oferta.setFechaComienzo(fechaComienzo);
	        oferta.setFechaCreacion(fechaCreacion);
	        oferta.setLugar(lugar);

	        // Guardar la reserva en la base de datos
	        return ofertaRepository.save(oferta);
	    }
	 
	 
	 //Este método para guardar una oferta es como el de arriba pero más simplificado
	 public void guardarOferta(Oferta oferta, String username) {
	        Usuario usuarioCreador = usuarioRepositorio.findByEmail(username)
	        		 .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));
	        oferta.setUsuarioCreador(usuarioCreador);
	        ofertaRepository.save(oferta);
	    }
	 /*
	 @Override
	public void guardarOferta(Oferta oferta, String username) {
		// TODO Auto-generated method stub
		
	}*/
	 

	private boolean puedeCrearOferta(Long usuarioId, Oferta oferta) {
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

    private boolean puedeEliminarOferta(String username, Oferta oferta) {
        // Verifica si el usuario logueado coincide con el usuario que creó la oferta
        return oferta.getUsuarioCreador().getUsername().equals(username);
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
	
	

	
}
