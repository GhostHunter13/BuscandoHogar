package com.example.buscandohogar.model.network;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LocacionAPI {

    @GET("xdk5-pm3f.json")
    Call<ArrayList<List>> obtenerCiudades(@Query("$$app_token") String key);

}
