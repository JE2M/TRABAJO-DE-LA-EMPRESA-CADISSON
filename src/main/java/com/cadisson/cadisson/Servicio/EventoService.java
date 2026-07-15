package com.cadisson.cadisson.Servicio;

import com.cadisson.cadisson.DTO.CalendarioEventosDTO;
import com.cadisson.cadisson.DTO.EventoEdicionDTO;
import com.cadisson.cadisson.DTO.EventoRespuestaDTO;

import java.util.List;

public interface EventoService {

    List<EventoRespuestaDTO> listarEventos();

    EventoRespuestaDTO buscarPorId(Long id);

    EventoRespuestaDTO convertirCotizacionEnEvento(Long cotizacionId);

    boolean existeEventoParaCotizacion(Long cotizacionId);

    CalendarioEventosDTO obtenerCalendario(
            Integer anio,
            Integer mes
    );

    EventoRespuestaDTO actualizarEstado(
            Long id,
            String nuevoEstado
    );

    EventoEdicionDTO obtenerEventoParaEditar(Long id);

    EventoRespuestaDTO actualizarEvento(
            Long id,
            EventoEdicionDTO eventoDTO
    );

    void cancelarEventoPorCotizacion(Long cotizacionId);
}