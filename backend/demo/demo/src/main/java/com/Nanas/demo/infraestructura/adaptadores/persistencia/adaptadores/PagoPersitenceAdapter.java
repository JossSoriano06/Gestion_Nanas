package com.Nanas.demo.infraestructura.adaptadores.persistencia.adaptadores;

import org.springframework.stereotype.Component;

import com.Nanas.demo.dominio.modelos.Pago;
import com.Nanas.demo.dominio.puertos.salidas.PagoRepositoryPort;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades.PagoEntity;
import com.Nanas.demo.infraestructura.adaptadores.persistencia.repositorios.SpringDataPagoRepository;

@Component
public class PagoPersitenceAdapter implements PagoRepositoryPort {

    private final SpringDataPagoRepository repository;
    

    public PagoPersitenceAdapter(SpringDataPagoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Pago guardarPago(Pago pago) {
        PagoEntity entity = new PagoEntity();

        entity.setIdReserva(pago.getIdReserva());
        entity.setMonto(pago.getMonto());
        entity.setMetodoPago(pago.getMetodoPago());
        entity.setNumeroTarjeta(pago.getNumeroTarjeta());
        entity.setTitular(pago.getTitular());
        entity.setEstado(pago.getEstado());
        entity.setCodigoAutorizacion(pago.getCodigoAutorizacion());

        PagoEntity guardado = repository.save(entity);

        pago.setIdPago(guardado.getIdPago());
        pago.setFechaPago(guardado.getFechaPago());

        return pago;
    }

    @Override
    public Pago buscarPorReserva(Integer idReserva) {
        PagoEntity entity = repository.findByIdReserva(idReserva)
                .orElse(null);

        if (entity == null) {
            return null;
        }

        Pago pago = new Pago();

        pago.setIdPago(entity.getIdPago());
        pago.setIdReserva(entity.getIdReserva());
        pago.setMonto(entity.getMonto());
        pago.setMetodoPago(entity.getMetodoPago());
        pago.setNumeroTarjeta(entity.getNumeroTarjeta());
        pago.setTitular(entity.getTitular());
        pago.setEstado(entity.getEstado());
        pago.setCodigoAutorizacion(entity.getCodigoAutorizacion());
        pago.setFechaPago(entity.getFechaPago());

        return pago;
    }
    
}
