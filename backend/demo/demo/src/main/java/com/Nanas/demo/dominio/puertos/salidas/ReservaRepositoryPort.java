package com.Nanas.demo.dominio.puertos.salidas;

import java.time.LocalDateTime;

import com.Nanas.demo.dominio.modelos.Reserva;
import com.Nanas.demo.dominio.modelos.Ubicacion;
import java.util.List;

public interface ReservaRepositoryPort {

    Reserva guardarReserva(Reserva reserva);
    boolean existeCruzeHorario(Integer idNana, LocalDateTime inicio, LocalDateTime fin);
    Ubicacion buscarUltimaUbicacionUsuario(Integer idUsuario);

    void actualizarEstadoReserva(Integer idReserva, String nuevoEstado);
    void actualizarEstadoPago(Integer idReserva, String nuevoEstadoPago);
    Reserva buscarPorId(Integer idReserva);

    List<Reserva> obtenerPendientesNana(Integer idNana);
    
    List<Reserva> obtenerReservasCliente(Integer idCliente);
} 
