package com.cadisson.cadisson.Repositorios;

import com.cadisson.cadisson.models.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CotizacionRepositorio
        extends JpaRepository<Cotizacion, Long> {

    long countByEstadoIgnoreCase(String estado);

    boolean existsByServicio_Id(Long servicioId);
}