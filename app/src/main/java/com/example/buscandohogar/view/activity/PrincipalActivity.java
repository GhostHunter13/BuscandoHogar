package com.example.buscandohogar.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.buscandohogar.R;
import com.example.buscandohogar.model.entity.Animal;
import com.example.buscandohogar.model.entity.Solicitud;
import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.model.repositories.SolicitudRepositorio;
import com.example.buscandohogar.view.adapter.AnimalAdapter;
import com.example.buscandohogar.view.fragments.SolicitudesFragment;
import com.example.buscandohogar.view.fragments.AdoptameFragment;
import com.example.buscandohogar.view.fragments.AdoptionFragment;
import com.example.buscandohogar.view.fragments.AnimalesFragment;
import com.example.buscandohogar.view.fragments.MisMascotasFragment;
import com.example.buscandohogar.view.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity {

    private static final String TAG = "PrincipalAnimal";
    private static final String CHANNEL_ID = "BuscandoUnHogarCanalNotificaciones";
    private static final int NOTIFICACION_SOLICITUD_ADOPCION = 325;


    private BottomNavigationView barNavigation;
    private FrameLayout  fragmentPrincipal;
    private SolicitudesFragment aboutusFramgent;
    private AdoptionFragment adoptionFragment;
    private AnimalesFragment animalesFragment;
    private MisMascotasFragment misMascotasFragment;
    private ProfileFragment profileFragment;
    private SolicitudRepositorio solicitudRepositorio;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.buscandoHogarToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.drawable.logo_buscando_hogar_mini);
        solicitudRepositorio = new SolicitudRepositorio(this);
        mFirestore = FirebaseFirestore.getInstance();

        setDatos();
    }

    public void setDatos(){
        barNavigation = findViewById(R.id.barNavigation);
        fragmentPrincipal = findViewById(R.id.fragmentPrincipal);
        aboutusFramgent = new SolicitudesFragment();
        adoptionFragment = new AdoptionFragment();
        animalesFragment = new AnimalesFragment(this);
        misMascotasFragment = new MisMascotasFragment(this);
        profileFragment = new ProfileFragment();

        setFragment(new AnimalesFragment(this));

        barNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "onNavigationItemSelected: "+ item.getItemId());
                switch (item.getItemId()) {
                    case R.id.homeIcon:
                        setFragment(animalesFragment);
                        return true;
                    case R.id.favoriteIcon:
                        setFragment(misMascotasFragment);
                        return true;
                    case R.id.addIcon:
                        setFragment(adoptionFragment);
                        return true;
                    case R.id.profileIcon:
                        setFragment(profileFragment);
                        return true;
                    case R.id.aboutusIcon:
                        setFragment(aboutusFramgent);
                        return true;
                    default:
                        return false;
                }
            }
        });

        solicitudRepositorio.recibirSolicitudes(new AppCallback<Boolean>() {
            @Override
            public void correcto(Boolean respuesta) {
                Log.d(TAG, "llego aquì "+ respuesta );
                enviarNotificacionPush();
            }

            @Override
            public void error(Exception exception) {
                Toast.makeText(PrincipalActivity.this, "Ha ocurrido un error al actualizar la lista de solicitudes "+ exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void enviarNotificacionPush(){
        Intent intent = new Intent(this, SolicitudesFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(PrincipalActivity.this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_new_push_notificacion)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notificacion_mensaje))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Log.d(TAG, "enviarNotificacionPush: entro a la notificacion");
        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        nmc.notify(NOTIFICACION_SOLICITUD_ADOPCION,builder.build());
    }

    public void tomarFoto(View v){
        adoptionFragment.tomarFoto(v);
    }

    public void buscarGaleria(View v){
        adoptionFragment.buscarCarrete(v);
    }

    public void cerrarSesion(View v){
        profileFragment.cerrarSesionUsuario(v);
    }

    public void editarInfoUsuario(View v){
        profileFragment.editarInfoUsuario(v);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPrincipal, fragment);
        fragmentTransaction.commit();
    }

    public void registrarMascota(View v) {
        adoptionFragment.registrarMascota(v);
    }
}