package com.Nanas.demo.aplicacion.puertos.entradas;

import java.util.List;

import com.Nanas.demo.dominio.modelos.Nana;

public interface NanaService {
    Nana registrarNana(Nana nana);
    List<Nana> listarNanasDisponibles();

    Nana obtenerNanaPorId(Integer idNana);
}


