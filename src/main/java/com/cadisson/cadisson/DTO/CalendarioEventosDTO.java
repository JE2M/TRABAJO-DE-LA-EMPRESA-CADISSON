package com.cadisson.cadisson.DTO;

import java.util.ArrayList;
import java.util.List;

public class CalendarioEventosDTO {

    private Integer anio;

    private Integer mes;

    private String titulo;

    private Integer anioAnterior;

    private Integer mesAnterior;

    private Integer anioSiguiente;

    private Integer mesSiguiente;

    private Integer cantidadEventos;

    private List<DiaCalendarioDTO> dias =
            new ArrayList<>();

    public CalendarioEventosDTO() {
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnioAnterior() {
        return anioAnterior;
    }

    public void setAnioAnterior(Integer anioAnterior) {
        this.anioAnterior = anioAnterior;
    }

    public Integer getMesAnterior() {
        return mesAnterior;
    }

    public void setMesAnterior(Integer mesAnterior) {
        this.mesAnterior = mesAnterior;
    }

    public Integer getAnioSiguiente() {
        return anioSiguiente;
    }

    public void setAnioSiguiente(Integer anioSiguiente) {
        this.anioSiguiente = anioSiguiente;
    }

    public Integer getMesSiguiente() {
        return mesSiguiente;
    }

    public void setMesSiguiente(Integer mesSiguiente) {
        this.mesSiguiente = mesSiguiente;
    }

    public Integer getCantidadEventos() {
        return cantidadEventos;
    }

    public void setCantidadEventos(
            Integer cantidadEventos
    ) {
        this.cantidadEventos = cantidadEventos;
    }

    public List<DiaCalendarioDTO> getDias() {
        return dias;
    }

    public void setDias(
            List<DiaCalendarioDTO> dias
    ) {
        this.dias = dias;
    }
}