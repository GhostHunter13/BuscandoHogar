package com.example.buscandohogar.model.entity;

import com.example.buscandohogar.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Animal implements Serializable {

    private String nombre;
    private String tipo;
    private String raza;
    private int edad;
    private String descripcion;
    private String urlImagen;
    private String id;
    private String idDueño;
    //Se crean estos campos mientras se busca como traer la info solo con el

    //Default constructor
    public Animal(){
    }

    //Full constructor
    public Animal(String nombre, String tipo, String raza, int edad, String descripcion, String urlImagen, String idDueño) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.raza = raza;
        this.edad = edad;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
        this.idDueño = idDueño;
        this.id = "";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdDueño() {
        return idDueño;
    }

    public void setIdDueño(String idDueño) {
        this.idDueño = idDueño;
    }
}
