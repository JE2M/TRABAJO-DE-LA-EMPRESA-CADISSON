package com.cadisson.cadisson.Controlador;

import com.cadisson.cadisson.Servicio.CotizacionPdfService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cotizaciones")
public class CotizacionPdfController {

    private final CotizacionPdfService cotizacionPdfService;

    public CotizacionPdfController(
            CotizacionPdfService cotizacionPdfService
    ) {
        this.cotizacionPdfService =
                cotizacionPdfService;
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarPdf(
            @PathVariable Long id
    ) {

        byte[] contenidoPdf =
                cotizacionPdfService
                        .generarPdf(id);

        String nombreArchivo =
                String.format(
                        "cotizacion-COT-%05d.pdf",
                        id
                );

        ContentDisposition disposicion =
                ContentDisposition
                        .attachment()
                        .filename(nombreArchivo)
                        .build();

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        disposicion.toString()
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .contentLength(
                        contenidoPdf.length
                )
                .body(contenidoPdf);
    }
}