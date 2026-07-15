package com.cadisson.cadisson.Servicio;

import com.cadisson.cadisson.DTO.ServicioDTO;
import com.cadisson.cadisson.DTO.ServicioFormularioDTO;

import java.util.List;

public interface ServicioService {

    List<ServicioDTO> listarServicios();

    ServicioDTO buscarPorId(Long id);

    ServicioFormularioDTO obtenerParaEditar(Long id);

    ServicioDTO crearServicio(
            ServicioFormularioDTO formularioDTO
    );

    ServicioDTO actualizarServicio(
            Long id,
            ServicioFormularioDTO formularioDTO
    );

    void eliminarServicio(Long id);
}