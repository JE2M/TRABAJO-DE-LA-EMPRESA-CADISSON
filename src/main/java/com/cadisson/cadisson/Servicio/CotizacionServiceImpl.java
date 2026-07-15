package com.cadisson.cadisson.Servicio;

import com.cadisson.cadisson.DTO.CotizacionRespuestaDTO;
import com.cadisson.cadisson.DTO.CotizacionSolicitudDTO;
import com.cadisson.cadisson.Repositorios.CotizacionRepositorio;
import com.cadisson.cadisson.Repositorios.ServicioRepositorio;
import com.cadisson.cadisson.models.Cotizacion;
import com.cadisson.cadisson.models.Servicio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CotizacionServiceImpl implements CotizacionService {

    private static final Set<String> ESTADOS_PERMITIDOS =
            Set.of(
                    "Pendiente",
                    "En revisión",
                    "Contactado",
                    "Aceptada",
                    "Rechazada",
                    "Cancelada"
            );

    private static final BigDecimal PRECIO_PAQUETE_PREMIUM =
            new BigDecimal("250.00");

    private static final BigDecimal PRECIO_PAQUETE_VIP =
            new BigDecimal("500.00");

    private static final BigDecimal PRECIO_HORA_ADICIONAL =
            new BigDecimal("150.00");

    private static final BigDecimal PRECIO_SONIDO =
            new BigDecimal("150.00");

    private static final BigDecimal PRECIO_LUCES =
            new BigDecimal("200.00");

    private static final BigDecimal PRECIO_PANTALLA_LED =
            new BigDecimal("300.00");

    private static final BigDecimal PRECIO_MAESTRO_CEREMONIA =
            new BigDecimal("250.00");

    private static final BigDecimal PRECIO_TRANSPORTE =
            new BigDecimal("100.00");

    private final CotizacionRepositorio cotizacionRepositorio;
    private final ServicioRepositorio servicioRepositorio;

    public CotizacionServiceImpl(
            CotizacionRepositorio cotizacionRepositorio,
            ServicioRepositorio servicioRepositorio
    ) {
        this.cotizacionRepositorio = cotizacionRepositorio;
        this.servicioRepositorio = servicioRepositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CotizacionRespuestaDTO> listarCotizaciones() {

        return cotizacionRepositorio
                .findAll()
                .stream()
                .map(this::convertirARespuestaDTO)
                .toList();
    }

    @Override
    public CotizacionRespuestaDTO crearCotizacion(
            CotizacionSolicitudDTO solicitudDTO
    ) {

        Servicio servicio = servicioRepositorio
                .findById(solicitudDTO.getServicioId())
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "El servicio seleccionado no existe"
                        )
                );

        Cotizacion cotizacion = convertirAEntidad(
                solicitudDTO,
                servicio
        );

        BigDecimal precioTotal = calcularPrecio(
                solicitudDTO,
                servicio
        );

        cotizacion.setPrecio(precioTotal.doubleValue());
        cotizacion.setEstado("Pendiente");

        Cotizacion cotizacionGuardada =
                cotizacionRepositorio.save(cotizacion);

        return convertirARespuestaDTO(
                cotizacionGuardada
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CotizacionRespuestaDTO buscarPorId(Long id) {

        Cotizacion cotizacion = cotizacionRepositorio
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "No se encontró la cotización"
                        )
                );

        return convertirARespuestaDTO(cotizacion);
    }

    @Override
    public CotizacionRespuestaDTO actualizarEstado(
            Long id,
            String nuevoEstado
    ) {

        if (nuevoEstado == null
                || !ESTADOS_PERMITIDOS.contains(nuevoEstado)) {

            throw new IllegalArgumentException(
                    "El estado seleccionado no es válido"
            );
        }

        Cotizacion cotizacion = cotizacionRepositorio
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "No se encontró la cotización"
                        )
                );

        cotizacion.setEstado(nuevoEstado);

        Cotizacion cotizacionActualizada =
                cotizacionRepositorio.save(cotizacion);

        return convertirARespuestaDTO(
                cotizacionActualizada
        );
    }

    @Override
    public void eliminarCotizacion(Long id) {

        if (!cotizacionRepositorio.existsById(id)) {

            throw new IllegalArgumentException(
                    "No se encontró la cotización"
            );
        }

        cotizacionRepositorio.deleteById(id);
    }

    private Cotizacion convertirAEntidad(
            CotizacionSolicitudDTO dto,
            Servicio servicio
    ) {

        Cotizacion cotizacion = new Cotizacion();

        cotizacion.setNombreCliente(
                dto.getNombreCliente().trim()
        );

        cotizacion.setCorreo(
                dto.getCorreo()
                        .trim()
                        .toLowerCase()
        );

        cotizacion.setTelefono(
                dto.getTelefono().trim()
        );

        cotizacion.setCiudad(
                dto.getCiudad().trim()
        );

        cotizacion.setDireccion(
                dto.getDireccion().trim()
        );

        cotizacion.setTipoEvento(
                dto.getTipoEvento()
        );

        cotizacion.setServicio(servicio);

        cotizacion.setPaquete(
                dto.getPaquete()
        );

        cotizacion.setHoras(
                dto.getHoras()
        );

        cotizacion.setFechaEvento(
                dto.getFechaEvento()
        );

        cotizacion.setHoraEvento(
                dto.getHoraEvento()
        );

        cotizacion.setInvitados(
                dto.getInvitados()
        );

        if (dto.getObservaciones() != null
                && !dto.getObservaciones().isBlank()) {

            cotizacion.setObservaciones(
                    dto.getObservaciones().trim()
            );
        }

        cotizacion.setSonido(
                Boolean.TRUE.equals(dto.getSonido())
        );

        cotizacion.setLuces(
                Boolean.TRUE.equals(dto.getLuces())
        );

        cotizacion.setPantallaLed(
                Boolean.TRUE.equals(dto.getPantallaLed())
        );

        cotizacion.setMaestroCeremonia(
                Boolean.TRUE.equals(
                        dto.getMaestroCeremonia()
                )
        );

        cotizacion.setTransporte(
                Boolean.TRUE.equals(dto.getTransporte())
        );

        return cotizacion;
    }

    private BigDecimal calcularPrecio(
            CotizacionSolicitudDTO dto,
            Servicio servicio
    ) {

        BigDecimal total = BigDecimal.valueOf(
                servicio.getPrecio() == null
                        ? 0.0
                        : servicio.getPrecio()
        );

        if ("Premium".equalsIgnoreCase(
                dto.getPaquete()
        )) {

            total = total.add(
                    PRECIO_PAQUETE_PREMIUM
            );

        } else if ("VIP".equalsIgnoreCase(
                dto.getPaquete()
        )) {

            total = total.add(
                    PRECIO_PAQUETE_VIP
            );
        }

        int duracionIncluida =
                servicio.getDuracion() == null
                        ? 1
                        : servicio.getDuracion();

        if (dto.getHoras() != null
                && dto.getHoras() > duracionIncluida) {

            int horasAdicionales =
                    dto.getHoras() - duracionIncluida;

            BigDecimal costoHoras =
                    PRECIO_HORA_ADICIONAL.multiply(
                            BigDecimal.valueOf(
                                    horasAdicionales
                            )
                    );

            total = total.add(costoHoras);
        }

        if (Boolean.TRUE.equals(dto.getSonido())) {

            total = total.add(
                    PRECIO_SONIDO
            );
        }

        if (Boolean.TRUE.equals(dto.getLuces())) {

            total = total.add(
                    PRECIO_LUCES
            );
        }

        if (Boolean.TRUE.equals(
                dto.getPantallaLed()
        )) {

            total = total.add(
                    PRECIO_PANTALLA_LED
            );
        }

        if (Boolean.TRUE.equals(
                dto.getMaestroCeremonia()
        )) {

            total = total.add(
                    PRECIO_MAESTRO_CEREMONIA
            );
        }

        if (Boolean.TRUE.equals(
                dto.getTransporte()
        )) {

            total = total.add(
                    PRECIO_TRANSPORTE
            );
        }

        return total;
    }

    private CotizacionRespuestaDTO convertirARespuestaDTO(
            Cotizacion cotizacion
    ) {

        CotizacionRespuestaDTO respuesta =
                new CotizacionRespuestaDTO();

        respuesta.setId(cotizacion.getId());

        respuesta.setCodigo(
                generarCodigo(cotizacion.getId())
        );

        respuesta.setNombreCliente(
                cotizacion.getNombreCliente()
        );

        respuesta.setCorreo(
                cotizacion.getCorreo()
        );

        respuesta.setTelefono(
                cotizacion.getTelefono()
        );

        respuesta.setCiudad(
                cotizacion.getCiudad()
        );

        respuesta.setDireccion(
                cotizacion.getDireccion()
        );

        respuesta.setTipoEvento(
                cotizacion.getTipoEvento()
        );

        respuesta.setServicio(
                cotizacion.getServicio().getNombre()
        );

        respuesta.setPaquete(
                cotizacion.getPaquete()
        );

        respuesta.setHoras(
                cotizacion.getHoras()
        );

        respuesta.setInvitados(
                cotizacion.getInvitados()
        );

        respuesta.setFechaEvento(
                cotizacion.getFechaEvento()
        );

        respuesta.setHoraEvento(
                cotizacion.getHoraEvento()
        );

        respuesta.setSonido(
                cotizacion.getSonido()
        );

        respuesta.setLuces(
                cotizacion.getLuces()
        );

        respuesta.setPantallaLed(
                cotizacion.getPantallaLed()
        );

        respuesta.setMaestroCeremonia(
                cotizacion.getMaestroCeremonia()
        );

        respuesta.setTransporte(
                cotizacion.getTransporte()
        );

        respuesta.setObservaciones(
                cotizacion.getObservaciones()
        );

        respuesta.setPrecioEstimado(
                cotizacion.getPrecio()
        );

        respuesta.setEstado(
                cotizacion.getEstado()
        );

        return respuesta;
    }

    private String generarCodigo(Long id) {

        if (id == null) {
            return "COT-PENDIENTE";
        }

        return String.format(
                "COT-%05d",
                id
        );
    }
}