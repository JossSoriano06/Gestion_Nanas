package com.Nanas.demo.infraestructura.adaptadores.web.Controladores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Nanas.demo.aplicacion.puertos.entradas.UbicacionService;
import com.Nanas.demo.dominio.modelos.Ubicacion;

@RestController
@RequestMapping("/api/ubicaciones")
@CrossOrigin(origins = "*")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    public UbicacionController(UbicacionService ubicacionService) {

        this.ubicacionService = ubicacionService;

    }

    @PostMapping
    public ResponseEntity<?> guardarUbicacion(
            @RequestBody Ubicacion ubicacion) {

        try {

            Ubicacion guardada =
                    ubicacionService.guardarUbicacion(ubicacion);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(guardada);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());

        }

    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> obtenerUltimaUbicacion(
            @PathVariable Integer idUsuario) {

        try {

            Ubicacion ubicacion =
                    ubicacionService.obtenerUltimaUbicacion(idUsuario);

            return ResponseEntity.ok(ubicacion);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        }

    }

}