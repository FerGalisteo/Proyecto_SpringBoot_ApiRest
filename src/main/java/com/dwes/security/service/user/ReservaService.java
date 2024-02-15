package com.dwes.security.service.user;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.dwes.security.entities.EstadoReserva;
import com.dwes.security.entities.Reserva;

public interface ReservaService {
	 // Método para crear una nueva reserva
    Reserva crearReserva(Long libroId, Long usuarioId, LocalDate fechaReserva, LocalDate fechaExpiracion);

    // Método para cancelar una reserva
    Reserva cancelarReserva(Long reservaId);

    // Método para actualizar el estado de una reserva
    Reserva actualizarEstadoReserva(Long reservaId, EstadoReserva nuevoEstado);

    // Método para obtener una reserva por ID
    Optional<Reserva> obtenerReservaPorId(Long reservaId);

    // Método para listar todas las reservas
    List<Reserva> listarTodasLasReservas();

    // Método para listar reservas de un usuario específico
    List<Reserva> listarReservasPorUsuario(Long usuarioId);

    // Método para verificar la disponibilidad de un libro para reserva
    boolean esLibroDisponibleParaReserva(Long libroId);

}
