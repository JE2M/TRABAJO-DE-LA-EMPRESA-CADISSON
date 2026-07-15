package com.cadisson.cadisson.Controlador;

import com.cadisson.cadisson.DTO.CambioPasswordDTO;
import com.cadisson.cadisson.Servicio.CuentaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(
            CuentaService cuentaService
    ) {
        this.cuentaService = cuentaService;
    }

    @GetMapping("/admin/cuenta")
    public String mostrarCuenta(
            Authentication authentication,
            Model model
    ) {

        model.addAttribute(
                "correoAdministrador",
                authentication.getName()
        );

        model.addAttribute(
                "cambioPassword",
                new CambioPasswordDTO()
        );

        return "cuentaAdministrador";
    }

    @PostMapping("/admin/cuenta/password")
    public String cambiarPassword(
            @Valid
            @ModelAttribute("cambioPassword")
            CambioPasswordDTO cambioPasswordDTO,
            BindingResult resultado,
            Authentication authentication,
            HttpServletRequest request,
            Model model
    ) {

        if (resultado.hasErrors()) {

            model.addAttribute(
                    "correoAdministrador",
                    authentication.getName()
            );

            return "cuentaAdministrador";
        }

        try {

            cuentaService.cambiarPassword(
                    authentication.getName(),
                    cambioPasswordDTO
            );

            HttpSession session =
                    request.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            SecurityContextHolder.clearContext();

            return "redirect:/login?passwordChanged=true";

        } catch (IllegalArgumentException excepcion) {

            model.addAttribute(
                    "error",
                    excepcion.getMessage()
            );

            model.addAttribute(
                    "correoAdministrador",
                    authentication.getName()
            );

            return "cuentaAdministrador";
        }
    }
}