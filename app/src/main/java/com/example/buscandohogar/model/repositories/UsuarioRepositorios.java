package com.example.buscandohogar.model.repositories;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.model.entity.User;
import com.example.buscandohogar.model.network.LocacionAPI;
import com.example.buscandohogar.model.network.LocacionService;
import com.example.buscandohogar.view.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UsuarioRepositorios {

    private static final String TAG = "UsuarioRepositorio";

    private static final String USERS_COLLECTION = "users";
    private static final String DEPARTAMENTO_ID = "c_digo_dane_del_departamento";
    private static final String DEPARTAMENTO = "departamento";
    private static final String MUNICIPIO_ID = "c_digo_dane_del_municipio";
    private static final String MUNICIPIO = "municipio";
    private Context contexto;
    private FirebaseFirestore mFirestore;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    private StorageReference mReference;
    private LocacionAPI locacionAPI;

    public UsuarioRepositorios(Context context){
        this.contexto = context;
        this.mFirestore = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.mStorage = FirebaseStorage.getInstance();
        this.mReference = mStorage.getReference();
        Retrofit retrofit = LocacionService.getInstance();

        this.locacionAPI = retrofit.create(LocacionAPI.class);
    }

    public void obtenerById(String idUser, final AppCallback<User> response){
        mFirestore.collection(USERS_COLLECTION).document(idUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if( task.isSuccessful() ){
                    Log.d(TAG, "onComplete: resultado "+ task.getResult());
                    if( task.getResult() != null ){
                        User user = task.getResult().toObject(User.class);
                        Log.d(TAG, "onComplete: usuario"+ user);
                        user.setId(idUser);
                        response.correcto(user);
                    }
                } else {
                    response.error(task.getException());
                }
            }
        });
    }

    public void iniciarSesion(String user, String pass, final AppCallback<Boolean> response){
        mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    response.correcto(true);
                }else {
                    response.error(task.getException());
                }
            }
        });
    }

    public void crearUsuario(final User user, String contraseña, final AppCallback<Boolean> response){
        mAuth.createUserWithEmailAndPassword(user.getEmail(), contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if( task.isSuccessful() ){
                    mFirestore.collection(USERS_COLLECTION).document(mAuth.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if( task.isSuccessful() ){
                                response.correcto(true);
                                mAuth.signOut();
                            }else {
                                response.error(task.getException());
                            }
                        }
                    });
                }else {
                    response.error(task.getException());
                }
            }
        });
    }

    public void editarUsuario(User user, final AppCallback<Boolean> response){
        mFirestore.collection(USERS_COLLECTION).document(user.getId()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if( task.isSuccessful() ){
                    response.correcto(true);
                } else{
                    response.error(task.getException());
                }
            }
        });
    }

    public void subirImagenUsuario(String imagen, Uri uriUsuarioProfile, AppCallback<String> response){
        StorageReference imagenReference = mReference.child("users/"+imagen);
        imagenReference.putFile(uriUsuarioProfile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if( task.isSuccessful() ){
                    task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if( task.isSuccessful() ){
                                String urlImagenProfile = task.getResult().toString();
                                response.correcto(urlImagenProfile);
                            } else {
                                response.error(task.getException());
                            }
                        }
                    });
                } else {
                    response.error(task.getException());
                }
            }
        });
    }

    public void cerrarSesionUsuario(AppCallback<Boolean> response){
        mAuth.signOut();
    }

    /**
     * Metodo que hace uso de una API para obtener los departamentos y municipios de Colombia
     * Para mostrar los departamentos se creò el array en el string.xml
     * NO SIRVE :(
     * @param response
     */
    public void obtenerDepartamentos(final AppCallback<String []> response){
        locacionAPI.obtenerCiudades("jEAUOWO4Yg3AQXoRqbTWjRgJJ").enqueue(new Callback<ArrayList<List>>() {
            @Override
            public void onResponse(Call<ArrayList<List>> call, Response<ArrayList<List>> respuesta) {
                String [] departamentos = {};
                Log.d(TAG, "onResponse: "+ respuesta.body().size());
                for( int i=0; i < respuesta.body().size(); i++ ){
                    departamentos[i] = (String) respuesta.body().get(i).get(2);
                }
                response.correcto(departamentos);
            }

            @Override
            public void onFailure(Call<ArrayList<List>> call, Throwable t) {

            }
        });
    }

    public String obtenerUsuarioSesion(){
        if( mAuth.getUid() != null )
            return mAuth.getUid();

        return null;
    }

    public void obtenerObjetoUsuarioEnSesion(final AppCallback<User> response){
        mFirestore.collection(USERS_COLLECTION).document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if( task.isSuccessful() ){
                    Log.d(TAG, "onComplete: resultado "+ task.getResult());
                    if( task.getResult() != null ){
                        User user = task.getResult().toObject(User.class);
                        response.correcto(user);
                    }
                } else {
                    response.error(task.getException());
                }
            }
        });
    }

}
