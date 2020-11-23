package com.example.buscandohogar.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.buscandohogar.R;
import com.example.buscandohogar.view.fragments.AboutusFragment;
import com.example.buscandohogar.view.fragments.AdoptameFragment;
import com.example.buscandohogar.view.fragments.AdoptionFragment;
import com.example.buscandohogar.view.fragments.AnimalesFragment;
import com.example.buscandohogar.view.fragments.FavoritesFragment;
import com.example.buscandohogar.view.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PrincipalActivity extends AppCompatActivity {

    private static final String TAG = "PrincipalAnimal";
    private BottomNavigationView barNavigation;
    private FrameLayout  fragmentPrincipal;
    private AboutusFragment aboutusFramgent;
    private AdoptameFragment adoptameFragment;
    private AdoptionFragment adoptionFragment;
    private AnimalesFragment animalesFragment;
    private FavoritesFragment favoritesFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.buscandoHogarToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setLogo(R.drawable.logo_buscando_hogar_mini);
        setDatos();
    }

    public void setDatos(){
        barNavigation = findViewById(R.id.barNavigation);
        fragmentPrincipal = findViewById(R.id.fragmentPrincipal);
        aboutusFramgent = new AboutusFragment();
        adoptameFragment = new AdoptameFragment();
        adoptionFragment = new AdoptionFragment();
        animalesFragment = new AnimalesFragment();
        favoritesFragment = new FavoritesFragment();
        profileFragment = new ProfileFragment();
        setFragment(new AnimalesFragment());

        barNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "onNavigationItemSelected: "+ item.getItemId());
                switch (item.getItemId()) {
                    case R.id.homeIcon:
                        setFragment(animalesFragment);
                        return true;
                    case R.id.favoriteIcon:
                        setFragment(favoritesFragment);
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

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPrincipal, fragment);
        fragmentTransaction.commit();
    }

    public void registrarMascota(View v) {
        adoptionFragment.registrarMascota(v);
    }
}