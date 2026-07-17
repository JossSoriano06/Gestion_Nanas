package com.Nanas.demo.infraestructura.adaptadores.persistencia.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades.PagoEntity;

public interface SpringDataPagoRepository extends JpaRepository <PagoEntity, Integer> {
     Optional<PagoEntity> findByIdReserva(Integer idReserva);
}
