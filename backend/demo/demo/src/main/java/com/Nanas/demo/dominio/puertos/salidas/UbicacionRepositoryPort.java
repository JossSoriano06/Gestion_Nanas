package com.Nanas.demo.dominio.puertos.salidas;

import com.Nanas.demo.dominio.modelos.Ubicacion;

public interface UbicacionRepositoryPort {
    Ubicacion guardar(Ubicacion ubicacion);

    Ubicacion obtenerUltimaUbicacion(Integer idUsuario);
}
