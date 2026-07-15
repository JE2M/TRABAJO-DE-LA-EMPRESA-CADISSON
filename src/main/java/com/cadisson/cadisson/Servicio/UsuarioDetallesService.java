package com.cadisson.cadisson.Servicio;

import com.cadisson.cadisson.Repositorios.UsuarioRepositorio;
import com.cadisson.cadisson.models.Usuario;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetallesService
        implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioDetallesService(
            UsuarioRepositorio usuarioRepositorio
    ) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(
            String correo
    ) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio
                .findByCorreoIgnoreCase(correo)
                .orElseThrow(
                        () -> new UsernameNotFoundException(
                                "No existe un usuario con ese correo"
                        )
                );

        String rol = normalizarRol(
                usuario.getRol()
        );

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getPassword())
                .roles(rol)
                .build();
    }

    private String normalizarRol(String rol) {

        if (rol == null || rol.isBlank()) {
            return "CLIENTE";
        }

        String rolNormalizado = rol
                .trim()
                .toUpperCase();

        if (rolNormalizado.startsWith("ROLE_")) {
            rolNormalizado =
                    rolNormalizado.substring(5);
        }

        return rolNormalizado;
    }
}