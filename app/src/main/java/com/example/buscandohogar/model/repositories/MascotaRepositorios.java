package com.example.buscandohogar.model.repositories;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.buscandohogar.model.entity.Animal;
import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.model.entity.User;
import com.example.buscandohogar.model.network.LocacionAPI;
import com.example.buscandohogar.model.network.LocacionService;
import com.example.buscandohogar.view.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MascotaRepositorios {

    private static final String TAG = "MascotaRepositorios";

    private static final String MASCOTA_COLLECTION = "mascota";
    private static final String USUARIO_COLLECTION = "users";
    private UsuarioRepositorios usuarioRepositorios;
    private Context contexto;
    private FirebaseFirestore mFirestore;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    private StorageReference mReference;
    private ArrayList<Animal> listadoMascotas;
    private HashMap<User,Animal> listaMascotasDueños;

    public MascotaRepositorios(Context context){
        this.contexto = context;
        this.mFirestore = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.listadoMascotas = new ArrayList<>();
        this.listaMascotasDueños = new HashMap<>();
        this.mStorage = FirebaseStorage.getInstance();
        this.mReference = mStorage.getReference();
        this.usuarioRepositorios = new UsuarioRepositorios(context);
    }

    public void obtenerMascotas(final AppCallback<ArrayList<Animal>> response) {
        mFirestore.collection(MASCOTA_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if( task.isSuccessful() ){
                    listadoMascotas.clear();
                    for(DocumentSnapshot item : task.getResult().getDocuments()){
                        Animal mascota = item.toObject(Animal.class);
                        listadoMascotas.add(mascota);
                    }
                    response.correcto(listadoMascotas);
                } else {
                    response.error(task.getException());
                }
            }
        });
    }

    public void obtenerMascotasMap(final AppCallback<HashMap<User,Animal>> response) {
        mFirestore.collection(MASCOTA_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if( task.isSuccessful() ){
                    listaMascotasDueños.clear();
                    for(DocumentSnapshot item : task.getResult().getDocuments()){
                        Animal mascota = item.toObject(Animal.class);
                        listadoMascotas.add(mascota);
                    }
                    response.correcto(listaMascotasDueños);

                } else {
                    response.error(task.getException());
                }
            }
        });
    }

    public void obtenerDueñosMascotas(ArrayList<Animal> listadoMascotas){
        for( Animal mascota : listadoMascotas ){

        }
    }

    public void obtenerMascotaById(String idUser, final AppCallback<User> response){

        mFirestore.collection(MASCOTA_COLLECTION).document(idUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if( task.isSuccessful() ){
                    User user = task.getResult().toObject(User.class);
                    user.setId(task.getResult().getId());
                    response.correcto(user);
                } else {
                    response.error(task.getException());
                }
            }
        });
    }

    public void crearMascota(final Animal mascota, final AppCallback<String> response){
        mascota.setIdDueño(mAuth.getUid());
        mFirestore.collection(MASCOTA_COLLECTION).add(mascota).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if( task.isSuccessful() ){
                    String id = task.getResult().getId();
                    response.correcto(id);
                } else {
                    response.error(task.getException());
                }
            }
        });
    }

    public void subirImagenMascota(String imagen, Uri uriUsuarioProfile, AppCallback<String> response){
        StorageReference imagenReference = mReference.child("mascotas/"+imagen);
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

}
