package com.cadisson.cadisson.Controlador;

import com.cadisson.cadisson.Servicio.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DashboardService dashboardService;

    public AdminController(
            DashboardService dashboardService
    ) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String dashboard(
            Authentication authentication,
            Model model
    ) {

        model.addAttribute(
                "correoAdministrador",
                authentication.getName()
        );

        model.addAttribute(
                "resumen",
                dashboardService.obtenerResumen()
        );

        return "admin";
    }
}