package com.Nanas.demo.infraestructura.adaptadores.persistencia.entidades;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "notificaciones")
public class NotificacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_notificacion")
    private Integer idNotificacion;

    @Column(name="id_usuario")
    private Integer idUsuario;

    @Column(name="titulo")
    private String titulo;

    @Column(name="mensaje")
    private String mensaje;

    @Column(name="tipo")
    private String tipo;

    @Column(name="leida")
    private Boolean leida;

    @Column(name="fecha")
    private LocalDateTime fecha;

    @PrePersist
    public void onCreate(){

        fecha = LocalDateTime.now();

        if(leida==null){

            leida=false;

        }

    }

    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getLeida() {
        return leida;
    }

    public void setLeida(Boolean leida) {
        this.leida = leida;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    
    
}
