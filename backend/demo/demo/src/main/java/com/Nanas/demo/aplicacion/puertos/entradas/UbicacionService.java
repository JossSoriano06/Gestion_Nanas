package com.Nanas.demo.aplicacion.puertos.entradas;

import com.Nanas.demo.dominio.modelos.Ubicacion;

public interface UbicacionService {
    Ubicacion guardarUbicacion(Ubicacion ubicacion);

    Ubicacion obtenerUltimaUbicacion(Integer idUsuario);
}
