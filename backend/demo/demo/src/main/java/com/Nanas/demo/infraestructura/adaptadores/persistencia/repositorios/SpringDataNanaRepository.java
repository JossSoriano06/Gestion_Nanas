package com.Nanas.demo.infraestructura.adaptadores.persistencia.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades.NanaEntity;

public interface SpringDataNanaRepository extends JpaRepository<NanaEntity, Integer> {

    @Query("""
        SELECT n
        FROM NanaEntity n
        WHERE n.usuario.idUsuario = :idUsuario
    """)
    Optional<NanaEntity> buscarPorIdUsuario(@Param("idUsuario") Integer idUsuario);

}