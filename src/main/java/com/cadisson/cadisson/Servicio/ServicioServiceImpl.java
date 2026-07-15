package com.cadisson.cadisson.Servicio;

import com.cadisson.cadisson.DTO.ServicioDTO;
import com.cadisson.cadisson.DTO.ServicioFormularioDTO;
import com.cadisson.cadisson.Repositorios.CotizacionRepositorio;
import com.cadisson.cadisson.Repositorios.ServicioRepositorio;
import com.cadisson.cadisson.models.Servicio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepositorio servicioRepositorio;
    private final CotizacionRepositorio cotizacionRepositorio;

    public ServicioServiceImpl(
            ServicioRepositorio servicioRepositorio,
            CotizacionRepositorio cotizacionRepositorio
    ) {
        this.servicioRepositorio = servicioRepositorio;
        this.cotizacionRepositorio = cotizacionRepositorio;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServicioDTO> listarServicios() {

        return servicioRepositorio
                .findAllByOrderByNombreAsc()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioDTO buscarPorId(Long id) {

        Servicio servicio = buscarEntidad(id);

        return convertirADTO(servicio);
    }

    @Override
    @Transactional(readOnly = true)
    public ServicioFormularioDTO obtenerParaEditar(Long id) {

        Servicio servicio = buscarEntidad(id);

        ServicioFormularioDTO formulario =
                new ServicioFormularioDTO();

        formulario.setNombre(servicio.getNombre());
        formulario.setDescripcion(servicio.getDescripcion());
        formulario.setPrecio(servicio.getPrecio());
        formulario.setDuracion(servicio.getDuracion());
        formulario.setCategoria(servicio.getCategoria());
        formulario.setImagen(servicio.getImagen());

        return formulario;
    }

    @Override
    public ServicioDTO crearServicio(
            ServicioFormularioDTO formularioDTO
    ) {

        String nombre = formularioDTO
                .getNombre()
                .trim();

        if (servicioRepositorio
                .existsByNombreIgnoreCase(nombre)) {

            throw new IllegalArgumentException(
                    "Ya existe un servicio con ese nombre"
            );
        }

        Servicio servicio = new Servicio();

        copiarFormularioAEntidad(
                formularioDTO,
                servicio
        );

        Servicio servicioGuardado =
                servicioRepositorio.save(servicio);

        return convertirADTO(servicioGuardado);
    }

    @Override
    public ServicioDTO actualizarServicio(
            Long id,
            ServicioFormularioDTO formularioDTO
    ) {

        Servicio servicio = buscarEntidad(id);

        String nombre = formularioDTO
                .getNombre()
                .trim();

        boolean nombreDuplicado =
                servicioRepositorio
                        .existsByNombreIgnoreCaseAndIdNot(
                                nombre,
                                id
                        );

        if (nombreDuplicado) {

            throw new IllegalArgumentException(
                    "Ya existe otro servicio con ese nombre"
            );
        }

        copiarFormularioAEntidad(
                formularioDTO,
                servicio
        );

        Servicio servicioActualizado =
                servicioRepositorio.save(servicio);

        return convertirADTO(servicioActualizado);
    }

    @Override
    public void eliminarServicio(Long id) {

        Servicio servicio = buscarEntidad(id);

        boolean tieneCotizaciones =
                cotizacionRepositorio
                        .existsByServicio_Id(id);

        if (tieneCotizaciones) {

            throw new IllegalArgumentException(
                    "No se puede eliminar el servicio porque tiene cotizaciones asociadas"
            );
        }

        servicioRepositorio.delete(servicio);
    }

    private Servicio buscarEntidad(Long id) {

        return servicioRepositorio
                .findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "No se encontró el servicio"
                        )
                );
    }

    private void copiarFormularioAEntidad(
            ServicioFormularioDTO formulario,
            Servicio servicio
    ) {

        servicio.setNombre(
                formulario.getNombre().trim()
        );

        servicio.setDescripcion(
                formulario.getDescripcion().trim()
        );

        servicio.setPrecio(
                formulario.getPrecio()
        );

        servicio.setDuracion(
                formulario.getDuracion()
        );

        servicio.setCategoria(
                formulario.getCategoria().trim()
        );

        servicio.setImagen(
                formulario.getImagen().trim()
        );
    }

    private ServicioDTO convertirADTO(
            Servicio servicio
    ) {

        ServicioDTO dto = new ServicioDTO();

        dto.setId(servicio.getId());
        dto.setNombre(servicio.getNombre());
        dto.setDescripcion(servicio.getDescripcion());
        dto.setPrecio(servicio.getPrecio());
        dto.setDuracion(servicio.getDuracion());
        dto.setCategoria(servicio.getCategoria());
        dto.setImagen(servicio.getImagen());

        return dto;
    }
}