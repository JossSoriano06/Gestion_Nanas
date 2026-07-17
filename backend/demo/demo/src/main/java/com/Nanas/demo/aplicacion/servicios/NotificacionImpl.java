package com.Nanas.demo.aplicacion.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Nanas.demo.aplicacion.puertos.entradas.NotificacionService;
import com.Nanas.demo.dominio.modelos.Notificacion;
import com.Nanas.demo.dominio.puertos.salidas.NotificacionRepositoryPort;


@Service
public class NotificacionImpl implements NotificacionService {

    private final NotificacionRepositoryPort repository;

    public NotificacionImpl(NotificacionRepositoryPort repository) {
        this.repository = repository;
    }
    
    @Override
    public Notificacion crearNotificacion(Integer idUsuario, String titulo, String mensaje, String tipo) {
         
        Notificacion n = new Notificacion();

        n.setIdUsuario(idUsuario);
        n.setTitulo(titulo);
        n.setMensaje(mensaje);
        n.setTipo(tipo);

        n.setLeida(false);

        n.setFecha(LocalDateTime.now());

        return repository.guardar(n);

    }

    @Override
    public List<Notificacion> obtenerNotificaciones(Integer idUsuario) {
         return repository.listarPorUsuario(idUsuario);
    }

    @Override
    public void marcarComoLeida(Integer idNotificacion) {
        repository.marcarComoLeida(idNotificacion);
    }
    
}
