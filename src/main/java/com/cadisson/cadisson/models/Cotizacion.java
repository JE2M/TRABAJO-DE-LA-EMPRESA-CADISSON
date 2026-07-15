package com.cadisson.cadisson.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "cotizaciones")
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombreCliente;

    @Column(nullable = false, length = 120)
    private String correo;

    @Column(nullable = false, length = 15)
    private String telefono;

    @Column(nullable = false, length = 80)
    private String ciudad;

    @Column(nullable = false, length = 200)
    private String direccion;

    @Column(nullable = false, length = 50)
    private String tipoEvento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_id", nullable = false)
    private Servicio servicio;

    @Column(nullable = false, length = 20)
    private String paquete;

    @Column(nullable = false)
    private Integer horas;

    @Column(nullable = false)
    private LocalDate fechaEvento;

    @Column(nullable = false)
    private LocalTime horaEvento;

    @Column(nullable = false)
    private Integer invitados;

    @Column(nullable = false)
    private Boolean sonido = false;

    @Column(nullable = false)
    private Boolean luces = false;

    @Column(nullable = false)
    private Boolean pantallaLed = false;

    @Column(nullable = false)
    private Boolean maestroCeremonia = false;

    @Column(nullable = false)
    private Boolean transporte = false;

    @Column(length = 500)
    private String observaciones;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(nullable = false)
    private Double precio;

    public Cotizacion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public String getPaquete() {
        return paquete;
    }

    public void setPaquete(String paquete) {
        this.paquete = paquete;
    }

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public LocalDate getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(LocalDate fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public LocalTime getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(LocalTime horaEvento) {
        this.horaEvento = horaEvento;
    }

    public Integer getInvitados() {
        return invitados;
    }

    public void setInvitados(Integer invitados) {
        this.invitados = invitados;
    }

    public Boolean getSonido() {
        return sonido;
    }

    public void setSonido(Boolean sonido) {
        this.sonido = sonido;
    }

    public Boolean getLuces() {
        return luces;
    }

    public void setLuces(Boolean luces) {
        this.luces = luces;
    }

    public Boolean getPantallaLed() {
        return pantallaLed;
    }

    public void setPantallaLed(Boolean pantallaLed) {
        this.pantallaLed = pantallaLed;
    }

    public Boolean getMaestroCeremonia() {
        return maestroCeremonia;
    }

    public void setMaestroCeremonia(Boolean maestroCeremonia) {
        this.maestroCeremonia = maestroCeremonia;
    }

    public Boolean getTransporte() {
        return transporte;
    }

    public void setTransporte(Boolean transporte) {
        this.transporte = transporte;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}