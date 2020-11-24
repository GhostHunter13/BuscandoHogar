package com.example.buscandohogar.model.repositories;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.buscandohogar.model.entity.Animal;
import com.example.buscandohogar.model.entity.Solicitud;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

public class SolicitudRepositorio {

    private static final String TAG = "SolicitudRepositorio";

    private static final String MASCOTA_COLLECTION = "mascota";
    private static final String USUARIO_COLLECTION = "users";
    private static final String SOLICITUD_COLLECTION = "solicitud";
    private static final String SOLICITUDES_COLLECTION = "solicitudes";
    private UsuarioRepositorios usuarioRepositorios;
    private MascotaRepositorios mascotaRepositorios;
    private Context contexto;
    private FirebaseFirestore mFirestore;
    private FirebaseStorage mStorage;
    private FirebaseAuth mAuth;
    private StorageReference mReference;
    private ArrayList<Solicitud> listadoSolicitud;

    public SolicitudRepositorio(Context context){
        this.contexto = context;
        this.mFirestore = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.listadoSolicitud = new ArrayList<>();
        this.mStorage = FirebaseStorage.getInstance();
        this.mReference = mStorage.getReference();
        this.usuarioRepositorios = new UsuarioRepositorios(context);
        this.mascotaRepositorios = new MascotaRepositorios(context);
    }

    public void obtenerSolicitudesPorDueño(final AppCallback<ArrayList<Solicitud>> response){
        mFirestore.collection(SOLICITUD_COLLECTION).document(mAuth.getUid())
                .collection(SOLICITUDES_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if( task.isSuccessful() ){
                    Log.d(TAG, "onComplete: traigo "+ task.getResult().getDocuments());
                    ArrayList<Solicitud> listaSolicitudes = new ArrayList<>();
                    for(DocumentSnapshot item : task.getResult().getDocuments()){
                        Solicitud solicitud = item.toObject(Solicitud.class);
                        listaSolicitudes.add(solicitud);
                    }
                    response.correcto(listaSolicitudes);
                }
            }
        });
    }

    public void crearSolicitud(final Solicitud solicitud, final AppCallback<Boolean> response){
        mFirestore.collection(SOLICITUD_COLLECTION).document(solicitud.getIdUsuarioPropietario())
                .collection(SOLICITUDES_COLLECTION).add(solicitud).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if( task.isSuccessful() ){
                    String id = task.getResult().getId();
                    Log.d(TAG, "onComplete: ID "+ id);
                    response.correcto(true);
                } else {
                    response.error(task.getException());
                }
            }
        });
    }

    public void recibirSolicitudes(final AppCallback<ArrayList<Solicitud>> response){
        mFirestore.collection(SOLICITUD_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                response.correcto(listadoSolicitud);
            }
        });

//        mFirestore.collection(SOLICITUD_COLLECTION).document(mAuth.getUid())
//                .collection(SOLICITUDES_COLLECTION).addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                listadoSolicitud.clear();
//                for( DocumentSnapshot solicitud : value.getDocuments() ){
//                    Solicitud sol = solicitud.toObject(Solicitud.class);
//                    listadoSolicitud.add(sol);
//                }
//                response.correcto(listadoSolicitud);
//            }
//        });
    }

    public String obtenerUsuarioSesion(){
        if( mAuth.getUid() != null )
            return mAuth.getUid();

        return null;
    }

}
