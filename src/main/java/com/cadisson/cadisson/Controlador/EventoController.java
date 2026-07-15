package com.cadisson.cadisson.Controlador;

import com.cadisson.cadisson.DTO.EventoEdicionDTO;
import com.cadisson.cadisson.DTO.EventoEstadoDTO;
import com.cadisson.cadisson.Servicio.EventoService;
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

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(
            EventoService eventoService
    ) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public String listarEventos(Model model) {

        model.addAttribute(
                "eventos",
                eventoService.listarEventos()
        );

        model.addAttribute(
                "estadosEvento",
                List.of(
                        "Confirmado",
                        "En preparación",
                        "Realizado",
                        "Cancelado"
                )
        );

        return "eventos";
    }

    @GetMapping("/calendario")
    public String mostrarCalendario(
            @RequestParam(required = false) Integer anio,
            @RequestParam(required = false) Integer mes,
            Model model
    ) {

        LocalDate hoy = LocalDate.now();

        int anioSeleccionado =
                anio == null
                        ? hoy.getYear()
                        : anio;

        int mesSeleccionado =
                mes == null
                        ? hoy.getMonthValue()
                        : mes;

        if (mesSeleccionado < 1 || mesSeleccionado > 12) {
            anioSeleccionado = hoy.getYear();
            mesSeleccionado = hoy.getMonthValue();
        }

        model.addAttribute(
                "calendario",
                eventoService.obtenerCalendario(
                        anioSeleccionado,
                        mesSeleccionado
                )
        );

        model.addAttribute("hoy", hoy);

        return "calendarioEventos";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicion(
            @PathVariable Long id,
            Model model
    ) {

        model.addAttribute(
                "evento",
                eventoService.obtenerEventoParaEditar(id)
        );

        model.addAttribute(
                "eventoActual",
                eventoService.buscarPorId(id)
        );

        return "editarEvento";
    }

    @PostMapping("/{id}/editar")
    public String actualizarEvento(
            @PathVariable Long id,
            @Valid
            @ModelAttribute("evento")
            EventoEdicionDTO eventoDTO,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (resultado.hasErrors()) {

            model.addAttribute(
                    "eventoActual",
                    eventoService.buscarPorId(id)
            );

            return "editarEvento";
        }

        try {

            eventoService.actualizarEvento(
                    id,
                    eventoDTO
            );

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "El evento se actualizó correctamente"
            );

            return "redirect:/eventos";

        } catch (IllegalArgumentException excepcion) {

            model.addAttribute(
                    "error",
                    excepcion.getMessage()
            );

            model.addAttribute(
                    "eventoActual",
                    eventoService.buscarPorId(id)
            );

            return "editarEvento";
        }
    }

    @PostMapping("/{id}/estado")
    public String actualizarEstado(
            @PathVariable Long id,
            @Valid
            @ModelAttribute
            EventoEstadoDTO estadoDTO,
            BindingResult resultado,
            RedirectAttributes redirectAttributes
    ) {

        if (resultado.hasErrors()) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    resultado.getAllErrors()
                            .get(0)
                            .getDefaultMessage()
            );

            return "redirect:/eventos";
        }

        try {

            eventoService.actualizarEstado(
                    id,
                    estadoDTO.getEstado()
            );

            redirectAttributes.addFlashAttribute(
                    "mensaje",
                    "El estado del evento se actualizó correctamente"
            );

        } catch (IllegalArgumentException excepcion) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    excepcion.getMessage()
            );
        }

        return "redirect:/eventos";
    }
}