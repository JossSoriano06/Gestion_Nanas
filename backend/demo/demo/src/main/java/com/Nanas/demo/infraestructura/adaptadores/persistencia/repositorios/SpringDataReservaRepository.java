package com.Nanas.demo.infraestructura.adaptadores.persistencia.repositorios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades.ReservaEntity;

public interface SpringDataReservaRepository extends JpaRepository<ReservaEntity, Integer> {

       // para ver si hay un cruze de horario
       @Query("SELECT COUNT(r) > 0 FROM ReservaEntity r WHERE r.idNana = :idNana " +
                     "AND r.estadoReserva != 'CANCELADA' " +
                     "AND (:inicio < r.fechaFin AND :fin > r.fechaInicio)")
       boolean existsOverlappingReservation(@Param("idNana") Integer idNana,
                     @Param("inicio") LocalDateTime inicio,
                     @Param("fin") LocalDateTime fin);

       @Query("""
                     SELECT r
                     FROM ReservaEntity r
                     WHERE r.idNana = :idNana
                     AND r.estadoReserva = 'PENDIENTE'
                     ORDER BY r.fechaInicio ASC
                     """)
       List<ReservaEntity> findPendientesByNana(@Param("idNana") Integer idNana);

       @Query("""
                         SELECT r
                         FROM ReservaEntity r
                         WHERE r.idCliente = :idCliente
                         ORDER BY r.fechaReserva DESC
                     """)
       List<ReservaEntity> findByCliente(@Param("idCliente") Integer idCliente);


       @Query("""
        SELECT r
        FROM ReservaEntity r
        WHERE r.idNana = :idNana
        ORDER BY r.fechaReserva DESC
        """)
              List<ReservaEntity> findByNana(@Param("idNana") Integer idNana);
}
