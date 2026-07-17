package com.Nanas.demo.infraestructura.adaptadores.persistencia.adaptadores;

import java.util.List;

import org.springframework.stereotype.Component;

import com.Nanas.demo.dominio.modelos.Notificacion;
import com.Nanas.demo.dominio.puertos.salidas.NotificacionRepositoryPort;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades.NotificacionEntity;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.repositorios.SpringDataNotificacionRepository;

@Component
public class NotificacionPersistenceAdapter implements NotificacionRepositoryPort {

    private final SpringDataNotificacionRepository repository;

    public NotificacionPersistenceAdapter(
            SpringDataNotificacionRepository repository) {

        this.repository = repository;

    }

    @Override
    public Notificacion guardar(Notificacion notificacion) {

        NotificacionEntity entity = new NotificacionEntity();

        entity.setIdUsuario(notificacion.getIdUsuario());
        entity.setTitulo(notificacion.getTitulo());
        entity.setMensaje(notificacion.getMensaje());
        entity.setTipo(notificacion.getTipo());
        entity.setLeida(false);

        NotificacionEntity guardada = repository.save(entity);

        notificacion.setIdNotificacion(
                guardada.getIdNotificacion());

        notificacion.setFecha(
                guardada.getFecha());

        return notificacion;

    }

    @Override
    public List<Notificacion> listarPorUsuario(Integer idUsuario) {

        return repository.findByIdUsuarioOrderByFechaDesc(idUsuario)
                .stream()
                .map(entity -> {

                    Notificacion n = new Notificacion();

                    n.setIdNotificacion(entity.getIdNotificacion());
                    n.setIdUsuario(entity.getIdUsuario());
                    n.setTitulo(entity.getTitulo());
                    n.setMensaje(entity.getMensaje());
                    n.setTipo(entity.getTipo());
                    n.setLeida(entity.getLeida());
                    n.setFecha(entity.getFecha());

                    return n;

                }).toList();

    }

    @Override
    public void marcarComoLeida(Integer idNotificacion) {

         repository.findById(idNotificacion)
                .ifPresent(entity -> {

                    entity.setLeida(true);

                    repository.save(entity);

                });

    }

}