package com.cadisson.cadisson.Controlador;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String mostrarLogin(
            Authentication authentication
    ) {

        boolean usuarioAutenticado =
                authentication != null
                        && authentication.isAuthenticated()
                        && !(authentication
                        instanceof AnonymousAuthenticationToken);

        if (usuarioAutenticado) {
            return "redirect:/admin";
        }

        return "login";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {

        return "error/403";
    }
}