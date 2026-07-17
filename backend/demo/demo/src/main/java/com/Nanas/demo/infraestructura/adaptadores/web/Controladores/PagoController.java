package com.Nanas.demo.infraestructura.adaptadores.web.Controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Nanas.demo.aplicacion.puertos.entradas.PagoService;
import com.Nanas.demo.dominio.modelos.Pago;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<?> procesarPago(@RequestBody Pago pago) {

        try {

            Pago respuesta = pagoService.procesarPago(pago);

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());

        }

    }

}