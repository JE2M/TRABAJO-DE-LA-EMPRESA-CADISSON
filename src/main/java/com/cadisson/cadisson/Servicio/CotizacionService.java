package com.cadisson.cadisson.Servicio;

import com.cadisson.cadisson.DTO.CotizacionRespuestaDTO;
import com.cadisson.cadisson.DTO.CotizacionSolicitudDTO;

import java.util.List;

public interface CotizacionService {

    List<CotizacionRespuestaDTO> listarCotizaciones();

    CotizacionRespuestaDTO crearCotizacion(
            CotizacionSolicitudDTO solicitudDTO
    );

    CotizacionRespuestaDTO buscarPorId(Long id);

    CotizacionRespuestaDTO actualizarEstado(
            Long id,
            String nuevoEstado
    );

    void eliminarCotizacion(Long id);
}