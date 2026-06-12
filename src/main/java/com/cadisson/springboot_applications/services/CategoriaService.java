package com.cadisson.springboot_applications.services;

import com.cadisson.springboot_applications.models.Categoria;
import com.cadisson.springboot_applications.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public List<Categoria> listarTodas() {
        return repository.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Categoria guardar(Categoria categoria) {
        return repository.save(categoria);
    }

    public Categoria actualizar(Long id, Categoria datos) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada con id: " + id));
        categoria.setNombre(datos.getNombre());
        categoria.setDescripcion(datos.getDescripcion());
        return repository.save(categoria);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}