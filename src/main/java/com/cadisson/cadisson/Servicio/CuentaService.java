package com.cadisson.cadisson.Servicio;

import com.cadisson.cadisson.DTO.CambioPasswordDTO;

public interface CuentaService {

    void cambiarPassword(
            String correo,
            CambioPasswordDTO cambioPasswordDTO
    );
}