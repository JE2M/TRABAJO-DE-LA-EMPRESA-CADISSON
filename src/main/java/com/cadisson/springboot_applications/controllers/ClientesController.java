package com.cadisson.springboot_applications.controllers;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import com.cadisson.springboot_applications.models.Empleados;

@Controller
public class ClientesController {

    @GetMapping("/detalles_info")

    public String info(Model model){ //ES COMO UNA DTO//    
        Empleados empleado1 = new Empleados("Juan", "Perez", "Manuel Jardo", "Gerente",
                                             35, 12131243, 001);
        model.addAttribute("Titulo", "Información del Empleado");
        model.addAttribute("Empleado", empleado1);

        return "detalles_info";

    }

    @ModelAttribute("Empleados")
    public List<Empleados> ListaEmpleados() {
        return Arrays.asList(
            new Empleados("Nestor", "Puma", "San Jose", "Asesor",
            20, 12131243, 001),
            new Empleados("Maria", "Gomez", "San Pedro", "Secretaria",
            25, 1231254, 002),
            new Empleados("Carlos", "Lopez", "San Juan", "Vendedor",
            23, 123124, 003),
            new Empleados("Ana", "Martinez", "San Carlos", "Contadora",
            32, 1231234, 004),
            new Empleados("Luis", "Garcia", "San Miguel", "Analista",
            18, 999999, 005),
            new Empleados("Sofia", "Rodriguez", "San Rafael", "Diseñadora",
            30, 1203987123, 006)

        );

    }


}
