package com.dwes.security.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwes.security.entities.EstadoReserva;
import com.dwes.security.entities.Libro;
import com.dwes.security.entities.Reserva;
import com.dwes.security.entities.Usuario;
import com.dwes.security.error.exception.LibroNotFoundException;
import com.dwes.security.error.exception.UserNotFoundException;
import com.dwes.security.repository.LibroRepository;
import com.dwes.security.repository.ReservaRepository;
import com.dwes.security.repository.UserRepository;
import com.dwes.security.service.user.ReservaService;

import jakarta.transaction.Transactional;

@Service
public class ReservaServiceImpl implements ReservaService{
	 @Autowired
	    private ReservaRepository reservaRepositorio;

	    @Autowired
	    private LibroRepository libroRepositorio;

	    @Autowired
	    private UserRepository usuarioRepositorio;

	    @Override
	    @Transactional
	    public Reserva crearReserva(Long libroId, Long usuarioId, LocalDate fechaReserva, LocalDate fechaExpiracion) {
	    
 
	    	
	    	// Verificar si el libro existe y está disponible para reserva
	        Libro libro = libroRepositorio.findById(libroId)
	                        .orElseThrow(() -> new LibroNotFoundException("Libro no encontrado"));

	        if (!esLibroDisponibleParaReserva(libroId)) {
	            throw new IllegalStateException("El libro no está disponible para reserva");
	        }

	        // Verificar si el usuario existe
	        Usuario usuario = usuarioRepositorio.findById(usuarioId)
	                        .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

	   	 // Verifica si ya existe una reserva pendiente para el mismo libro y usuario
	        boolean reservaExistente = reservaRepositorio.existsByLibroAndUsuarioAndEstadoReserva(
	                libro, usuario, EstadoReserva.PENDIENTE);

	        if (reservaExistente) {
	            // Lanza una excepción o maneja el caso como consideres necesario
	            throw new IllegalStateException("Ya existe una reserva pendiente para este libro y usuario.");
	        }
	        
	        
	        // Crear la nueva reserva
	        Reserva reserva = new Reserva();
	        reserva.setLibro(libro);
	        reserva.setUsuario(usuario);
	        reserva.setFechaReserva(fechaReserva);
	        reserva.setFechaExpiracion(fechaExpiracion);
	        reserva.setEstadoReserva(EstadoReserva.PENDIENTE); // O el estado inicial que corresponda

	        // Guardar la reserva en la base de datos
	        return reservaRepositorio.save(reserva);
	    }
	@Override
	public Reserva cancelarReserva(Long reservaId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reserva actualizarEstadoReserva(Long reservaId, EstadoReserva nuevoEstado) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Reserva> obtenerReservaPorId(Long reservaId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<Reserva> listarTodasLasReservas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reserva> listarReservasPorUsuario(Long usuarioId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean esLibroDisponibleParaReserva(Long libroId) {
		 // Comprobar si el libro está actualmente prestado
	 /*   boolean estaPrestado = prestamoRepositorio.existsByLibroIdAndEstadoPrestamo(libroId, EstadoReserva.CONFIRMADA);
	    if (estaPrestado) {
	        return false;
	    }

	    // Comprobar si el libro está actualmente reservado
	    boolean estaReservado = reservaRepositorio.existsByLibroIdAndEstadoReserva(libroId, EstadoReserva.PENDIENTE);
	    if (estaReservado) {
	        return false;
	    }*/

	    // El libro está disponible para reserva
	    return true;
	}

}
