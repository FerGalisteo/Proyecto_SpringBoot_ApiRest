package com.dwes.security.entities;

public enum EstadoReserva {
	  PENDIENTE,    // Reserva realizada, libro esperando ser recogido
	    CONFIRMADA,   // Reserva confirmada, libro recogido por el usuario
	    CANCELADA,    // Reserva cancelada por el usuario o la biblioteca
	    EXPIRADA,     // Reserva no confirmada y pasó el tiempo límite para recoger
	    COMPLETADA    // Reserva completada, libro devuelto
}
