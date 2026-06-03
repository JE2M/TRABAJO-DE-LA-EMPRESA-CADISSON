package com.cadisson.springboot_applications.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/parámetros")
public class RquestParamControllers {

    @GetMapping("/detalle")
    public ParametroDTO detalle(@RequestParam (required = false , defaultValue = "hola a todos")String información){
        ParametroDTO parametro1 = new ParametroDTO();
        parametro1.setInformación( información);
        return parametro1;

    }
    

}
