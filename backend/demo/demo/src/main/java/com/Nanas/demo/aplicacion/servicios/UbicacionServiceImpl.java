package com.Nanas.demo.aplicacion.servicios;

import org.springframework.stereotype.Service;

import com.Nanas.demo.aplicacion.puertos.entradas.UbicacionService;
import com.Nanas.demo.dominio.modelos.Ubicacion;
import com.Nanas.demo.dominio.puertos.salidas.UbicacionRepositoryPort;

@Service
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionRepositoryPort ubicacionRepositoryPort;

    public UbicacionServiceImpl(UbicacionRepositoryPort ubicacionRepositoryPort) {

        this.ubicacionRepositoryPort = ubicacionRepositoryPort;

    }

    @Override
    public Ubicacion guardarUbicacion(Ubicacion ubicacion) {
        return ubicacionRepositoryPort.guardar(ubicacion);
    }

    @Override
    public Ubicacion obtenerUltimaUbicacion(Integer idUsuario) {
       return ubicacionRepositoryPort.obtenerUltimaUbicacion(idUsuario);
    }
    
    
}
