package com.cadisson.cadisson.Servicio;

import com.cadisson.cadisson.DTO.CambioPasswordDTO;
import com.cadisson.cadisson.Repositorios.UsuarioRepositorio;
import com.cadisson.cadisson.models.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CuentaServiceImpl implements CuentaService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    public CuentaServiceImpl(
            UsuarioRepositorio usuarioRepositorio,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void cambiarPassword(
            String correo,
            CambioPasswordDTO cambioPasswordDTO
    ) {

        Usuario usuario = usuarioRepositorio
                .findByCorreoIgnoreCase(correo)
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "No se encontró el usuario autenticado"
                        )
                );

        boolean passwordCorrecto =
                passwordEncoder.matches(
                        cambioPasswordDTO.getPasswordActual(),
                        usuario.getPassword()
                );

        if (!passwordCorrecto) {
            throw new IllegalArgumentException(
                    "La contraseña actual es incorrecta"
            );
        }

        if (!cambioPasswordDTO
                .getNuevaPassword()
                .equals(
                        cambioPasswordDTO.getConfirmarPassword()
                )) {

            throw new IllegalArgumentException(
                    "La nueva contraseña y su confirmación no coinciden"
            );
        }

        boolean mismaPassword =
                passwordEncoder.matches(
                        cambioPasswordDTO.getNuevaPassword(),
                        usuario.getPassword()
                );

        if (mismaPassword) {
            throw new IllegalArgumentException(
                    "La nueva contraseña debe ser diferente de la contraseña actual"
            );
        }

        usuario.setPassword(
                passwordEncoder.encode(
                        cambioPasswordDTO.getNuevaPassword()
                )
        );

        usuarioRepositorio.save(usuario);
    }
}