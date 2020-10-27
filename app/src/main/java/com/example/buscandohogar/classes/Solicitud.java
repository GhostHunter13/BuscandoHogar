package com.example.buscandohogar.classes;

public class Solicitud {

    private int id;
    private int idUsuarioSolicitante;
    private int idUsuarioPropietario;
    private int idAnimal;
    private Character estado;

    public Solicitud() {
    }

    public Solicitud(int id, int idUsuarioSolicitante, int idUsuarioPropietario, int idAnimal, Character estado) {
        this.id = id;
        this.idUsuarioSolicitante = idUsuarioSolicitante;
        this.idUsuarioPropietario = idUsuarioPropietario;
        this.idAnimal = idAnimal;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuarioSolicitante() {
        return idUsuarioSolicitante;
    }

    public void setIdUsuarioSolicitante(int idUsuarioSolicitante) {
        this.idUsuarioSolicitante = idUsuarioSolicitante;
    }

    public int getIdUsuarioPropietario() {
        return idUsuarioPropietario;
    }

    public void setIdUsuarioPropietario(int idUsuarioPropietario) {
        this.idUsuarioPropietario = idUsuarioPropietario;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(int idAnimal) {
        this.idAnimal = idAnimal;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }
}
