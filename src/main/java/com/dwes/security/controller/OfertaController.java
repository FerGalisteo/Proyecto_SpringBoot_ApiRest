package com.dwes.security.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dwes.security.entities.Oferta;
import com.dwes.security.entities.Role;
import com.dwes.security.entities.Usuario;
import com.dwes.security.service.OfertaService;

/**
 * - Usa la Entidad directamente si tu aplicación es sencilla, deseas mantener
 * el código al mínimo, y la API refleja directamente tu modelo de dominio.
 * 
 */
@RestController
@RequestMapping("/api/v1/ofertas")
public class OfertaController {

	private static final Logger logger = LoggerFactory.getLogger(OfertaController.class);

	@Autowired
	private OfertaService ofertaService;

	// Endpoint para obtener un listado de oferta
	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public ResponseEntity<Page<Oferta>> listarTodasLasOfertas(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(value = "usuario", required = false) String usuario,
	        @RequestParam(value = "precioMax", required = false) Double precioMax) {

	    logger.info("OfertasController :: listarTodasLasOfertas");
	    Pageable pageable = PageRequest.of(page, size);

	    if (usuario != null) {
	        // Filtrar por usuario
	        Page<Oferta> listaUsers = ofertaService.listarOfertaPorUsuario(usuario, pageable);
	        return new ResponseEntity<>(listaUsers, HttpStatus.OK);
	    } else if (precioMax != null) {
	        // Filtrar por precio máximo
	        Page<Oferta> listaPorPrecio = ofertaService.listarOfertasPorPrecioMaximo(precioMax, pageable);
	        return new ResponseEntity<>(listaPorPrecio, HttpStatus.OK);
	    } else {
	        // Obtener todas las ofertas si no se proporciona usuario ni precioMax
	        Page<Oferta> ofertas = ofertaService.listarTodasLasOfertas(pageable);
	        return new ResponseEntity<>(ofertas, HttpStatus.OK);
	    }
	}


	// Leer una oferta por ID
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public Oferta getOfertaById(@PathVariable Long id) {
		return ofertaService.obtenerOfertaPorId(id);
	}

	// ------POST-------Método para crear una oferta-----------
	@PostMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Void> crearOferta(@RequestBody Oferta oferta, @AuthenticationPrincipal Usuario usuario) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		ofertaService.guardarOferta(oferta, username);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	// Actualizar una Oferta
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public Oferta updateOferta(@PathVariable Long id, @RequestBody Oferta oferta, @AuthenticationPrincipal Usuario usuario) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();

		Set<Role> roles = usuario.getRoles();

		if (roles.contains(Role.ROLE_ADMIN)) {
			return ofertaService.actualizarOfertaAdmin(id, oferta);
		} else {
			return ofertaService.actualizarOferta(id, oferta, username);
		}

	}

	/* 
	 * @DeleteMapping("/{id}") -TODO COMENTAR
	 */

	// Eliminar una oferta siendo usuario registrado
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public void deleteOfertaUser(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();

		Set<Role> roles = usuario.getRoles();

		if (roles.contains(Role.ROLE_ADMIN)) {
			ofertaService.eliminarOfertaAdmin(id);
		} else {
			ofertaService.eliminarOferta(id, username);
		}

	}
	
	
	
	/*
	 *--------Filtros pero no son necesarios estos dado que utilizamos los que tenemos en el propio listar integrados. 
	*/
	
	/**
	 * Listar Ofertas por Usuario
	 *
	 */
	
	@GetMapping("/usuario/{username}")
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public ResponseEntity<Page<Oferta>> listarOfertasPorUsuario(
	        @PathVariable String username,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
	    logger.info("OfertasController :: listarOfertasPorUsuario");
	    Pageable pageable = PageRequest.of(page, size);
	    Page<Oferta> ofertas = ofertaService.listarOfertaPorUsuario(username, pageable);
	    return new ResponseEntity<>(ofertas, HttpStatus.OK);
	}


	/**
	 * FILTRAR OFERTAS POR PRECIO MAXIMO
	 * @param precioMax
	 * @param page
	 * @param size
	 * @return
	 */
	@GetMapping("/filtrar")
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public ResponseEntity<Page<Oferta>> filtrarOfertasPorPrecio(
	        @RequestParam(required = false) Double precioMax,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
	    logger.info("OfertasController :: filtrarOfertasPorPrecio");
	    Pageable pageable = PageRequest.of(page, size);
	    Page<Oferta> ofertas = ofertaService.listarOfertasPorPrecioMaximo(precioMax, pageable);
	    return new ResponseEntity<>(ofertas, HttpStatus.OK);
	}

}

/***
 * ############ # Reservar Libro ###########
 * 
 * 
 * @PostMapping("/{libroId}/reservar") @PreAuthorize("hasRole('ROLE_USER')")
 * public ResponseEntity<?> realizarReserva(@PathVariable Long
 * libroId, @AuthenticationPrincipal Usuario usuario) { try { // Agregar log de
 * la operación logger.info("LibrosController :: realizarReserva id Libro: {}
 * Usuario: {}", libroId, usuario.getUsername());
 * 
 * if (!reservaService.esLibroDisponibleParaReserva(libroId)) {
 * ErrorDetailsResponse errorDetails = new ErrorDetailsResponse( new Date(),
 * "Conflicto", "El libro no está disponible para reserva." ); return
 * ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails); }
 * 
 * LocalDate fechaReserva = LocalDate.now(); LocalDate fechaExpiracion =
 * fechaReserva.plusDays(7);
 * 
 * // Asegúrate de que el ID del usuario sea de tipo Long Long usuarioId =
 * usuario.getId(); // Suponiendo que getId() devuelve un Long
 * 
 * Reserva reserva = reservaService.crearReserva(libroId, usuarioId,
 * fechaReserva, fechaExpiracion); DetailsResponse details_reserva = new
 * DetailsResponse( new Date(), "Reservado:'" +
 * reserva.getLibro().getTitulo()+"', "+ reserva.getLibro().getAutor(),
 * "Expiración reserva:'" + reserva.getFechaExpiracion()+"'"
 * 
 * ); return ResponseEntity.status(HttpStatus.CREATED).body(details_reserva); }
 * catch (EntityNotFoundException e) { ErrorDetailsResponse errorDetails = new
 * ErrorDetailsResponse( new Date(), "No encontrado", e.getMessage() ); return
 * ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails); } catch
 * (Exception e) { ErrorDetailsResponse errorDetails = new ErrorDetailsResponse(
 * new Date(), "Error interno del servidor", e.getMessage() ); return
 * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails); }
 * } }
 * 
 */
