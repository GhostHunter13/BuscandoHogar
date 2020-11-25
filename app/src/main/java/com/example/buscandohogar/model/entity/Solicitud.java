package com.example.buscandohogar.model.entity;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Solicitud implements Serializable {

    @DocumentId
    private String id;
    private String idUsuarioSolicitante;
    private String idUsuarioPropietario;
    private String idAnimal;
    private String estado;

    public Solicitud() {
    }

    public Solicitud(String idUsuarioSolicitante, String idUsuarioPropietario, String idAnimal, String estado) {
        this.idUsuarioSolicitante = idUsuarioSolicitante;
        this.idUsuarioPropietario = idUsuarioPropietario;
        this.idAnimal = idAnimal;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuarioSolicitante() {
        return idUsuarioSolicitante;
    }

    public void setIdUsuarioSolicitante(String idUsuarioSolicitante) {
        this.idUsuarioSolicitante = idUsuarioSolicitante;
    }

    public String getIdUsuarioPropietario() {
        return idUsuarioPropietario;
    }

    public void setIdUsuarioPropietario(String idUsuarioPropietario) {
        this.idUsuarioPropietario = idUsuarioPropietario;
    }

    public String getIdAnimal() {
        return idAnimal;
    }

    public void setIdAnimal(String idAnimal) {
        this.idAnimal = idAnimal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String toString(Solicitud solicitud){
        return "IdSolicitud: "+ solicitud.getId() + " idUsuarioSolicitante: " + solicitud.getIdUsuarioSolicitante() +
                " idUsuarioPropietario: " + solicitud.getIdUsuarioPropietario() + " idAnimal: "+ solicitud.getIdAnimal() + " estado "+ solicitud.getEstado();
    }
}
