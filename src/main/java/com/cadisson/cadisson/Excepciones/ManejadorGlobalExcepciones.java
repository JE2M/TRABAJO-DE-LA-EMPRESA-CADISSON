package com.cadisson.cadisson.Excepciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ManejadorGlobalExcepciones {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    ManejadorGlobalExcepciones.class
            );

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String manejarArgumentoInvalido(
            IllegalArgumentException excepcion,
            Model model
    ) {

        logger.warn(
                "Solicitud inválida: {}",
                excepcion.getMessage()
        );

        model.addAttribute(
                "mensaje",
                excepcion.getMessage()
        );

        return "error/400";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String manejarErrorGeneral(
            Exception excepcion,
            Model model
    ) {

        logger.error(
                "Error interno no controlado",
                excepcion
        );

        model.addAttribute(
                "mensaje",
                "Ocurrió un error inesperado al procesar la solicitud."
        );

        return "error/500";
    }
}