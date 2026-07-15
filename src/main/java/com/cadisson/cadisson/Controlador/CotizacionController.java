package com.cadisson.cadisson.Controlador;

import com.cadisson.cadisson.DTO.CotizacionRespuestaDTO;
import com.cadisson.cadisson.DTO.CotizacionSolicitudDTO;
import com.cadisson.cadisson.Servicio.ContactoClienteService;
import com.cadisson.cadisson.Servicio.CotizacionService;
import com.cadisson.cadisson.Servicio.EventoService;
import com.cadisson.cadisson.Servicio.ServicioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cotizaciones")
public class CotizacionController {

    private final CotizacionService cotizacionService;
    private final ServicioService servicioService;
    private final EventoService eventoService;
    private final ContactoClienteService contactoClienteService;

    public CotizacionController(
            CotizacionService cotizacionService,
            ServicioService servicioService,
            EventoService eventoService,
            ContactoClienteService contactoClienteService
    ) {
        this.cotizacionService = cotizacionService;
        this.servicioService = servicioService;
        this.eventoService = eventoService;
        this.contactoClienteService =
                contactoClienteService;
    }

    @GetMapping
    public String listarCotizaciones(
            Model model
    ) {

        model.addAttribute(
                "cotizaciones",
                cotizacionService.listarCotizaciones()
        );

        model.addAttribute(
                "estados",
                List.of(
                        "Pendiente",
                        "En revisión",
                        "Aceptada",
                        "Rechazada",
                        "Cancelada"
                )
        );

        return "cotizaciones";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCotizacion(
            Model model
    ) {

        if (!model.containsAttribute("cotizacion")) {

            model.addAttribute(
                    "cotizacion",
                    new CotizacionSolicitudDTO()
            );
        }

        model.addAttribute(
                "servicios",
                servicioService.listarServicios()
        );

        return "cotizacion";
    }

    @PostMapping("/guardar")
    public String guardarCotizacion(
            @Valid
            @ModelAttribute("cotizacion")
            CotizacionSolicitudDTO cotizacionSolicitudDTO,
            BindingResult resultado,
            Model model,
            HttpSession session
    ) {

        if (resultado.hasErrors()) {

            model.addAttribute(
                    "servicios",
                    servicioService.listarServicios()
            );

            return "cotizacion";
        }

        try {

            CotizacionRespuestaDTO cotizacionGuardada =
                    cotizacionService.crearCotizacion(
                            cotizacionSolicitudDTO
                    );

            session.setAttribute(
                    "ultimaCotizacionId",
                    cotizacionGuardada.getId()
            );

            return "redirect:/cotizaciones/confirmacion/"
                    + cotizacionGuardada.getId();

        } catch (IllegalArgumentException excepcion) {

            model.addAttribute(
                    "error",
                    excepcion.getMessage()
            );

            model.addAttribute(
                    "servicios",
                    servicioService.listarServicios()
            );

            return "cotizacion";
        }
    }

    @GetMapping("/confirmacion/{id}")
    public String mostrarConfirmacion(
            @PathVariable Long id,
            HttpSession session,
            Model model
    ) {

        Object ultimaCotizacionId =
                session.getAttribute(
                        "ultimaCotizacionId"
                );

        boolean accesoPermitido =
                ultimaCotizacionId instanceof Long
                        && id.equals(
                        ultimaCotizacionId
                );

        if (!accesoPermitido) {
            return "redirect:/";
        }

        model.addAttribute(
                "cotizacion",
                cotizacionService.buscarPorId(id)
        );

        return "confirmacionCotizacion";
    }

    @GetMapping("/{id}")
    public String mostrarDetalle(
            @PathVariable Long id,
            Model model
    ) {

        CotizacionRespuestaDTO cotizacion =
                cotizacionService.buscarPorId(id);

        model.addAttribute(
                "cotizacion",
                cotizacion
        );

        model.addAttribute(
                "eventoCreado",
                eventoService
                        .existeEventoParaCotizacion(id)
        );

        model.addAttribute(
                "contacto",
                contactoClienteService
                        .generarEnlaces(cotizacion)
        );

        return "detalleCotizacion";
    }

    @PostMapping("/{id}/estado")
    public String actualizarEstado(
            @PathVariable Long id,
            @RequestParam("estado")
            String estado,
            RedirectAttributes redirectAttributes
    ) {

        try {

            cotizacionService.actualizarEstado(
                    id,
                    estado
            );

            if ("Cancelada".equalsIgnoreCase(estado)
                    || "Rechazada".equalsIgnoreCase(estado)) {

                eventoService
                        .cancelarEventoPorCotizacion(id);
            }

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "El estado de la cotización se actualizó correctamente"
            );

        } catch (IllegalArgumentException excepcion) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    excepcion.getMessage()
            );
        }

        return "redirect:/cotizaciones";
    }

    @PostMapping("/{id}/convertir-evento")
    public String convertirEnEvento(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        try {

            eventoService
                    .convertirCotizacionEnEvento(id);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "La cotización se convirtió en evento correctamente"
            );

            return "redirect:/eventos";

        } catch (IllegalArgumentException excepcion) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    excepcion.getMessage()
            );

            return "redirect:/cotizaciones/" + id;
        }
    }
}