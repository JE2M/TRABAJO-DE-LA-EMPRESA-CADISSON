package com.cadisson.cadisson.Servicio;

import com.cadisson.cadisson.DTO.ContactoClienteDTO;
import com.cadisson.cadisson.DTO.CotizacionRespuestaDTO;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class ContactoClienteServiceImpl
        implements ContactoClienteService {

    @Override
    public ContactoClienteDTO generarEnlaces(
            CotizacionRespuestaDTO cotizacion
    ) {

        ContactoClienteDTO contacto =
                new ContactoClienteDTO();

        String mensaje =
                crearMensaje(cotizacion);

        String telefono =
                normalizarTelefono(
                        cotizacion.getTelefono()
                );

        if (!telefono.isBlank()) {

            contacto.setWhatsappUrl(
                    "https://wa.me/"
                            + telefono
                            + "?text="
                            + codificar(mensaje)
            );
        }

        String correo =
                cotizacion.getCorreo();

        if (correo != null
                && !correo.isBlank()) {

            String asunto =
                    "Cotización "
                            + cotizacion.getCodigo()
                            + " - CADISSON";

            contacto.setCorreoUrl(
                    "mailto:"
                            + correo.trim()
                            + "?subject="
                            + codificar(asunto)
                            + "&body="
                            + codificar(mensaje)
            );
        }

        return contacto;
    }

    private String crearMensaje(
            CotizacionRespuestaDTO cotizacion
    ) {

        return "Hola "
                + valorTexto(cotizacion.getNombreCliente())
                + ",\n\n"
                + "Te compartimos la información de tu cotización "
                + valorTexto(cotizacion.getCodigo())
                + " de CADISSON.\n\n"
                + "Servicio: "
                + valorTexto(cotizacion.getServicio())
                + "\n"
                + "Tipo de evento: "
                + valorTexto(cotizacion.getTipoEvento())
                + "\n"
                + "Fecha: "
                + formatearFecha(
                cotizacion.getFechaEvento()
        )
                + "\n"
                + "Hora: "
                + formatearHora(
                cotizacion.getHoraEvento()
        )
                + "\n"
                + "Total estimado: "
                + formatearPrecio(
                cotizacion.getPrecioEstimado()
        )
                + "\n"
                + "Estado: "
                + valorTexto(cotizacion.getEstado())
                + "\n\n"
                + "Gracias por comunicarte con CADISSON.";
    }

    private String normalizarTelefono(
            String telefono
    ) {

        if (telefono == null) {
            return "";
        }

        String numeros =
                telefono.replaceAll(
                        "[^0-9]",
                        ""
                );

        if (numeros.length() == 9) {
            return "51" + numeros;
        }

        if (numeros.startsWith("0051")) {
            return numeros.substring(2);
        }

        return numeros;
    }

    private String codificar(String texto) {

        return URLEncoder
                .encode(
                        texto,
                        StandardCharsets.UTF_8
                )
                .replace("+", "%20");
    }

    private String formatearFecha(
            LocalDate fecha
    ) {

        if (fecha == null) {
            return "No especificada";
        }

        return fecha.format(
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"
                )
        );
    }

    private String formatearHora(
            LocalTime hora
    ) {

        if (hora == null) {
            return "No especificada";
        }

        return hora.format(
                DateTimeFormatter.ofPattern(
                        "HH:mm"
                )
        );
    }

    private String formatearPrecio(
            Number precio
    ) {

        if (precio == null) {
            return "S/ 0.00";
        }

        return String.format(
                Locale.US,
                "S/ %,.2f",
                precio.doubleValue()
        );
    }

    private String valorTexto(
            Object valor
    ) {

        if (valor == null) {
            return "No especificado";
        }

        String texto =
                String.valueOf(valor).trim();

        return texto.isBlank()
                ? "No especificado"
                : texto;
    }
}