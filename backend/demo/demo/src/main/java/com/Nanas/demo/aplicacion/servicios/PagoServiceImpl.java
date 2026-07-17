package com.Nanas.demo.aplicacion.servicios;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Nanas.demo.aplicacion.puertos.entradas.NotificacionService;
import com.Nanas.demo.aplicacion.puertos.entradas.PagoService;
import com.Nanas.demo.dominio.modelos.Pago;
import com.Nanas.demo.dominio.modelos.Reserva;
import com.Nanas.demo.dominio.puertos.salidas.PagoRepositoryPort;
import com.Nanas.demo.dominio.puertos.salidas.ReservaRepositoryPort;
import com.Nanas.demo.dominio.puertos.salidas.UsuarioRepositoryPort;

@Service
public class PagoServiceImpl implements PagoService {

    private final PagoRepositoryPort pagoRepository;
    private final UsuarioRepositoryPort usuarioRepository;
    private final ReservaRepositoryPort reservaRepository;
    private final NotificacionService notificacionService;

    
    
    public PagoServiceImpl(PagoRepositoryPort pagoRepository, UsuarioRepositoryPort usuarioRepository,
            ReservaRepositoryPort reservaRepository, NotificacionService notificacionService) {
        this.pagoRepository = pagoRepository;
        this.usuarioRepository = usuarioRepository;
        this.reservaRepository = reservaRepository;
        this.notificacionService = notificacionService;
    }



    @Override
    @Transactional
    public Pago procesarPago(Pago pago) {
        Reserva reserva =
                reservaRepository.buscarPorId(
                        pago.getIdReserva());

        if(reserva == null){

            throw new RuntimeException("La reserva no existe.");

        }

        if(!"ACEPTADA".equals(reserva.getEstadoReserva())){

            throw new RuntimeException("La reserva aún no ha sido aceptada.");

        }

        if("PAGADO".equals(reserva.getEstadoPago())){

            throw new RuntimeException("La reserva ya fue pagada.");

        }
        if(pago.getNumeroTarjeta().length()!=16){

                throw new IllegalArgumentException(
      "La tarjeta debe contener 16 dígitos.");

}

        /*
         * SANDBOX
         */

        if(pago.getNumeroTarjeta().startsWith("4")){

            pago.setEstado("PAGADO");

        }else{

            pago.setEstado("RECHAZADO");

        }

        pago.setCodigoAutorizacion(

                UUID.randomUUID().toString()

        );

        Pago guardado =
                pagoRepository.guardarPago(pago);

        if("PAGADO".equals(guardado.getEstado())){

            reservaRepository.actualizarEstadoPago(
                    reserva.getIdReserva(),
                    "PAGADO");

            Integer idUsuarioNana =
                    usuarioRepository.obtenerIdUsuarioNana(
                            reserva.getIdNana());

            notificacionService.crearNotificacion(

                    idUsuarioNana,

                    "Pago recibido",

                    "El cliente realizó el pago correctamente.",

                    "PAGO"

            );

        }

        return guardado;

    }

    
    
}
