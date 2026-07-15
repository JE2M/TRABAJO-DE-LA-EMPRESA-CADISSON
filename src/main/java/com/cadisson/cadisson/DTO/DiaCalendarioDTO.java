package com.cadisson.cadisson.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DiaCalendarioDTO {

    private Integer numeroDia;

    private LocalDate fecha;

    private boolean perteneceAlMes;

    private List<EventoRespuestaDTO> eventos = new ArrayList<>();

    public DiaCalendarioDTO() {
    }

    public DiaCalendarioDTO(
            Integer numeroDia,
            LocalDate fecha,
            boolean perteneceAlMes,
            List<EventoRespuestaDTO> eventos
    ) {
        this.numeroDia = numeroDia;
        this.fecha = fecha;
        this.perteneceAlMes = perteneceAlMes;
        this.eventos = eventos;
    }

    public Integer getNumeroDia() {
        return numeroDia;
    }

    public void setNumeroDia(Integer numeroDia) {
        this.numeroDia = numeroDia;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isPerteneceAlMes() {
        return perteneceAlMes;
    }

    public void setPerteneceAlMes(boolean perteneceAlMes) {
        this.perteneceAlMes = perteneceAlMes;
    }

    public List<EventoRespuestaDTO> getEventos() {
        return eventos;
    }

    public void setEventos(
            List<EventoRespuestaDTO> eventos
    ) {
        this.eventos = eventos;
    }
}