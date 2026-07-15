package com.cadisson.cadisson.Servicio;

import com.cadisson.cadisson.DTO.CalendarioEventosDTO;
import com.cadisson.cadisson.DTO.DiaCalendarioDTO;
import com.cadisson.cadisson.DTO.EventoEdicionDTO;
import com.cadisson.cadisson.DTO.EventoRespuestaDTO;
import com.cadisson.cadisson.Repositorios.CotizacionRepositorio;
import com.cadisson.cadisson.Repositorios.EventoRepositorio;
import com.cadisson.cadisson.models.Cotizacion;
import com.cadisson.cadisson.models.Evento;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class EventoServiceImpl implements EventoService {

    private static final Set<String> ESTADOS_PERMITIDOS =
            Set.of(
                    "Confirmado",
                    "En preparación",
                    "Realizado",
                    "Cancelado"
            );

    private final EventoRepositorio eventoRepositorio;
    private final CotizacionRepositorio cotizacionRepositorio;

    public EventoServiceImpl(
            EventoRepositorio eventoRepositorio,
            CotizacionRepositorio cotizacionRepositorio
    ) {
        this.eventoRepositorio = eventoRepositorio;
        this.cotizacionRepositorio = cotizacionRepositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventoRespuestaDTO> listarEventos() {

        return eventoRepositorio
                .findAllByOrderByFechaAscHoraAsc()
                .stream()
                .map(this::convertirARespuestaDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EventoRespuestaDTO buscarPorId(Long id) {

        Evento evento = eventoRepositorio
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "No se encontró el evento"
                        )
                );

        return convertirARespuestaDTO(evento);
    }

    @Override
    public EventoRespuestaDTO convertirCotizacionEnEvento(
            Long cotizacionId
    ) {

        if (eventoRepositorio.existsByCotizacion_Id(cotizacionId)) {

            throw new IllegalArgumentException(
                    "Esta cotización ya fue convertida en evento"
            );
        }

        Cotizacion cotizacion = cotizacionRepositorio
                .findById(cotizacionId)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "No se encontró la cotización"
                        )
                );

        if (!"Aceptada".equalsIgnoreCase(
                cotizacion.getEstado()
        )) {

            throw new IllegalArgumentException(
                    "Solo una cotización aceptada puede convertirse en evento"
            );
        }

        validarDisponibilidad(cotizacion);

        Evento evento = new Evento();

        evento.setCodigo(
                generarCodigoEvento(cotizacion.getId())
        );

        evento.setCotizacion(cotizacion);

        evento.setNombreCliente(
                cotizacion.getNombreCliente()
        );

        evento.setTelefono(
                cotizacion.getTelefono()
        );

        evento.setServicio(
                cotizacion.getServicio().getNombre()
        );

        evento.setTipoEvento(
                cotizacion.getTipoEvento()
        );

        evento.setFecha(
                cotizacion.getFechaEvento()
        );

        evento.setHora(
                cotizacion.getHoraEvento()
        );

        evento.setDuracionHoras(
                cotizacion.getHoras() == null
                        ? 1
                        : cotizacion.getHoras()
        );

        evento.setCiudad(
                cotizacion.getCiudad()
        );

        evento.setDireccion(
                cotizacion.getDireccion()
        );

        evento.setInvitados(
                cotizacion.getInvitados()
        );

        evento.setPrecio(
                cotizacion.getPrecio()
        );

        evento.setObservaciones(
                cotizacion.getObservaciones()
        );

        evento.setEstado("Confirmado");

        Evento eventoGuardado =
                eventoRepositorio.save(evento);

        return convertirARespuestaDTO(
                eventoGuardado
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeEventoParaCotizacion(
            Long cotizacionId
    ) {

        return eventoRepositorio
                .existsByCotizacion_Id(cotizacionId);
    }

    @Override
    public void cancelarEventoPorCotizacion(
            Long cotizacionId
    ) {

        eventoRepositorio
                .findByCotizacion_Id(cotizacionId)
                .ifPresent(evento -> {

                    evento.setEstado("Cancelado");

                    eventoRepositorio.save(evento);
                });
    }

    @Override
    public EventoRespuestaDTO actualizarEstado(
            Long id,
            String nuevoEstado
    ) {

        if (nuevoEstado == null
                || !ESTADOS_PERMITIDOS.contains(nuevoEstado)) {

            throw new IllegalArgumentException(
                    "El estado seleccionado no es válido"
            );
        }

        Evento evento = eventoRepositorio
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "No se encontró el evento"
                        )
                );

        boolean estabaCancelado =
                "Cancelado".equalsIgnoreCase(
                        evento.getEstado()
                );

        boolean seraEventoActivo =
                "Confirmado".equalsIgnoreCase(nuevoEstado)
                        || "En preparación".equalsIgnoreCase(
                        nuevoEstado
                );

        if (estabaCancelado && seraEventoActivo) {

            validarDisponibilidadEventoExistente(evento);
        }

        evento.setEstado(nuevoEstado);

        Evento eventoActualizado =
                eventoRepositorio.save(evento);

        return convertirARespuestaDTO(
                eventoActualizado
        );
    }

    @Override
    @Transactional(readOnly = true)
    public EventoEdicionDTO obtenerEventoParaEditar(
            Long id
    ) {

        Evento evento = eventoRepositorio
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "No se encontró el evento"
                        )
                );

        EventoEdicionDTO dto =
                new EventoEdicionDTO();

        dto.setFecha(evento.getFecha());
        dto.setHora(evento.getHora());
        dto.setDuracionHoras(evento.getDuracionHoras());
        dto.setCiudad(evento.getCiudad());
        dto.setDireccion(evento.getDireccion());
        dto.setInvitados(evento.getInvitados());
        dto.setObservaciones(evento.getObservaciones());

        return dto;
    }

    @Override
    public EventoRespuestaDTO actualizarEvento(
            Long id,
            EventoEdicionDTO eventoDTO
    ) {

        Evento evento = eventoRepositorio
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "No se encontró el evento"
                        )
                );

        if ("Realizado".equalsIgnoreCase(
                evento.getEstado()
        )) {

            throw new IllegalArgumentException(
                    "No se puede modificar un evento realizado"
            );
        }

        evento.setFecha(
                eventoDTO.getFecha()
        );

        evento.setHora(
                eventoDTO.getHora()
        );

        evento.setDuracionHoras(
                eventoDTO.getDuracionHoras()
        );

        evento.setCiudad(
                eventoDTO.getCiudad().trim()
        );

        evento.setDireccion(
                eventoDTO.getDireccion().trim()
        );

        evento.setInvitados(
                eventoDTO.getInvitados()
        );

        if (eventoDTO.getObservaciones() == null
                || eventoDTO.getObservaciones().isBlank()) {

            evento.setObservaciones(null);

        } else {

            evento.setObservaciones(
                    eventoDTO.getObservaciones().trim()
            );
        }

        if (!"Cancelado".equalsIgnoreCase(
                evento.getEstado()
        )) {

            validarDisponibilidadEventoExistente(evento);
        }

        Evento eventoActualizado =
                eventoRepositorio.save(evento);

        return convertirARespuestaDTO(
                eventoActualizado
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CalendarioEventosDTO obtenerCalendario(
            Integer anio,
            Integer mes
    ) {

        YearMonth mesSeleccionado;

        try {

            mesSeleccionado = YearMonth.of(
                    anio,
                    mes
            );

        } catch (DateTimeException excepcion) {

            throw new IllegalArgumentException(
                    "El mes o el año seleccionado no es válido"
            );
        }

        LocalDate fechaInicial =
                mesSeleccionado.atDay(1);

        LocalDate fechaFinal =
                mesSeleccionado.atEndOfMonth();

        List<Evento> eventosDelMes =
                eventoRepositorio
                        .findByFechaBetweenOrderByFechaAscHoraAsc(
                                fechaInicial,
                                fechaFinal
                        );

        Map<LocalDate, List<EventoRespuestaDTO>>
                eventosPorFecha = new HashMap<>();

        for (Evento evento : eventosDelMes) {

            EventoRespuestaDTO eventoDTO =
                    convertirARespuestaDTO(evento);

            eventosPorFecha
                    .computeIfAbsent(
                            evento.getFecha(),
                            fecha -> new ArrayList<>()
                    )
                    .add(eventoDTO);
        }

        List<DiaCalendarioDTO> dias =
                construirDiasDelCalendario(
                        mesSeleccionado,
                        eventosPorFecha
                );

        YearMonth mesAnterior =
                mesSeleccionado.minusMonths(1);

        YearMonth mesSiguiente =
                mesSeleccionado.plusMonths(1);

        CalendarioEventosDTO calendario =
                new CalendarioEventosDTO();

        calendario.setAnio(
                mesSeleccionado.getYear()
        );

        calendario.setMes(
                mesSeleccionado.getMonthValue()
        );

        calendario.setTitulo(
                crearTituloMes(mesSeleccionado)
        );

        calendario.setAnioAnterior(
                mesAnterior.getYear()
        );

        calendario.setMesAnterior(
                mesAnterior.getMonthValue()
        );

        calendario.setAnioSiguiente(
                mesSiguiente.getYear()
        );

        calendario.setMesSiguiente(
                mesSiguiente.getMonthValue()
        );

        calendario.setCantidadEventos(
                eventosDelMes.size()
        );

        calendario.setDias(dias);

        return calendario;
    }

    private List<DiaCalendarioDTO> construirDiasDelCalendario(
            YearMonth mesSeleccionado,
            Map<LocalDate, List<EventoRespuestaDTO>>
                    eventosPorFecha
    ) {

        List<DiaCalendarioDTO> dias =
                new ArrayList<>();

        LocalDate primerDia =
                mesSeleccionado.atDay(1);

        int espaciosIniciales =
                primerDia.getDayOfWeek().getValue() - 1;

        for (int i = 0; i < espaciosIniciales; i++) {

            dias.add(crearDiaVacio());
        }

        for (
                int numeroDia = 1;
                numeroDia <= mesSeleccionado.lengthOfMonth();
                numeroDia++
        ) {

            LocalDate fecha =
                    mesSeleccionado.atDay(numeroDia);

            List<EventoRespuestaDTO> eventos =
                    eventosPorFecha.getOrDefault(
                            fecha,
                            List.of()
                    );

            DiaCalendarioDTO dia =
                    new DiaCalendarioDTO(
                            numeroDia,
                            fecha,
                            true,
                            eventos
                    );

            dias.add(dia);
        }

        while (dias.size() < 42) {

            dias.add(crearDiaVacio());
        }

        return dias;
    }

    private DiaCalendarioDTO crearDiaVacio() {

        return new DiaCalendarioDTO(
                null,
                null,
                false,
                List.of()
        );
    }

    private String crearTituloMes(
            YearMonth mesSeleccionado
    ) {

        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern(
                        "MMMM 'de' yyyy",
                        new Locale("es", "PE")
                );

        String titulo =
                mesSeleccionado
                        .atDay(1)
                        .format(formato);

        return titulo.substring(0, 1)
                .toUpperCase()
                + titulo.substring(1);
    }

    private void validarDisponibilidad(
            Cotizacion cotizacion
    ) {

        String nombreServicio =
                cotizacion.getServicio().getNombre();

        int duracionSolicitada =
                cotizacion.getHoras() == null
                        ? 1
                        : cotizacion.getHoras();

        LocalDateTime inicioSolicitado =
                LocalDateTime.of(
                        cotizacion.getFechaEvento(),
                        cotizacion.getHoraEvento()
                );

        LocalDateTime finSolicitado =
                inicioSolicitado.plusHours(
                        duracionSolicitada
                );

        List<Evento> posiblesConflictos =
                eventoRepositorio
                        .findByServicioAndFechaBetweenAndEstadoNotOrderByFechaAscHoraAsc(
                                nombreServicio,
                                cotizacion
                                        .getFechaEvento()
                                        .minusDays(1),
                                cotizacion
                                        .getFechaEvento()
                                        .plusDays(1),
                                "Cancelado"
                        );

        for (Evento eventoExistente : posiblesConflictos) {

            int duracionExistente =
                    eventoExistente.getDuracionHoras() == null
                            ? 1
                            : eventoExistente.getDuracionHoras();

            LocalDateTime inicioExistente =
                    LocalDateTime.of(
                            eventoExistente.getFecha(),
                            eventoExistente.getHora()
                    );

            LocalDateTime finExistente =
                    inicioExistente.plusHours(
                            duracionExistente
                    );

            boolean existeSuperposicion =
                    inicioSolicitado.isBefore(
                            finExistente
                    )
                            && inicioExistente.isBefore(
                            finSolicitado
                    );

            if (existeSuperposicion) {

                DateTimeFormatter formato =
                        DateTimeFormatter.ofPattern(
                                "dd/MM/yyyy HH:mm"
                        );

                throw new IllegalArgumentException(
                        "El servicio "
                                + nombreServicio
                                + " ya está reservado desde "
                                + inicioExistente.format(formato)
                                + " hasta "
                                + finExistente.format(formato)
                                + ". Selecciona otro horario o servicio."
                );
            }
        }
    }

    private void validarDisponibilidadEventoExistente(
            Evento evento
    ) {

        int duracionSolicitada =
                evento.getDuracionHoras() == null
                        ? 1
                        : evento.getDuracionHoras();

        LocalDateTime inicioSolicitado =
                LocalDateTime.of(
                        evento.getFecha(),
                        evento.getHora()
                );

        LocalDateTime finSolicitado =
                inicioSolicitado.plusHours(
                        duracionSolicitada
                );

        List<Evento> posiblesConflictos =
                eventoRepositorio
                        .findByServicioAndFechaBetweenAndEstadoNotOrderByFechaAscHoraAsc(
                                evento.getServicio(),
                                evento.getFecha().minusDays(1),
                                evento.getFecha().plusDays(1),
                                "Cancelado"
                        );

        for (Evento eventoExistente : posiblesConflictos) {

            if (evento.getId() != null
                    && evento.getId().equals(
                    eventoExistente.getId()
            )) {

                continue;
            }

            int duracionExistente =
                    eventoExistente.getDuracionHoras() == null
                            ? 1
                            : eventoExistente.getDuracionHoras();

            LocalDateTime inicioExistente =
                    LocalDateTime.of(
                            eventoExistente.getFecha(),
                            eventoExistente.getHora()
                    );

            LocalDateTime finExistente =
                    inicioExistente.plusHours(
                            duracionExistente
                    );

            boolean existeSuperposicion =
                    inicioSolicitado.isBefore(
                            finExistente
                    )
                            && inicioExistente.isBefore(
                            finSolicitado
                    );

            if (existeSuperposicion) {

                DateTimeFormatter formato =
                        DateTimeFormatter.ofPattern(
                                "dd/MM/yyyy HH:mm"
                        );

                throw new IllegalArgumentException(
                        "No se puede reprogramar el evento porque "
                                + evento.getServicio()
                                + " está reservado desde "
                                + inicioExistente.format(formato)
                                + " hasta "
                                + finExistente.format(formato)
                                + "."
                );
            }
        }
    }

    private EventoRespuestaDTO convertirARespuestaDTO(
            Evento evento
    ) {

        EventoRespuestaDTO respuesta =
                new EventoRespuestaDTO();

        respuesta.setId(
                evento.getId()
        );

        respuesta.setCodigo(
                evento.getCodigo()
        );

        respuesta.setCodigoCotizacion(
                generarCodigoCotizacion(
                        evento.getCotizacion().getId()
                )
        );

        respuesta.setNombreCliente(
                evento.getNombreCliente()
        );

        respuesta.setTelefono(
                evento.getTelefono()
        );

        respuesta.setServicio(
                evento.getServicio()
        );

        respuesta.setTipoEvento(
                evento.getTipoEvento()
        );

        respuesta.setFecha(
                evento.getFecha()
        );

        respuesta.setHora(
                evento.getHora()
        );

        respuesta.setDuracionHoras(
                evento.getDuracionHoras()
        );

        respuesta.setCiudad(
                evento.getCiudad()
        );

        respuesta.setDireccion(
                evento.getDireccion()
        );

        respuesta.setInvitados(
                evento.getInvitados()
        );

        respuesta.setPrecio(
                evento.getPrecio()
        );

        respuesta.setEstado(
                evento.getEstado()
        );

        respuesta.setObservaciones(
                evento.getObservaciones()
        );

        return respuesta;
    }

    private String generarCodigoEvento(
            Long cotizacionId
    ) {

        return String.format(
                "EVT-%05d",
                cotizacionId
        );
    }

    private String generarCodigoCotizacion(
            Long cotizacionId
    ) {

        return String.format(
                "COT-%05d",
                cotizacionId
        );
    }
}