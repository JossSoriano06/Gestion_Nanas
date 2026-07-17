package com.Nanas.demo.infraestructura.adaptadores.persistencia.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades.NotificacionEntity;

public interface SpringDataNotificacionRepository extends JpaRepository<NotificacionEntity, Integer> {
    
    List<NotificacionEntity> findByIdUsuarioOrderByFechaDesc(Integer idUsuario);
}
