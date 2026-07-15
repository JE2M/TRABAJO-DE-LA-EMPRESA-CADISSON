package com.cadisson.cadisson.Repositorios;

import com.cadisson.cadisson.models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepositorio
        extends JpaRepository<Evento, Long> {

    boolean existsByCotizacion_Id(
            Long cotizacionId
    );

    Optional<Evento> findByCotizacion_Id(
            Long cotizacionId
    );

    List<Evento> findAllByOrderByFechaAscHoraAsc();

    List<Evento> findByFechaBetweenOrderByFechaAscHoraAsc(
            LocalDate fechaInicial,
            LocalDate fechaFinal
    );

    List<Evento>
    findByServicioAndFechaBetweenAndEstadoNotOrderByFechaAscHoraAsc(
            String servicio,
            LocalDate fechaInicial,
            LocalDate fechaFinal,
            String estadoExcluido
    );

    long countByEstadoIgnoreCase(String estado);

    @Query("""
            SELECT COALESCE(SUM(e.precio), 0)
            FROM Evento e
            WHERE e.estado <> :estadoExcluido
            """)
    Double sumarPrecioExceptoEstado(
            @Param("estadoExcluido")
            String estadoExcluido
    );
}