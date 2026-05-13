package org.example.model;

import jakarta.persistence.*;

// 1. Le decimos a JPA que esta clase es una tabla de la base de datos
@Entity
@Table(name = "productos")
public class Producto {

    // 2. Le decimos cuál es la Clave Primaria (ID) y que se genere sola (Autoincremental)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 3. Los atributos obligatorios
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    // 4. Constructor vacío obligatorio (JPA e Hibernate lo necesitan para funcionar por debajo)
    public Producto() {
    }

    // Constructor con parámetros
    public Producto(String nombre, String descripcion, double precio, int stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}