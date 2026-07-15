package com.cadisson.cadisson.Servicio;

import com.cadisson.cadisson.Repositorios.CotizacionRepositorio;
import com.cadisson.cadisson.models.Cotizacion;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class CotizacionPdfServiceImpl
        implements CotizacionPdfService {

    private static final float MARGEN = 50f;

    private static final float ANCHO_CONTENIDO =
            PDRectangle.A4.getWidth() - MARGEN * 2;

    private static final PDFont FUENTE_NORMAL =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA
            );

    private static final PDFont FUENTE_NEGRITA =
            new PDType1Font(
                    Standard14Fonts.FontName.HELVETICA_BOLD
            );

    private static final Color DORADO =
            new Color(219, 165, 18);

    private static final Color NEGRO =
            new Color(20, 20, 24);

    private static final Color GRIS_TEXTO =
            new Color(70, 70, 76);

    private static final Color GRIS_CLARO =
            new Color(241, 241, 243);

    private static final Color GRIS_BORDE =
            new Color(205, 205, 210);

    private static final Color BLANCO =
            new Color(255, 255, 255);

    private final CotizacionRepositorio cotizacionRepositorio;

    public CotizacionPdfServiceImpl(
            CotizacionRepositorio cotizacionRepositorio
    ) {
        this.cotizacionRepositorio =
                cotizacionRepositorio;
    }

    @Override
    public byte[] generarPdf(Long cotizacionId) {

        Cotizacion cotizacion =
                cotizacionRepositorio
                        .findById(cotizacionId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "No se encontró la cotización"
                                )
                        );

        try (
                PDDocument documento = new PDDocument();
                ByteArrayOutputStream salida =
                        new ByteArrayOutputStream()
        ) {

            configurarInformacion(
                    documento,
                    cotizacion
            );

            ContextoPdf contexto =
                    new ContextoPdf(documento);

            contexto.nuevaPagina();

            dibujarEncabezado(
                    contexto,
                    cotizacion
            );

            dibujarDatosCliente(
                    contexto,
                    cotizacion
            );

            dibujarDatosEvento(
                    contexto,
                    cotizacion
            );

            dibujarServicio(
                    contexto,
                    cotizacion
            );

            dibujarExtras(
                    contexto,
                    cotizacion
            );

            dibujarObservaciones(
                    contexto,
                    cotizacion
            );

            dibujarTotal(
                    contexto,
                    cotizacion
            );

            dibujarPiePagina(contexto);

            contexto.cerrar();

            documento.save(salida);

            return salida.toByteArray();

        } catch (IOException excepcion) {

            throw new IllegalStateException(
                    "No se pudo generar el archivo PDF",
                    excepcion
            );
        }
    }

    private void configurarInformacion(
            PDDocument documento,
            Cotizacion cotizacion
    ) {

        PDDocumentInformation informacion =
                new PDDocumentInformation();

        informacion.setTitle(
                "Cotización "
                        + generarCodigo(cotizacion.getId())
        );

        informacion.setAuthor("CADISSON");

        informacion.setSubject(
                "Cotización de servicio musical"
        );

        informacion.setCreator(
                "Sistema administrativo CADISSON"
        );

        documento.setDocumentInformation(informacion);
    }

    private void dibujarEncabezado(
            ContextoPdf contexto,
            Cotizacion cotizacion
    ) throws IOException {

        contexto.asegurarEspacio(110);

        float parteSuperior = contexto.y;

        contexto.contenido.setNonStrokingColor(
                DORADO
        );

        contexto.contenido.addRect(
                MARGEN,
                parteSuperior - 80,
                ANCHO_CONTENIDO,
                80
        );

        contexto.contenido.fill();

        escribirTexto(
                contexto,
                "CADISSON",
                FUENTE_NEGRITA,
                25,
                MARGEN + 20,
                parteSuperior - 32,
                NEGRO
        );

        escribirTexto(
                contexto,
                "COTIZACION DE SERVICIO MUSICAL",
                FUENTE_NEGRITA,
                11,
                MARGEN + 20,
                parteSuperior - 54,
                NEGRO
        );

        escribirTexto(
                contexto,
                generarCodigo(cotizacion.getId()),
                FUENTE_NEGRITA,
                14,
                MARGEN + 355,
                parteSuperior - 32,
                NEGRO
        );

        escribirTexto(
                contexto,
                "Emitida: "
                        + LocalDate.now().format(
                        DateTimeFormatter.ofPattern(
                                "dd/MM/yyyy"
                        )
                ),
                FUENTE_NORMAL,
                9,
                MARGEN + 355,
                parteSuperior - 53,
                NEGRO
        );

        contexto.y = parteSuperior - 102;
    }

    private void dibujarDatosCliente(
            ContextoPdf contexto,
            Cotizacion cotizacion
    ) throws IOException {

        dibujarTituloSeccion(
                contexto,
                "DATOS DEL CLIENTE"
        );

        escribirCampo(
                contexto,
                "Nombre",
                cotizacion.getNombreCliente()
        );

        escribirCampo(
                contexto,
                "Correo",
                cotizacion.getCorreo()
        );

        escribirCampo(
                contexto,
                "Telefono",
                cotizacion.getTelefono()
        );

        dibujarSeparador(contexto);
    }

    private void dibujarDatosEvento(
            ContextoPdf contexto,
            Cotizacion cotizacion
    ) throws IOException {

        dibujarTituloSeccion(
                contexto,
                "DATOS DEL EVENTO"
        );

        escribirCampo(
                contexto,
                "Tipo de evento",
                cotizacion.getTipoEvento()
        );

        escribirCampo(
                contexto,
                "Fecha",
                formatearFecha(
                        cotizacion.getFechaEvento()
                )
        );

        escribirCampo(
                contexto,
                "Hora",
                formatearHora(
                        cotizacion.getHoraEvento()
                )
        );

        escribirCampo(
                contexto,
                "Ciudad",
                cotizacion.getCiudad()
        );

        escribirCampo(
                contexto,
                "Direccion",
                cotizacion.getDireccion()
        );

        escribirCampo(
                contexto,
                "Invitados",
                valorTexto(cotizacion.getInvitados())
        );

        dibujarSeparador(contexto);
    }

    private void dibujarServicio(
            ContextoPdf contexto,
            Cotizacion cotizacion
    ) throws IOException {

        dibujarTituloSeccion(
                contexto,
                "SERVICIO CONTRATADO"
        );

        String nombreServicio =
                cotizacion.getServicio() == null
                        ? "No especificado"
                        : cotizacion
                          .getServicio()
                          .getNombre();

        escribirCampo(
                contexto,
                "Servicio",
                nombreServicio
        );

        escribirCampo(
                contexto,
                "Paquete",
                valorTexto(cotizacion.getPaquete())
        );

        escribirCampo(
                contexto,
                "Horas contratadas",
                valorTexto(cotizacion.getHoras())
        );

        dibujarSeparador(contexto);
    }

    private void dibujarExtras(
            ContextoPdf contexto,
            Cotizacion cotizacion
    ) throws IOException {

        dibujarTituloSeccion(
                contexto,
                "SERVICIOS ADICIONALES"
        );

        escribirParrafo(
                contexto,
                obtenerExtras(cotizacion),
                FUENTE_NORMAL,
                11,
                GRIS_TEXTO,
                15
        );

        dibujarSeparador(contexto);
    }

    private void dibujarObservaciones(
            ContextoPdf contexto,
            Cotizacion cotizacion
    ) throws IOException {

        dibujarTituloSeccion(
                contexto,
                "OBSERVACIONES"
        );

        String observaciones =
                cotizacion.getObservaciones();

        if (observaciones == null
                || observaciones.isBlank()) {

            observaciones =
                    "No se registraron observaciones.";
        }

        escribirParrafo(
                contexto,
                observaciones,
                FUENTE_NORMAL,
                11,
                GRIS_TEXTO,
                15
        );

        dibujarSeparador(contexto);
    }

    private void dibujarTotal(
            ContextoPdf contexto,
            Cotizacion cotizacion
    ) throws IOException {

        contexto.asegurarEspacio(90);

        float parteSuperior = contexto.y;

        contexto.contenido.setNonStrokingColor(
                GRIS_CLARO
        );

        contexto.contenido.addRect(
                MARGEN,
                parteSuperior - 68,
                ANCHO_CONTENIDO,
                68
        );

        contexto.contenido.fill();

        contexto.contenido.setStrokingColor(
                DORADO
        );

        contexto.contenido.addRect(
                MARGEN,
                parteSuperior - 68,
                ANCHO_CONTENIDO,
                68
        );

        contexto.contenido.stroke();

        escribirTexto(
                contexto,
                "TOTAL ESTIMADO",
                FUENTE_NEGRITA,
                11,
                MARGEN + 18,
                parteSuperior - 27,
                GRIS_TEXTO
        );

        escribirTexto(
                contexto,
                formatearPrecio(
                        cotizacion.getPrecio()
                ),
                FUENTE_NEGRITA,
                22,
                MARGEN + 315,
                parteSuperior - 31,
                DORADO
        );

        escribirTexto(
                contexto,
                "Estado: "
                        + valorTexto(
                        cotizacion.getEstado()
                ),
                FUENTE_NORMAL,
                10,
                MARGEN + 18,
                parteSuperior - 49,
                GRIS_TEXTO
        );

        contexto.y = parteSuperior - 85;
    }

    private void dibujarPiePagina(
            ContextoPdf contexto
    ) throws IOException {

        escribirParrafo(
                contexto,
                "Documento generado por el sistema administrativo CADISSON. "
                        + "El precio puede variar si se modifican las condiciones del evento.",
                FUENTE_NORMAL,
                8,
                GRIS_TEXTO,
                11
        );
    }

    private void dibujarTituloSeccion(
            ContextoPdf contexto,
            String titulo
    ) throws IOException {

        contexto.asegurarEspacio(38);

        float parteSuperior = contexto.y;

        contexto.contenido.setNonStrokingColor(
                NEGRO
        );

        contexto.contenido.addRect(
                MARGEN,
                parteSuperior - 25,
                ANCHO_CONTENIDO,
                25
        );

        contexto.contenido.fill();

        escribirTexto(
                contexto,
                titulo,
                FUENTE_NEGRITA,
                10,
                MARGEN + 12,
                parteSuperior - 17,
                BLANCO
        );

        contexto.y = parteSuperior - 37;
    }

    private void escribirCampo(
            ContextoPdf contexto,
            String etiqueta,
            String valor
    ) throws IOException {

        escribirParrafo(
                contexto,
                etiqueta + ": " + valorTexto(valor),
                FUENTE_NORMAL,
                11,
                GRIS_TEXTO,
                15
        );
    }

    private void escribirParrafo(
            ContextoPdf contexto,
            String texto,
            PDFont fuente,
            float tamano,
            Color color,
            float altoLinea
    ) throws IOException {

        List<String> lineas =
                dividirLineas(
                        textoSeguro(texto),
                        fuente,
                        tamano,
                        ANCHO_CONTENIDO
                );

        for (String linea : lineas) {

            contexto.asegurarEspacio(altoLinea);

            escribirTexto(
                    contexto,
                    linea,
                    fuente,
                    tamano,
                    MARGEN,
                    contexto.y,
                    color
            );

            contexto.y -= altoLinea;
        }

        contexto.y -= 3;
    }

    private void escribirTexto(
            ContextoPdf contexto,
            String texto,
            PDFont fuente,
            float tamano,
            float x,
            float y,
            Color color
    ) throws IOException {

        contexto.contenido.beginText();

        contexto.contenido.setFont(
                fuente,
                tamano
        );

        contexto.contenido.setNonStrokingColor(
                color
        );

        contexto.contenido.newLineAtOffset(
                x,
                y
        );

        contexto.contenido.showText(
                textoSeguro(texto)
        );

        contexto.contenido.endText();
    }

    private void dibujarSeparador(
            ContextoPdf contexto
    ) throws IOException {

        contexto.asegurarEspacio(16);

        contexto.contenido.setStrokingColor(
                GRIS_BORDE
        );

        contexto.contenido.moveTo(
                MARGEN,
                contexto.y
        );

        contexto.contenido.lineTo(
                MARGEN + ANCHO_CONTENIDO,
                contexto.y
        );

        contexto.contenido.stroke();

        contexto.y -= 16;
    }

    private List<String> dividirLineas(
            String texto,
            PDFont fuente,
            float tamano,
            float anchoMaximo
    ) throws IOException {

        List<String> resultado =
                new ArrayList<>();

        String[] parrafos =
                texto.split("\\R", -1);

        for (String parrafo : parrafos) {

            if (parrafo.isBlank()) {

                resultado.add("");

                continue;
            }

            String[] palabras =
                    parrafo.trim().split("\\s+");

            StringBuilder lineaActual =
                    new StringBuilder();

            for (String palabra : palabras) {

                String lineaPropuesta;

                if (lineaActual.length() == 0) {
                    lineaPropuesta = palabra;
                } else {
                    lineaPropuesta =
                            lineaActual + " " + palabra;
                }

                float ancho =
                        fuente.getStringWidth(
                                lineaPropuesta
                        ) / 1000f * tamano;

                if (ancho <= anchoMaximo) {

                    lineaActual =
                            new StringBuilder(
                                    lineaPropuesta
                            );

                } else {

                    if (lineaActual.length() > 0) {

                        resultado.add(
                                lineaActual.toString()
                        );
                    }

                    lineaActual =
                            new StringBuilder(palabra);
                }
            }

            if (lineaActual.length() > 0) {

                resultado.add(
                        lineaActual.toString()
                );
            }
        }

        return resultado;
    }

    private String obtenerExtras(
            Cotizacion cotizacion
    ) {

        List<String> extras =
                new ArrayList<>();

        if (leerBooleano(
                cotizacion,
                "sonido"
        )) {
            extras.add("Sonido profesional");
        }

        if (leerBooleano(
                cotizacion,
                "luces"
        )) {
            extras.add("Iluminacion");
        }

        if (leerBooleano(
                cotizacion,
                "pantallaLed"
        )) {
            extras.add("Pantalla LED");
        }

        if (leerBooleano(
                cotizacion,
                "maestroCeremonia"
        )) {
            extras.add("Maestro de ceremonia");
        }

        if (leerBooleano(
                cotizacion,
                "transporte"
        )) {
            extras.add("Transporte");
        }

        if (extras.isEmpty()) {

            return "No se contrataron servicios adicionales.";
        }

        return String.join(", ", extras) + ".";
    }

    private boolean leerBooleano(
            Cotizacion cotizacion,
            String propiedad
    ) {

        BeanWrapper contenedor =
                PropertyAccessorFactory
                        .forBeanPropertyAccess(
                                cotizacion
                        );

        if (!contenedor.isReadableProperty(
                propiedad
        )) {
            return false;
        }

        Object valor =
                contenedor.getPropertyValue(
                        propiedad
                );

        return Boolean.TRUE.equals(valor);
    }

    private String generarCodigo(Long id) {

        return String.format(
                "COT-%05d",
                id
        );
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

    private String valorTexto(Object valor) {

        if (valor == null) {
            return "No especificado";
        }

        String texto =
                String.valueOf(valor).trim();

        if (texto.isBlank()) {
            return "No especificado";
        }

        return texto;
    }

    private String textoSeguro(String texto) {

        if (texto == null) {
            return "";
        }

        String normalizado =
                texto
                        .replace("–", "-")
                        .replace("—", "-")
                        .replace("“", "\"")
                        .replace("”", "\"")
                        .replace("’", "'")
                        .replace("…", "...");

        StringBuilder resultado =
                new StringBuilder();

        for (char caracter :
                normalizado.toCharArray()) {

            if (caracter == '\n'
                    || caracter == '\r'
                    || caracter == '\t'
                    || caracter >= 32
                    && caracter <= 255) {

                resultado.append(caracter);

            } else {

                resultado.append('?');
            }
        }

        return resultado.toString();
    }

    private static class ContextoPdf {

        private final PDDocument documento;

        private PDPageContentStream contenido;

        private float y;

        private ContextoPdf(
                PDDocument documento
        ) {
            this.documento = documento;
        }

        private void nuevaPagina()
                throws IOException {

            if (contenido != null) {
                contenido.close();
            }

            PDPage pagina =
                    new PDPage(
                            PDRectangle.A4
                    );

            documento.addPage(pagina);

            contenido =
                    new PDPageContentStream(
                            documento,
                            pagina
                    );

            y = PDRectangle.A4.getHeight()
                    - MARGEN;
        }

        private void asegurarEspacio(
                float espacio
        ) throws IOException {

            if (contenido == null
                    || y - espacio < MARGEN) {

                nuevaPagina();
            }
        }

        private void cerrar()
                throws IOException {

            if (contenido != null) {

                contenido.close();

                contenido = null;
            }
        }
    }
}