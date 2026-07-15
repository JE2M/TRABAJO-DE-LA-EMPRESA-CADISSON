package com.cadisson.cadisson.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 300)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer duracion;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String imagen;

    @OneToMany(mappedBy = "servicio")
    private List<Cotizacion> cotizaciones = new ArrayList<>();

    public Servicio() {
    }

    public Servicio(Long id,
                    String nombre,
                    String descripcion,
                    Double precio,
                    Integer duracion,
                    String categoria,
                    String imagen) {

        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracion = duracion;
        this.categoria = categoria;
        this.imagen = imagen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<Cotizacion> getCotizaciones() {
        return cotizaciones;
    }

    public void setCotizaciones(List<Cotizacion> cotizaciones) {
        this.cotizaciones = cotizaciones;
    }
}