package com.Nanas.demo.aplicacion.servicios;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.Nanas.demo.aplicacion.puertos.entradas.ReservaService;
import com.Nanas.demo.dominio.modelos.Nana;
import com.Nanas.demo.dominio.modelos.Reserva;
import com.Nanas.demo.dominio.modelos.Ubicacion;
import com.Nanas.demo.dominio.puertos.salidas.ReservaRepositoryPort;
import com.Nanas.demo.dominio.puertos.salidas.ReviewRepositoryPort;
import com.Nanas.demo.dominio.puertos.salidas.UsuarioRepositoryPort;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepositoryPort reservaRepositoryPort;
    private final ReviewRepositoryPort reviewRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public ReservaServiceImpl(ReservaRepositoryPort reservaRepositoryPort, 
                              ReviewRepositoryPort reviewRepositoryPort,
                              UsuarioRepositoryPort usuarioRepositoryPort) {
        this.reservaRepositoryPort = reservaRepositoryPort;
        this.reviewRepositoryPort = reviewRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public Reserva solicitarReserva(Reserva reserva) {

        LocalDateTime ahora = LocalDateTime.now();
        if (Duration.between(ahora, reserva.getFechaInicio()).toHours() < 1) {
            throw new IllegalArgumentException("Las reservas deben realizarse con un mínimo de 1 hora de anticipación.");
        }

        
        if (reserva.getFechaFin().isBefore(reserva.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de finalización no puede ser previa a la de inicio.");
        }

        
        boolean estaOcupada = reservaRepositoryPort.existeCruzeHorario(
                reserva.getIdNana(), reserva.getFechaInicio(), reserva.getFechaFin()
        );
        if (estaOcupada) {
            throw new IllegalArgumentException("La nana seleccionada ya cuenta con un servicio agendado en ese horario.");
        }

        //valores predeterminados 
        reserva.setEstadoReserva("PENDIENTE"); 
        reserva.setEstadoPago("PENDIENTE");
        reserva.setFechaReserva(ahora);

        return reservaRepositoryPort.guardarReserva(reserva);
    }

    @Override
    public Ubicacion obtenerUbicacionActualNana(Integer idUsuarioNana) {
        Ubicacion ubicacion = reservaRepositoryPort.buscarUltimaUbicacionUsuario(idUsuarioNana);
        if (ubicacion == null) {
            throw new IllegalArgumentException("No se encontraron registros de ubicación en tiempo real para esta nana.");
        }
        return ubicacion;
    }

    @Override
    @Transactional
    public void aceptarReserva(Integer idReserva) {
        Reserva reserva = reservaRepositoryPort.buscarPorId(idReserva);
        if (reserva == null) throw new IllegalArgumentException("La reserva no existe.");
        
        // Regla: Solo se puede aceptar si está PENDIENTE
        if (!"PENDIENTE".equals(reserva.getEstadoReserva())) {
            throw new IllegalStateException("Solo se pueden aceptar reservas en estado PENDIENTE.");
        }
        
        reservaRepositoryPort.actualizarEstadoReserva(idReserva, "ACEPTADA");
    }

    @Override
    @Transactional
    public void rechazarReserva(Integer idReserva) {
        Reserva reserva = reservaRepositoryPort.buscarPorId(idReserva);
        if (reserva == null) throw new IllegalArgumentException("La reserva no existe.");
        
        if (!"PENDIENTE".equals(reserva.getEstadoReserva())) {
            throw new IllegalStateException("Solo se pueden rechazar reservas en estado PENDIENTE.");
        }
        
        reservaRepositoryPort.actualizarEstadoReserva(idReserva, "RECHAZADA");
    }

    @Override
    @Transactional
    public void iniciarServicio(Integer idReserva) {
        Reserva reserva = reservaRepositoryPort.buscarPorId(idReserva);
        if (reserva == null) throw new IllegalArgumentException("La reserva no existe.");
        
        // Reglas de control para iniciar
        if (!"ACEPTADA".equals(reserva.getEstadoReserva())) {
            throw new IllegalStateException("El servicio no puede iniciar si la reserva no está ACEPTADA.");
        }
        if (!"PAGADO".equals(reserva.getEstadoPago())) {
            throw new IllegalStateException("Debe abonar el pago antes de iniciar el servicio en camino.");
        }
        
        reservaRepositoryPort.actualizarEstadoReserva(idReserva, "EN_PROGRESO");
    }

    @Override
    public void finalizarServicio(Integer idReserva) {
        Reserva reserva = reservaRepositoryPort.buscarPorId(idReserva);
        if (reserva == null) {
            throw new IllegalStateException("La reserva no existe");
        }
        if(!"EN PROCESO".equals(reserva.getEstadoReserva())){
            throw new IllegalStateException("Solo se puede finalizar un servicio que esté EN_PROGRESO.");
        }
        reservaRepositoryPort.actualizarEstadoReserva(idReserva, "FINALIZADA");
    }

    @Override
    @Transactional
    public void pagarReserva(Integer idReserva) {
        Reserva reserva = reservaRepositoryPort.buscarPorId(idReserva);
        if(reserva == null){
            throw new IllegalStateException("La reserva no existe");
        }
        //se tiene que haber aceptado antes
        if (!"ACEPTADA".equals(reserva.getEstadoReserva())) {
            throw new IllegalStateException("No se puede procesar el pago si la reserva no ha sido ACEPTADA.");
        }

        reservaRepositoryPort.actualizarEstadoPago(idReserva, "PAGADO");

    }

    @Override
    @Transactional
    public void calificarServicio(Integer idReserva, Integer rating, String comentario) {
        if(rating < 1 || rating > 5){
            throw new IllegalStateException("La calificacion debe de estar entre 1 y 5");
        }

        Reserva reserva = reservaRepositoryPort.buscarPorId(idReserva);
        if (reserva == null) {
            throw new IllegalStateException("No existe la reserva");
        }
        if (!"FINALIZADA".equals(reserva.getEstadoReserva())) {
            throw new IllegalStateException("No se puede calificar si no se a finalizado el servicio");
        }

        reviewRepositoryPort.guardarReview(idReserva, rating, comentario);

        Nana nana = usuarioRepositoryPort.buscarNanaPorId(reserva.getIdNana());
        
        int nuevaCantidadReviews = nana.getCantidadReviews() + 1;
        
        // promedio
        BigDecimal ratingActualTotal = nana.getRatingPromedio().multiply(new BigDecimal(nana.getCantidadReviews()));
        BigDecimal nuevoRatingTotal = ratingActualTotal.add(new BigDecimal(rating));
        BigDecimal nuevoPromedio = nuevoRatingTotal.divide(new BigDecimal(nuevaCantidadReviews), 2, RoundingMode.HALF_UP);
        
        // actualizamos el promedio
        reviewRepositoryPort.actualizarRatingNana(reserva.getIdNana(), nuevoPromedio, nuevaCantidadReviews);
        
        // cambio de estado, para no calificar nuevamente
        reservaRepositoryPort.actualizarEstadoReserva(idReserva, "CALIFICADA");
    }

    @Override
    @Transactional(readOnly = true)
    public com.Nanas.demo.dominio.modelos.Ubicacion buscarUltimaUbicacionUsuario(Integer idUsuario) {
        // validamos que este el id
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo.");
        }
        
        // llamanos al puerto de salida para buscar en la bd
        com.Nanas.demo.dominio.modelos.Ubicacion ubicacion = reservaRepositoryPort.buscarUltimaUbicacionUsuario(idUsuario);
        
        
        return ubicacion;
    }

    @Override
    public List<Reserva> obtenerPendientesNana(Integer idNana){

        return reservaRepositoryPort.obtenerPendientesNana(idNana);

    }

    @Override
    public List<Reserva> obtenerReservasCliente(Integer idCliente) {

        return reservaRepositoryPort.obtenerReservasCliente(idCliente);

    }
}
