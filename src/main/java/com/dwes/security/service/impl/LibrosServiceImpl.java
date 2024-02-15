package com.dwes.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dwes.security.entities.Libro;
import com.dwes.security.entities.Reserva;
import com.dwes.security.entities.Usuario;
import com.dwes.security.error.exception.LibroNotFoundException;
import com.dwes.security.repository.LibroRepository;
import com.dwes.security.repository.ReservaRepository;
import com.dwes.security.service.LibrosService;


import jakarta.validation.Valid;

@Service
public class LibrosServiceImpl implements LibrosService {


    @Autowired
    private LibroRepository libroRepository;
    
    @Autowired
    private ReservaRepository reservaRepository;
 

    @Override
    public Libro agregarLibro(@Valid Libro libro) {
        // Aquí se lanzará una excepción si el libro no es válido
        return libroRepository.save(libro);
    }

    @Override
    public Libro obtenerLibroPorId(Long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new LibroNotFoundException("Libro no encontrado"));
    }

    @Override
    public Libro actualizarLibro(Long id, @Valid Libro detallesLibro) {
        Libro libro = obtenerLibroPorId(id);
        libro.setTitulo(detallesLibro.getTitulo());
        libro.setAutor(detallesLibro.getAutor());
        libro.setIsbn(detallesLibro.getIsbn());
        // Actualiza otros campos necesarios
        return libroRepository.save(libro);
    }

    @Override
    public void eliminarLibro(Long id) {
        libroRepository.deleteById(id);
    }

	@Override
	public Page<Libro> listarTodosLosLibros(Pageable pageable) {
		 return libroRepository.findAll(pageable);
	}

	@Override
	public Page<Libro> listarLibrosPrestadosPorUsuario(Long usuarioId, Pageable pageable) {
	    return reservaRepository.findLibrosPrestadosPorUsuarioId(usuarioId, pageable);

	}

	@Override
	public Reserva reservarLibro(Libro libro, Usuario usuario) {
		
		return null;
	}

	@Override
	public void devolverLibro(Long reservaId) {
		// TODO Auto-generated method stub
		
	}




}
