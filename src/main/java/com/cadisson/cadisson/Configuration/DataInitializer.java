package com.cadisson.cadisson.Configuration;

import com.cadisson.cadisson.Repositorios.ServicioRepositorio;
import com.cadisson.cadisson.Repositorios.UsuarioRepositorio;
import com.cadisson.cadisson.models.Servicio;
import com.cadisson.cadisson.models.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ServicioRepositorio servicioRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final String correoAdministrador;
    private final String passwordAdministrador;

    public DataInitializer(
            ServicioRepositorio servicioRepositorio,
            UsuarioRepositorio usuarioRepositorio,
            PasswordEncoder passwordEncoder,
            @Value("${cadisson.admin.correo}")
            String correoAdministrador,
            @Value("${cadisson.admin.password}")
            String passwordAdministrador
    ) {
        this.servicioRepositorio = servicioRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
        this.correoAdministrador = correoAdministrador;
        this.passwordAdministrador = passwordAdministrador;
    }

    @Override
    public void run(String... args) {

        crearServiciosIniciales();
        crearAdministradorInicial();
    }

    private void crearServiciosIniciales() {

        if (servicioRepositorio.count() > 0) {
            return;
        }

        Servicio mariachi = new Servicio();

        mariachi.setNombre("Mariachi Internacional");
        mariachi.setDescripcion(
                "10 músicos profesionales para bodas y eventos."
        );
        mariachi.setPrecio(650.00);
        mariachi.setDuracion(2);
        mariachi.setCategoria("Mariachi");
        mariachi.setImagen("mariachi.jpg");

        Servicio orquesta = new Servicio();

        orquesta.setNombre("Orquesta Tropical");
        orquesta.setDescripcion(
                "Orquesta completa con animación y repertorio variado."
        );
        orquesta.setPrecio(1200.00);
        orquesta.setDuracion(4);
        orquesta.setCategoria("Orquesta");
        orquesta.setImagen("orquesta.jpg");

        Servicio andina = new Servicio();

        andina.setNombre("Agrupación Andina");
        andina.setDescripcion(
                "Música andina para eventos culturales y celebraciones."
        );
        andina.setPrecio(900.00);
        andina.setDuracion(3);
        andina.setCategoria("Andina");
        andina.setImagen("andina.jpg");

        servicioRepositorio.save(mariachi);
        servicioRepositorio.save(orquesta);
        servicioRepositorio.save(andina);
    }

    private void crearAdministradorInicial() {

        boolean administradorExiste =
                usuarioRepositorio.existsByCorreoIgnoreCase(
                        correoAdministrador
                );

        if (administradorExiste) {
            return;
        }

        if (passwordAdministrador == null
                || passwordAdministrador.isBlank()) {

            throw new IllegalStateException(
                    "Debes configurar la variable CADISSON_ADMIN_PASSWORD"
            );
        }

        Usuario administrador = new Usuario();

        administrador.setNombre("Administrador");
        administrador.setApellido("CADISSON");
        administrador.setCorreo(correoAdministrador);

        administrador.setPassword(
                passwordEncoder.encode(
                        passwordAdministrador
                )
        );

        administrador.setRol("ADMIN");

        usuarioRepositorio.save(administrador);

        System.out.println(
                "Administrador inicial creado: "
                        + correoAdministrador
        );
    }
}