package com.cadisson.springboot_applications.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cadisson.springboot_applications.models.Empleados;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/variable")
public class PathVariableController {

    @GetMapping("/página1/{mensaje}")
    public ParametroDTO página1(@PathVariable String mensaje){
        ParametroDTO parametro1 = new ParametroDTO();
        parametro1.setInformación(mensaje);
        return parametro1;
    }
    
    @PostMapping("/solicitud")
    public Empleados crearEmpleado(@RequestBody Empleados empleado1){
    return empleado1;
    }
    

}
