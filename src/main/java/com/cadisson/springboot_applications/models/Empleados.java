package com.cadisson.springboot_applications.models;

public class Empleados {
    private String nombre, apellido, dirección, puesto;
    private int edad, telefono, id;
    
    public Empleados(String nombre, String apellido, String dirección, String puesto, int edad, int telefono, int id) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dirección = dirección;
        this.puesto = puesto;
        this.edad = edad;
        this.telefono = telefono;
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getDirección() {
        return dirección;
    }
    public void setDirección(String dirección) {
        this.dirección = dirección;
    }
    public String getPuesto() {
        return puesto;
    }
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
    public int getEdad() {
        return edad;
    }
    public void setEdad(int edad) {
        this.edad = edad;
    }
    public int getTelefono() {
        return telefono;
    }
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    
}
