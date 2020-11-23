package com.example.buscandohogar.model.network;

public interface AppCallback<T> {

    void correcto(T respuesta);
    void error(Exception exception);

}
