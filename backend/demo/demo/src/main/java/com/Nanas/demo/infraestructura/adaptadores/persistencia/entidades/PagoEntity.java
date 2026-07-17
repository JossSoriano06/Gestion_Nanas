package com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "pagos")
public class PagoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @Column(name = "id_reserva")
    private Integer idReserva;

    @Column(name = "monto")
    private BigDecimal monto;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @Column(name = "numero_tarjeta")
    private String numeroTarjeta;

    @Column(name = "titular")
    private String titular;

    @Column(name = "estado")
    private String estado;

    @Column(name = "codigo_autorizacion")
    private String codigoAutorizacion;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @PrePersist
    public void onCreate(){

        fechaPago = LocalDateTime.now();

    }

    public Integer getIdPago() {
        return idPago;
    }

    public void setIdPago(Integer idPago) {
        this.idPago = idPago;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigoAutorizacion() {
        return codigoAutorizacion;
    }

    public void setCodigoAutorizacion(String codigoAutorizacion) {
        this.codigoAutorizacion = codigoAutorizacion;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    
}
