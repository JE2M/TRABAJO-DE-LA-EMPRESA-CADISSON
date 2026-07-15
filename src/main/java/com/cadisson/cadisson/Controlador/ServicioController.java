package com.cadisson.cadisson.Controlador;

import com.cadisson.cadisson.DTO.ServicioFormularioDTO;
import com.cadisson.cadisson.Servicio.ServicioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(
            ServicioService servicioService
    ) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public String listarServicios(Model model) {

        model.addAttribute(
                "servicios",
                servicioService.listarServicios()
        );

        return "servicios";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(
            Model model
    ) {

        model.addAttribute(
                "servicio",
                new ServicioFormularioDTO()
        );

        model.addAttribute(
                "modoEdicion",
                false
        );

        return "formularioServicio";
    }

    @PostMapping("/guardar")
    public String guardarServicio(
            @Valid
            @ModelAttribute("servicio")
            ServicioFormularioDTO formularioDTO,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (resultado.hasErrors()) {

            model.addAttribute(
                    "modoEdicion",
                    false
            );

            return "formularioServicio";
        }

        try {

            servicioService.crearServicio(
                    formularioDTO
            );

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "El servicio se creó correctamente"
            );

            return "redirect:/servicios";

        } catch (IllegalArgumentException excepcion) {

            model.addAttribute(
                    "error",
                    excepcion.getMessage()
            );

            model.addAttribute(
                    "modoEdicion",
                    false
            );

            return "formularioServicio";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(
            @PathVariable Long id,
            Model model
    ) {

        model.addAttribute(
                "servicio",
                servicioService.obtenerParaEditar(id)
        );

        model.addAttribute(
                "servicioId",
                id
        );

        model.addAttribute(
                "modoEdicion",
                true
        );

        return "formularioServicio";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarServicio(
            @PathVariable Long id,
            @Valid
            @ModelAttribute("servicio")
            ServicioFormularioDTO formularioDTO,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (resultado.hasErrors()) {

            model.addAttribute(
                    "servicioId",
                    id
            );

            model.addAttribute(
                    "modoEdicion",
                    true
            );

            return "formularioServicio";
        }

        try {

            servicioService.actualizarServicio(
                    id,
                    formularioDTO
            );

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "El servicio se actualizó correctamente"
            );

            return "redirect:/servicios";

        } catch (IllegalArgumentException excepcion) {

            model.addAttribute(
                    "error",
                    excepcion.getMessage()
            );

            model.addAttribute(
                    "servicioId",
                    id
            );

            model.addAttribute(
                    "modoEdicion",
                    true
            );

            return "formularioServicio";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarServicio(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        try {

            servicioService.eliminarServicio(id);

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "El servicio se eliminó correctamente"
            );

        } catch (IllegalArgumentException excepcion) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    excepcion.getMessage()
            );
        }

        return "redirect:/servicios";
    }
}