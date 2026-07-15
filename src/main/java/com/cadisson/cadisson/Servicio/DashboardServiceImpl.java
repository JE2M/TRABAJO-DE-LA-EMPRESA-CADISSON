package com.cadisson.cadisson.Servicio;

import com.cadisson.cadisson.DTO.DashboardResumenDTO;
import com.cadisson.cadisson.DTO.EventoRespuestaDTO;
import com.cadisson.cadisson.Repositorios.CotizacionRepositorio;
import com.cadisson.cadisson.Repositorios.EventoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl
        implements DashboardService {

    private final CotizacionRepositorio cotizacionRepositorio;
    private final EventoRepositorio eventoRepositorio;
    private final EventoService eventoService;

    public DashboardServiceImpl(
            CotizacionRepositorio cotizacionRepositorio,
            EventoRepositorio eventoRepositorio,
            EventoService eventoService
    ) {
        this.cotizacionRepositorio = cotizacionRepositorio;
        this.eventoRepositorio = eventoRepositorio;
        this.eventoService = eventoService;
    }

    @Override
    public DashboardResumenDTO obtenerResumen() {

        DashboardResumenDTO resumen =
                new DashboardResumenDTO();

        resumen.setTotalCotizaciones(
                cotizacionRepositorio.count()
        );

        resumen.setCotizacionesPendientes(
                cotizacionRepositorio
                        .countByEstadoIgnoreCase(
                                "Pendiente"
                        )
        );

        resumen.setCotizacionesAceptadas(
                cotizacionRepositorio
                        .countByEstadoIgnoreCase(
                                "Aceptada"
                        )
        );

        long confirmados =
                eventoRepositorio
                        .countByEstadoIgnoreCase(
                                "Confirmado"
                        );

        long enPreparacion =
                eventoRepositorio
                        .countByEstadoIgnoreCase(
                                "En preparación"
                        );

        resumen.setTotalEventos(
                eventoRepositorio.count()
        );

        resumen.setEventosActivos(
                confirmados + enPreparacion
        );

        resumen.setEventosRealizados(
                eventoRepositorio
                        .countByEstadoIgnoreCase(
                                "Realizado"
                        )
        );

        resumen.setEventosCancelados(
                eventoRepositorio
                        .countByEstadoIgnoreCase(
                                "Cancelado"
                        )
        );

        Double ingresos =
                eventoRepositorio
                        .sumarPrecioExceptoEstado(
                                "Cancelado"
                        );

        resumen.setIngresosEstimados(
                ingresos == null
                        ? 0.0
                        : ingresos
        );

        LocalDate fechaActual = LocalDate.now();

        List<EventoRespuestaDTO> proximosEventos =
                eventoService.listarEventos()
                        .stream()
                        .filter(
                                evento ->
                                        evento.getFecha() != null
                        )
                        .filter(
                                evento ->
                                        !evento.getFecha()
                                                .isBefore(
                                                        fechaActual
                                                )
                        )
                        .filter(
                                evento ->
                                        !"Cancelado"
                                                .equalsIgnoreCase(
                                                        evento.getEstado()
                                                )
                        )
                        .filter(
                                evento ->
                                        !"Realizado"
                                                .equalsIgnoreCase(
                                                        evento.getEstado()
                                                )
                        )
                        .limit(5)
                        .toList();

        resumen.setProximosEventos(
                proximosEventos
        );

        return resumen;
    }
}