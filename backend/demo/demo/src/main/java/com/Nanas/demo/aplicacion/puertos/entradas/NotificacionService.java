package com.Nanas.demo.aplicacion.puertos.entradas;

import java.util.List;

import com.Nanas.demo.dominio.modelos.Notificacion;

public interface NotificacionService {
    Notificacion crearNotificacion(
            Integer idUsuario,
            String titulo,
            String mensaje,
            String tipo);

    List<Notificacion> obtenerNotificaciones(Integer idUsuario);

    void marcarComoLeida(Integer idNotificacion);
}
