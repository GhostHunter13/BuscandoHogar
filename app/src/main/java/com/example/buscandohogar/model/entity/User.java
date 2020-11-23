package com.example.buscandohogar.model.entity;

import android.content.ContentValues;

import java.io.Serializable;

public class User implements Serializable {

    private String id;
    private String name;
    private String lastname;
    private String email;
    private String address;
    private String phone;
    private String departamento;
    private String municipio;
    private String urlImagen;

    //Default constructor
    public User(){
    }

    //Full constructor
    public User(String name, String lastname, String email, String address, String phone, String departamento, String municipio){
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.departamento = departamento;
        this.municipio = municipio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
