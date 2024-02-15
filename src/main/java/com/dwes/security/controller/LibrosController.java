package com.dwes.security.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dwes.security.controller.user.AuthorizationAdminController;
import com.dwes.security.dto.response.error.DetailsResponse;
import com.dwes.security.dto.response.error.ErrorDetailsResponse;
import com.dwes.security.entities.Libro;
import com.dwes.security.entities.Reserva;
import com.dwes.security.entities.Usuario;
import com.dwes.security.service.LibrosService;
import com.dwes.security.service.user.ReservaService;

import jakarta.persistence.EntityNotFoundException;

/** 
 * Nota 1: Es preferible mantener un solo idioma para el proyecto 
 * es discutible si se debería llamar BookController.java
 *  
 *       LibrosController.java 'llanito style ' 
 */ 

// (o yanito) a una variedad lingüística utilizada 
//  comúnmente por los habitantes de Gibraltar

/**
 * Nota 2:
 * 
 *  - Usa DTO (LibroRequest) si necesitas control de seguridad adicional,
 *  desacoplamiento, validaciones específicas de la API,
 *  o personalización para diferentes operaciones.
 *  
 *  vs
 *  
 *  - Usa la Entidad directamente si tu aplicación es sencilla, 
 *  deseas mantener el código al mínimo, 
 *  y la API refleja directamente tu modelo de dominio.
 *  
 */
	@RestController
	@RequestMapping("/api/v1/libros")
	public class LibrosController {

    	private static final Logger logger = LoggerFactory.getLogger(LibrosController.class);

	    @Autowired
	    private LibrosService librosService;
	    
	    @Autowired
	    private ReservaService reservaService;

	    // Endpoint para obtener un listado de libros, accesible solo por ROLE_USER
	    @GetMapping
	    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	    public ResponseEntity<Page<Libro>> listarTodosLosLibros(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {
	        
	        logger.info("LibrosController :: listarTodosLosLibros");
	        Pageable pageable = PageRequest.of(page, size);
	        Page<Libro> libros = librosService.listarTodosLosLibros(pageable);
	        
	   
	        
	        return new ResponseEntity<>(libros, HttpStatus.OK);
	    }
	    
	 // Leer un libro por ID
	    @GetMapping("/{id}")
	    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	    public Libro getBookById(@PathVariable Long id) {
	        return librosService.obtenerLibroPorId(id);
	    }

	    // CRUD endpoints, accesibles solo por ROLE_ADMIN
	    // Crear un nuevo libro
	    @PostMapping
	    @PreAuthorize("hasRole('ROLE_ADMIN')")
	    public Libro createBook(@RequestBody Libro book) {
	        return librosService.agregarLibro(book);
	    }

	    // Actualizar un libro
	    @PutMapping("/{id}")
	    @PreAuthorize("hasRole('ROLE_ADMIN')")
	    public Libro updateBook(@PathVariable Long id, @RequestBody Libro bookDetails) {
	        return librosService.actualizarLibro(id, bookDetails);
	    }

	    // Eliminar un libro
	    @DeleteMapping("/{id}")
	    @PreAuthorize("hasRole('ROLE_ADMIN')")
	    public void deleteBook(@PathVariable Long id) {
	        librosService.eliminarLibro(id);
	    }
	    
	    /***
	     * ############
	     * #   Reservar Libro
	     * ###########
	     */
	
	    @PostMapping("/{libroId}/reservar")
	    @PreAuthorize("hasRole('ROLE_USER')")
	    public ResponseEntity<?> realizarReserva(@PathVariable Long libroId, @AuthenticationPrincipal Usuario usuario) {
	    	  try {
	              // Agregar log de la operación
	              logger.info("LibrosController :: realizarReserva id Libro: {} Usuario: {}", libroId, usuario.getUsername());

	              if (!reservaService.esLibroDisponibleParaReserva(libroId)) {
	                  ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
	                          new Date(),
	                          "Conflicto",
	                          "El libro no está disponible para reserva."
	                  );
	                  return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails);
	              }

	              LocalDate fechaReserva = LocalDate.now();
	              LocalDate fechaExpiracion = fechaReserva.plusDays(7);

	              // Asegúrate de que el ID del usuario sea de tipo Long
	              Long usuarioId = usuario.getId(); // Suponiendo que getId() devuelve un Long

	              Reserva reserva = reservaService.crearReserva(libroId, usuarioId, fechaReserva, fechaExpiracion);
	              DetailsResponse details_reserva = new DetailsResponse(
	                      new Date(),
	                      "Reservado:'" + reserva.getLibro().getTitulo()+"', "+ reserva.getLibro().getAutor(),
	                      "Expiración reserva:'" + reserva.getFechaExpiracion()+"'"
	                    
	              );
	              return ResponseEntity.status(HttpStatus.CREATED).body(details_reserva);
	          } catch (EntityNotFoundException e) {
	              ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
	                      new Date(),
	                      "No encontrado",
	                      e.getMessage()
	              );
	              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
	          } catch (Exception e) {
	              ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
	                      new Date(),
	                      "Error interno del servidor",
	                      e.getMessage()
	              );
	              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
	          }
	      }
	    }
	    
	    
	    
	