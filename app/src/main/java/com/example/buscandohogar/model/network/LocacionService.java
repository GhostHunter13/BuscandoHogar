package com.example.buscandohogar.model.network;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class LocacionService {

    private static Retrofit instance = null;
    private static String URL = "https://www.datos.gov.co/resource/";
    private static String API_TOKEN = "jEAUOWO4Yg3AQXoRqbTWjRgJJ/";

    public static Retrofit getInstance(){
            Log.d("Departamentos", "getInstance: "+ URL);
        if( instance == null ){
            instance = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance;
    }

}
