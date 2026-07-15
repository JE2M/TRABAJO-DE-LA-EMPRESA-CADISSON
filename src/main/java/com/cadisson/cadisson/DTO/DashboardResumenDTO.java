package com.cadisson.cadisson.DTO;

import java.util.ArrayList;
import java.util.List;

public class DashboardResumenDTO {

    private Long totalCotizaciones;
    private Long cotizacionesPendientes;
    private Long cotizacionesAceptadas;

    private Long totalEventos;
    private Long eventosActivos;
    private Long eventosRealizados;
    private Long eventosCancelados;

    private Double ingresosEstimados;

    private List<EventoRespuestaDTO> proximosEventos =
            new ArrayList<>();

    public DashboardResumenDTO() {
    }

    public Long getTotalCotizaciones() {
        return totalCotizaciones;
    }

    public void setTotalCotizaciones(Long totalCotizaciones) {
        this.totalCotizaciones = totalCotizaciones;
    }

    public Long getCotizacionesPendientes() {
        return cotizacionesPendientes;
    }

    public void setCotizacionesPendientes(
            Long cotizacionesPendientes
    ) {
        this.cotizacionesPendientes = cotizacionesPendientes;
    }

    public Long getCotizacionesAceptadas() {
        return cotizacionesAceptadas;
    }

    public void setCotizacionesAceptadas(
            Long cotizacionesAceptadas
    ) {
        this.cotizacionesAceptadas = cotizacionesAceptadas;
    }

    public Long getTotalEventos() {
        return totalEventos;
    }

    public void setTotalEventos(Long totalEventos) {
        this.totalEventos = totalEventos;
    }

    public Long getEventosActivos() {
        return eventosActivos;
    }

    public void setEventosActivos(Long eventosActivos) {
        this.eventosActivos = eventosActivos;
    }

    public Long getEventosRealizados() {
        return eventosRealizados;
    }

    public void setEventosRealizados(Long eventosRealizados) {
        this.eventosRealizados = eventosRealizados;
    }

    public Long getEventosCancelados() {
        return eventosCancelados;
    }

    public void setEventosCancelados(Long eventosCancelados) {
        this.eventosCancelados = eventosCancelados;
    }

    public Double getIngresosEstimados() {
        return ingresosEstimados;
    }

    public void setIngresosEstimados(Double ingresosEstimados) {
        this.ingresosEstimados = ingresosEstimados;
    }

    public List<EventoRespuestaDTO> getProximosEventos() {
        return proximosEventos;
    }

    public void setProximosEventos(
            List<EventoRespuestaDTO> proximosEventos
    ) {
        this.proximosEventos = proximosEventos;
    }
}