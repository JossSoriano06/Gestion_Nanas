package com.Nanas.demo.infraestructura.adaptadores.web.Controladores;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Nanas.demo.aplicacion.puertos.entradas.NotificacionService;
import com.Nanas.demo.dominio.modelos.Notificacion;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Notificacion>> obtenerNotificaciones(
            @PathVariable Integer idUsuario) {

        return ResponseEntity.ok(
                notificacionService.obtenerNotificaciones(idUsuario));

    }

    @PatchMapping("/{id}/leida")
    public ResponseEntity<?> marcarComoLeida(
            @PathVariable Integer id) {

        notificacionService.marcarComoLeida(id);

        return ResponseEntity.ok().build();

    }

}