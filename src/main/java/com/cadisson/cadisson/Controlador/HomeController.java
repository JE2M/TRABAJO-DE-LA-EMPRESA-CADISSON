package com.cadisson.cadisson.Controlador;

import com.cadisson.cadisson.Servicio.ServicioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ServicioService servicioService;

    public HomeController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping("/")
    public String inicio(Model model) {

        model.addAttribute(
                "servicios",
                servicioService.listarServicios()
        );

        return "index";
    }
}