package com.cadisson.cadisson.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class CotizacionRespuestaDTO {

    private Long id;
    private String codigo;

    private String nombreCliente;
    private String correo;
    private String telefono;

    private String ciudad;
    private String direccion;

    private String tipoEvento;
    private String servicio;
    private String paquete;

    private Integer horas;
    private Integer invitados;

    private LocalDate fechaEvento;
    private LocalTime horaEvento;

    private Boolean sonido;
    private Boolean luces;
    private Boolean pantallaLed;
    private Boolean maestroCeremonia;
    private Boolean transporte;

    private String observaciones;
    private Double precioEstimado;
    private String estado;

    public CotizacionRespuestaDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
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

    public Integer getInvitados() {
        return invitados;
    }

    public void setInvitados(Integer invitados) {
        this.invitados = invitados;
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

    public Double getPrecioEstimado() {
        return precioEstimado;
    }

    public void setPrecioEstimado(Double precioEstimado) {
        this.precioEstimado = precioEstimado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}