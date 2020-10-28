package com.example.buscandohogar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buscandohogar.adapters.AnimalAdapter;
import com.example.buscandohogar.classes.Animal;
import com.example.buscandohogar.fragments.AboutusFragment;
import com.example.buscandohogar.fragments.AdoptionFragment;
import com.example.buscandohogar.fragments.AnimalesFragment;
import com.example.buscandohogar.fragments.FavoritesFragment;
import com.example.buscandohogar.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private static final String TAG = "PrincipalAnimal";
    private BottomNavigationView barNavigation;
    private FrameLayout fragmentPrincipal;

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
        setFragment(new AnimalesFragment());

        barNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "onNavigationItemSelected: "+ item.getItemId());
                switch (item.getItemId()) {
                    case R.id.homeIcon:
                        setFragment(new AnimalesFragment());
                        return true;
                    case R.id.favoriteIcon:
                        setFragment(new FavoritesFragment());
                        return true;
                    case R.id.addIcon:
                        setFragment(new AdoptionFragment());
                        return true;
                    case R.id.profileIcon:
                        setFragment(new ProfileFragment());
                        return true;
                    case R.id.aboutusIcon:
                        setFragment(new AboutusFragment());
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPrincipal, fragment);
        fragmentTransaction.commit();
    }

}