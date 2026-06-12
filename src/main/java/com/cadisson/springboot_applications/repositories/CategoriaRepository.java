package com.cadisson.springboot_applications.repositories;

import com.cadisson.springboot_applications.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}