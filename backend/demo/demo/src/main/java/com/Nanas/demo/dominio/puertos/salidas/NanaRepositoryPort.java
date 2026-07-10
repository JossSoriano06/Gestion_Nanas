package com.Nanas.demo.dominio.puertos.salidas;

import java.util.List;

import com.Nanas.demo.dominio.modelos.Nana;

public interface NanaRepositoryPort {
    
    List<Nana> obtenerTodas();
    Nana guardarNana(Nana nana);
    
    Nana buscarNanaPorId(Integer idNana);
}
