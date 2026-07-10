package com.Nanas.demo.infraestructura.adaptadores.web.Controladores;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Nanas.demo.aplicacion.puertos.entradas.ReservaService;
import com.Nanas.demo.dominio.modelos.Reserva;
import com.Nanas.demo.dominio.modelos.Ubicacion;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva) {

        try {

            System.out.println("===== RESERVA RECIBIDA =====");
            System.out.println("Cliente: " + reserva.getIdCliente());
            System.out.println("Nana: " + reserva.getIdNana());
            System.out.println("Inicio: " + reserva.getFechaInicio());
            System.out.println("Fin: " + reserva.getFechaFin());
            System.out.println("Monto: " + reserva.getMontoTotal());

            Reserva nuevaReserva = reservaService.solicitarReserva(reserva);

            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.toString());

        }

    }

    /*
     * // crear reserva
     * 
     * @PostMapping
     * public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva) {
     * 
     * try {
     * Reserva nuevaReserva = reservaService.solicitarReserva(reserva);
     * return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
     * } catch (IllegalArgumentException | IllegalStateException e) {
     * // Captura cruces de horario o reserva menor a 12 horas
     * return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
     * } catch (Exception e) {
     * return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
     * .body("Error al procesar la solicitud de reserva.");
     * }
     * 
     * }
     */
    // acpetar el reserva por aprte de las nanas
    @PatchMapping("/{id}/aceptar")
    public ResponseEntity<?> aceptarReserva(@PathVariable Integer id) {
        try {
            reservaService.aceptarReserva(id);
            return ResponseEntity.ok(Map.of("mensaje", "Reserva aceptada exitosamente."));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // se rechaza la reserva
    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarReserva(@PathVariable Integer id) {
        try {
            reservaService.rechazarReserva(id);
            return ResponseEntity.ok(Map.of("mensaje", "Reserva rechazada exitosamente."));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // pagar por parte del cliente
    @PostMapping("/{id}/pagar")
    public ResponseEntity<?> pagarReserva(@PathVariable Integer id) {
        try {
            reservaService.pagarReserva(id);
            return ResponseEntity.ok(Map.of("mensaje", "Pago procesado de manera conforme."));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // inicio del servicio
    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<?> iniciarServicio(@PathVariable Integer id) {
        try {
            reservaService.iniciarServicio(id);
            return ResponseEntity.ok(Map.of("mensaje", "Servicio iniciado. Nana en camino."));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarServicio(@PathVariable Integer id) {
        try {
            reservaService.finalizarServicio(id);
            return ResponseEntity.ok(Map.of("mensaje", "Servicio concluido con éxito."));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // registro de la calificacion del servicio
    @PostMapping("/{id}/calificar")
    public ResponseEntity<?> calificarServicio(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> payload) {
        try {
            Integer rating = (Integer) payload.get("rating");
            String comentario = (String) payload.get("comentario");

            reservaService.calificarServicio(id, rating, comentario);
            return ResponseEntity.ok(Map.of("mensaje", "Calificación registrada. Reputación recalculada."));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar la reseña.");
        }
    }

    // ubicacion de la nana en tiempo real
    @GetMapping("/nana/{idUsuario}/ubicacion")
    public ResponseEntity<?> obtenerUbicacionActualNana(@PathVariable Integer idUsuario) {
        try {
            Ubicacion ubicacion = reservaService.buscarUltimaUbicacionUsuario(idUsuario);
            if (ubicacion == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No hay registros de geolocalización activos para este usuario.");
            }
            return ResponseEntity.ok(ubicacion);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al recuperar la localización del satélite.");
        }
    }

    @GetMapping("/solicitudes/{idNana}")
    public ResponseEntity<List<Reserva>> obtenerSolicitudes(
            @PathVariable Integer idNana) {

        return ResponseEntity.ok(
                reservaService.obtenerPendientesNana(idNana));

    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Reserva>> obtenerReservasCliente(
            @PathVariable Integer idCliente) {

        return ResponseEntity.ok(
                reservaService.obtenerReservasCliente(idCliente));

    }
}
