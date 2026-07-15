package com.cadisson.cadisson.Servicio;

import com.cadisson.cadisson.DTO.ContactoClienteDTO;
import com.cadisson.cadisson.DTO.CotizacionRespuestaDTO;

public interface ContactoClienteService {

    ContactoClienteDTO generarEnlaces(
            CotizacionRespuestaDTO cotizacion
    );
}