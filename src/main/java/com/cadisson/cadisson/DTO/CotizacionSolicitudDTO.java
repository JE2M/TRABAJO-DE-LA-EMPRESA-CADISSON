package com.cadisson.cadisson.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class CotizacionSolicitudDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombreCliente;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Ingrese un correo válido")
    @Size(max = 120, message = "El correo es demasiado largo")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(
            regexp = "^[0-9+\\-\\s]{9,15}$",
            message = "Ingrese un teléfono válido"
    )
    private String telefono;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 80, message = "La ciudad es demasiado larga")
    private String ciudad;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección es demasiado larga")
    private String direccion;

    @NotBlank(message = "Seleccione el tipo de evento")
    private String tipoEvento;

    @NotNull(message = "Seleccione un servicio")
    private Long servicioId;

    @NotBlank(message = "Seleccione un paquete")
    private String paquete;

    @NotNull(message = "Indique las horas de presentación")
    @Min(value = 1, message = "La presentación debe durar al menos una hora")
    @Max(value = 8, message = "La presentación no puede superar las 8 horas")
    private Integer horas;

    private Boolean sonido = false;

    private Boolean luces = false;

    private Boolean pantallaLed = false;

    private Boolean maestroCeremonia = false;

    private Boolean transporte = false;

    @NotNull(message = "Seleccione la fecha del evento")
    @FutureOrPresent(message = "La fecha no puede estar en el pasado")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaEvento;

    @NotNull(message = "Seleccione la hora del evento")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime horaEvento;

    @NotNull(message = "Indique la cantidad de invitados")
    @Min(value = 1, message = "Debe existir al menos un invitado")
    @Max(value = 10000, message = "La cantidad de invitados es demasiado alta")
    private Integer invitados;

    @Size(
            max = 500,
            message = "Las observaciones no pueden superar los 500 caracteres"
    )
    private String observaciones;

    public CotizacionSolicitudDTO() {
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

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}