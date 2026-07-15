package com.cadisson.cadisson.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class EventoEstadoDTO {

    @NotBlank(message = "Debe seleccionar un estado")
    @Pattern(
            regexp = "^(Confirmado|En preparación|Realizado|Cancelado)$",
            message = "El estado seleccionado no es válido"
    )
    private String estado;

    public EventoEstadoDTO() {
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}