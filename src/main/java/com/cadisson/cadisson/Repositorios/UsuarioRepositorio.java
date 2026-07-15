package com.cadisson.cadisson.Repositorios;

import com.cadisson.cadisson.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio
        extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreoIgnoreCase(
            String correo
    );

    boolean existsByCorreoIgnoreCase(
            String correo
    );
}