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

    public MascotaRepositorios(Context context){
        this.contexto = context;
        this.mFirestore = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.listadoMascotas = new ArrayList<>();
        this.mStorage = FirebaseStorage.getInstance();
        this.mReference = mStorage.getReference();
        this.usuarioRepositorios = new UsuarioRepositorios(context);
    }

    /**
     * Metodo encargado de Obtener todas las mascotas registradas en la BD.
     * @param response
     */
    public void obtenerMascotas(final AppCallback<ArrayList<Animal>> response) {
        mFirestore.collection(MASCOTA_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if( task.isSuccessful() ){
                    listadoMascotas.clear();
                    for(DocumentSnapshot item : task.getResult().getDocuments()){
                        Animal mascota = item.toObject(Animal.class);
                        mascota.setId(item.getId());
                        listadoMascotas.add(mascota);
                    }
                    response.correcto(listadoMascotas);
                } else {
                    response.error(task.getException());
                }
            }
        });
    }

    /**
     * Metodo encargado de obtener una lista de mascotas asociadas al dueño
     * No recibe parametros ya que el ID del dueño, se saca de la instancia de Firebase Authentication.
     * @param response
     */
    public void obtenerMascotasPorDueño(final AppCallback<ArrayList<Animal>> response){
        mFirestore.collection(MASCOTA_COLLECTION).whereEqualTo("idDueño", mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if( task.isSuccessful() ){
                    Log.d(TAG, "onComplete: traigo "+ task.getResult().getDocuments());
                    ArrayList<Animal> mascotas = new ArrayList<>();
                    for(DocumentSnapshot item : task.getResult().getDocuments()){
                        Animal mascota = item.toObject(Animal.class);
                        Log.d(TAG, "onComplete: Mascota: "+ mascota.toString(mascota));
                        mascotas.add(mascota);
                    }
                    response.correcto(mascotas);
                }
            }
        });
    }

    /**
     * Metodo encargado de obtener una sola mascota, teniendo en cuenta el ID de la mascota.
     * @param idMascota
     * @param response
     */
    public void obtenerMascotaById(String idMascota, final AppCallback<Animal> response){
        mFirestore.collection(MASCOTA_COLLECTION).document(idMascota).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if( task.isSuccessful() ){
                    Animal animal = task.getResult().toObject(Animal.class);
                    animal.setId(task.getResult().getId());
                    response.correcto(animal);
                } else {
                    response.error(task.getException());
                }
            }
        });
    }

    /**
     * Metodo encargado de guardar el objeto Mascota en la base de datos
     * @param mascota
     * @param response
     */
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

    /**
     * Metodo encargado de subir la imagen de la mascota.
     * Devuelve la URL de la imagen para posteriormente ser seteada en el objeto a guardar.
     * @param imagen
     * @param uriUsuarioProfile
     * @param response
     */
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

    /**
     * Metodo encarga de eliminar la mascota de la BD, teniendo como referencia el ID.
     * Posteriormente se procede a eliminar la imagen de la mascota teniendo como referencia
     * la URL de la imagen, y haciendo uso del metodo delete.
     * @param idMascota
     * @param urlImagen
     * @param response
     */
    public void eliminarMascota(String idMascota, String urlImagen, final AppCallback<Boolean> response){
        mFirestore.collection(MASCOTA_COLLECTION).document(idMascota).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if( task.isSuccessful() ){
                    StorageReference referenciaImagen = mStorage.getReferenceFromUrl(urlImagen);
                    referenciaImagen.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if( task.isSuccessful() ){
                                response.correcto(true);
                            } else{
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

    /**
     * Metodo encargado de editar la informacion de una mascota en la BD.
     * @param mascota
     */
    public void editarMascota(Animal mascota, final AppCallback<Boolean> response){
        mFirestore.collection(MASCOTA_COLLECTION).document(mascota.getId()).set(mascota).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if( task.isSuccessful() ){
                    response.correcto(true);
                } else {
                    response.error(task.getException());
                }
            }
        });
    }

}
