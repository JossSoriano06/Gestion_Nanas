package com.Nanas.demo.infraestructura.adaptadores.persistencia.adaptadores;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.Nanas.demo.dominio.modelos.Reserva;
import com.Nanas.demo.dominio.modelos.Ubicacion;
import com.Nanas.demo.dominio.puertos.salidas.ReservaRepositoryPort;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades.ReservaEntity;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades.UbicacionEntity;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.repositorios.SpringDataReservaRepository;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.repositorios.SpringDataUbicacionRepositoty;

@Component
public class ReservaPersitenceAdapter implements ReservaRepositoryPort {

    private final SpringDataReservaRepository reservaRepository;
    private final SpringDataUbicacionRepositoty ubicacionRepository;

    public ReservaPersitenceAdapter(SpringDataReservaRepository reservaRepository,
            SpringDataUbicacionRepositoty ubicacionRepositoty) {
        this.reservaRepository = reservaRepository;
        this.ubicacionRepository = ubicacionRepositoty;
    }

  
    @Override
    public Reserva guardarReserva(Reserva reserva) {

        ReservaEntity entity = new ReservaEntity();

        entity.setIdCliente(reserva.getIdCliente());
        entity.setIdNana(reserva.getIdNana());
        entity.setFechaInicio(reserva.getFechaInicio());
        entity.setFechaFin(reserva.getFechaFin());
        entity.setMontoTotal(reserva.getMontoTotal());
        entity.setEstadoPago(reserva.getEstadoPago());
        entity.setEstadoReserva(reserva.getEstadoReserva());

        System.out.println("===== ANTES DEL SAVE =====");
        System.out.println(entity.getIdCliente());
        System.out.println(entity.getIdNana());
        System.out.println(entity.getFechaInicio());
        System.out.println(entity.getFechaFin());
        System.out.println(entity.getMontoTotal());

        ReservaEntity guardado = reservaRepository.save(entity);

        System.out.println("===== DESPUÉS DEL SAVE =====");

        reserva.setIdReserva(guardado.getIdReserva());
        reserva.setFechaReserva(guardado.getFechaReserva());

        return reserva;
    }

    @Override
    public boolean existeCruzeHorario(Integer idNana, LocalDateTime inicio, LocalDateTime fin) {
        return reservaRepository.existsOverlappingReservation(idNana, inicio, fin);
    }

    @Override
    public Ubicacion buscarUltimaUbicacionUsuario(Integer idUsuario) {
        Optional<UbicacionEntity> entityOpt = ubicacionRepository
                .findFirstByIdUsuarioOrderByFechaRegistroDesc(idUsuario);

        if (entityOpt.isEmpty()) {
            return null;
        }

        UbicacionEntity entity = entityOpt.get();
        // Mapeo de Entidad a Dominio
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setIdUbicacion(entity.getIdUbicacion());
        ubicacion.setIdUsuario(entity.getIdUsuario());
        ubicacion.setLatitud(entity.getLatitud());
        ubicacion.setLongitud(entity.getLongitud());
        ubicacion.setFechaRegistro(entity.getFechaRegistro());

        return ubicacion;
    }

    @Override
    public void actualizarEstadoReserva(Integer idReserva, String nuevoEstado) {
        // Buscamos la entidad,
        // cambiamos de estado y guardamos
        reservaRepository.findById(idReserva).ifPresent(entity -> {
            entity.setEstadoReserva(nuevoEstado);
            reservaRepository.save(entity);
        });
    }

    @Override
    public void actualizarEstadoPago(Integer idReserva, String nuevoEstadoPago) {
        // solo se modifica el estado de pago
        reservaRepository.findById(idReserva).ifPresent(entity -> {
            entity.setEstadoPago(nuevoEstadoPago);
            reservaRepository.save(entity);
        });
    }

    @Override
    public Reserva buscarPorId(Integer idReserva) {

        // El servicio se encargará de lanzar la excepción si no existe
        Optional<ReservaEntity> entityOpt = reservaRepository.findById(idReserva);

        if (entityOpt.isEmpty()) {
            return null;
        }

        ReservaEntity entity = entityOpt.get();

        // se pasa de los valores de entities a al modelo del dominio
        Reserva reserva = new Reserva();
        reserva.setIdReserva(entity.getIdReserva());
        reserva.setIdCliente(entity.getIdCliente());
        reserva.setIdNana(entity.getIdNana());
        reserva.setFechaInicio(entity.getFechaInicio());
        reserva.setFechaFin(entity.getFechaFin());
        reserva.setMontoTotal(entity.getMontoTotal());
        reserva.setEstadoPago(entity.getEstadoPago());
        reserva.setEstadoReserva(entity.getEstadoReserva());
        reserva.setFechaReserva(entity.getFechaReserva());

        return reserva;
    }

    @Override
    public List<Reserva> obtenerPendientesNana(Integer idNana) {

        List<ReservaEntity> entities = reservaRepository.findPendientesByNana(idNana);

        return entities.stream().map(entity -> {

            Reserva reserva = new Reserva();

            reserva.setIdReserva(entity.getIdReserva());
            reserva.setIdCliente(entity.getIdCliente());
            reserva.setIdNana(entity.getIdNana());
            reserva.setFechaInicio(entity.getFechaInicio());
            reserva.setFechaFin(entity.getFechaFin());
            reserva.setMontoTotal(entity.getMontoTotal());
            reserva.setEstadoReserva(entity.getEstadoReserva());
            reserva.setEstadoPago(entity.getEstadoPago());

            return reserva;

        }).toList();

    }

    @Override
    public List<Reserva> obtenerReservasCliente(Integer idCliente) {

        System.out.println("ID CLIENTE RECIBIDO: " + idCliente);

        List<ReservaEntity> entities = reservaRepository.findByCliente(idCliente);

        System.out.println("RESERVAS ENCONTRADAS: " + entities.size());

        return entities.stream().map(entity -> {

            Reserva reserva = new Reserva();

            reserva.setIdReserva(entity.getIdReserva());
            reserva.setIdCliente(entity.getIdCliente());
            reserva.setIdNana(entity.getIdNana());
            reserva.setFechaInicio(entity.getFechaInicio());
            reserva.setFechaFin(entity.getFechaFin());
            reserva.setMontoTotal(entity.getMontoTotal());
            reserva.setEstadoReserva(entity.getEstadoReserva());
            reserva.setEstadoPago(entity.getEstadoPago());

            return reserva;

        }).toList();

    }

   
        @Override
    public List<Reserva> obtenerReservasNana(Integer idNana) {

    List<ReservaEntity> entities = reservaRepository.findByNana(idNana);

    return entities.stream().map(entity -> {

        Reserva reserva = new Reserva();

        reserva.setIdReserva(entity.getIdReserva());
        reserva.setIdCliente(entity.getIdCliente());
        reserva.setIdNana(entity.getIdNana());
        reserva.setFechaInicio(entity.getFechaInicio());
        reserva.setFechaFin(entity.getFechaFin());
        reserva.setMontoTotal(entity.getMontoTotal());
        reserva.setEstadoReserva(entity.getEstadoReserva());
        reserva.setEstadoPago(entity.getEstadoPago());
        reserva.setFechaReserva(entity.getFechaReserva());

        return reserva;

    }).toList();

    }
    

}
