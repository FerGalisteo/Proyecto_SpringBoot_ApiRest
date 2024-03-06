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

import jakarta.validation.Valid;

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

	/**
	 * 
	 *Este endpoint permite listar todas las ofertas.
	 *Además incluye filtros para poder listar las ofertas según el usuario que las ha creado o su precio máximo.
	 *
	 * @param page
	 * @param size
	 * @param usuario
	 * @param precioMax
	 * @return
	 */
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


	/**
	 * Este Endpoint recupera una oferta dado su id.
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public Oferta getOfertaById(@PathVariable Long id) {
		return ofertaService.obtenerOfertaPorId(id);
	}

	/**
	 * 
	 * Endpoint del método POST para crear una nueva Oferta.
	 * El método creará la oferta incluyendo el usuario loggeado como usuario creador de la oferta.
	 * 
	 * @param oferta
	 * @param usuario
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Void> crearOferta(@RequestBody @Valid Oferta oferta, @AuthenticationPrincipal Usuario usuario) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();
		ofertaService.guardarOferta(oferta, username);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * 
	 * Endpoint para actualizar una oferta. Se comprueba si el usuario es administrador o un usuario normal.
	 * En el ofertaService.actualizarOferta se comprueba si el usuario logeado ha creado esa oferta.
	 * Solo permite a usuarios loggeados o administradores editar sus propias ofertas.
	 * 
	 * @param id
	 * @param oferta
	 * @param usuario
	 * @return
	 */
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public Oferta updateOferta(@PathVariable Long id, @RequestBody @Valid Oferta oferta, @AuthenticationPrincipal Usuario usuario) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = userDetails.getUsername();

		Set<Role> roles = usuario.getRoles();

		if (roles.contains(Role.ROLE_ADMIN)) {
			return ofertaService.actualizarOfertaAdmin(id, oferta);
		} else {
			return ofertaService.actualizarOferta(id, oferta, username);
		}

	}

	/**
	 * Endpoint para eliminar una oferta. El método al igual que el de actualizar comprueba la identidad del usuario que realiza la petición.
	 * @param id
	 * @param usuario
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
	
}
