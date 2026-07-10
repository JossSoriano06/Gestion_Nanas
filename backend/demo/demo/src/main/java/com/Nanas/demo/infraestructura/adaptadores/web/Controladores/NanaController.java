package com.Nanas.demo.infraestructura.adaptadores.web.Controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Nanas.demo.aplicacion.puertos.entradas.NanaService;
import com.Nanas.demo.dominio.modelos.Nana;

@RestController
@RequestMapping("/api/nanas")
@CrossOrigin(origins = "*")
public class NanaController {

    private final NanaService nanaService;

    public NanaController(NanaService nanaService) {
        this.nanaService = nanaService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarNana(@RequestBody Nana nana) {

        try {

            Nana nuevaNana = nanaService.registrarNana(nana);

            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaNana);

        } catch (IllegalArgumentException e) {

            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());

        } catch (Exception e) {

            e.printStackTrace(); 

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage()); //error visto en angular

        }

    }

    // Obtener solo nanas disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<Nana>> obtenerNanasDisponibles() {

        List<Nana> nanas = nanaService.listarNanasDisponibles();

        return ResponseEntity.ok(nanas);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerNanaPorId(@PathVariable Integer id) {

        try {

            Nana nana = nanaService.obtenerNanaPorId(id);

            return ResponseEntity.ok(nana);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        }

    }

}