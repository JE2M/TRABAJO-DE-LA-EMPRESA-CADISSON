package com.cadisson.springboot_applications.controllers;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cadisson.springboot_applications.models.Empleados;
import com.cadisson.springboot_applications.models.dto.ClaseDTO;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@RestController //Sus métodos manejaran solicitudes tipo Rest//
@RequestMapping("/api") //Ruta de primer nivel porque esta encima del controlador//


public class EjemploRestController { //Clase pública ejemplo RestController//

    @RequestMapping(path = "/detalles_info2") 

    public ClaseDTO detalles_info2() {
        ClaseDTO usuario1 = new ClaseDTO();
        usuario1.setTitulo("Administrador");
        usuario1.setUsuario("Cadisson");

        return usuario1; //Emos cambiado la ruta//

    }


}
