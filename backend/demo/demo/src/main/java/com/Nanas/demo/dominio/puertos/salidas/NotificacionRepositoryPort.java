package com.Nanas.demo.dominio.puertos.salidas;

import java.util.List;

import com.Nanas.demo.dominio.modelos.Notificacion;

public interface NotificacionRepositoryPort {
    Notificacion guardar(Notificacion notificacion);

    List<Notificacion> listarPorUsuario(Integer idUsuario);

    void marcarComoLeida(Integer idNotificacion);
}
