package com.dwes.security.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;

@Entity
public class Reserva {
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	   @Enumerated(EnumType.STRING)
	   private EstadoReserva estadoReserva;

	   @ManyToOne
	    @JoinColumn(name = "usuario_id")
	    private Usuario usuario;

	    @ManyToOne
	    @JoinColumn(name = "libro_id")
	    private Libro libro;

	    @PastOrPresent
	    private LocalDate fechaReserva;
	    @FutureOrPresent
	    private LocalDate fechaExpiracion;
	    @FutureOrPresent
	    private LocalDate fechaRecogida;
	    @FutureOrPresent
	    private LocalDate fechaCancelada;
	    @FutureOrPresent
	    private LocalDate fechaDevolucion;
	    
	    
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public EstadoReserva getEstadoReserva() {
			return estadoReserva;
		}
		public void setEstadoReserva(EstadoReserva estadoReserva) {
			this.estadoReserva = estadoReserva;
		}
		public Usuario getUsuario() {
			return usuario;
		}
		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}
		public Libro getLibro() {
			return libro;
		}
		public void setLibro(Libro libro) {
			this.libro = libro;
		}
		public LocalDate getFechaReserva() {
			return fechaReserva;
		}
		public void setFechaReserva(LocalDate fechaReserva) {
			this.fechaReserva = fechaReserva;
		}
		public LocalDate getFechaExpiracion() {
			return fechaExpiracion;
		}
		public void setFechaExpiracion(LocalDate fechaExpiracion) {
			this.fechaExpiracion = fechaExpiracion;
		}
		public LocalDate getFechaRecogida() {
			return fechaRecogida;
		}
		public void setFechaRecogida(LocalDate fechaRecogida) {
			this.fechaRecogida = fechaRecogida;
		}
		public LocalDate getFechaCancelada() {
			return fechaCancelada;
		}
		public void setFechaCancelada(LocalDate fechaCancelada) {
			this.fechaCancelada = fechaCancelada;
		}
		public LocalDate getFechaDevolucion() {
			return fechaDevolucion;
		}
		public void setFechaDevolucion(LocalDate fechaDevolucion) {
			this.fechaDevolucion = fechaDevolucion;
		}
	    
	    
	
}
