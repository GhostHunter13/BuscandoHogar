package com.example.buscandohogar.view.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.buscandohogar.R;
import com.example.buscandohogar.model.entity.User;
import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.model.repositories.MascotaRepositorios;
import com.example.buscandohogar.model.repositories.UsuarioRepositorios;
import com.example.buscandohogar.view.adapter.AnimalAdapter;
import com.example.buscandohogar.model.entity.Animal;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AnimalesFragment extends Fragment {

    private static final String TAB_LAYOUT = "TabLayout";
    private static final int TAB_PERROS = 0;
    private static final int TAB_GATOS = 1;

    private View v;
    private ArrayList<Animal> animalsList;
    private HashMap<User, Animal> listaMascotasAnimales;
    private User dueñoMascota;
    private AnimalAdapter adapterAnimals;
    private RecyclerView rvAnimales;
    private LinearLayoutManager llmAnimales;
    private TabLayout tablLayout;
    private MascotaRepositorios mascotaRepositorios;
    private UsuarioRepositorios usuarioRepositorios;

    public AnimalesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_animales, container, false);
        setDatos();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setDatos(){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIcon(R.mipmap.ic_launcher_round);
        progressDialog.setMessage("Cargando mascotas...");
        progressDialog.show();
        animalsList = new ArrayList<>();
        listaMascotasAnimales = new HashMap<>();
        mascotaRepositorios = new MascotaRepositorios(getContext());
        tablLayout = v.findViewById(R.id.tabLayout);
        ArrayList<Animal> listaGatos = new ArrayList<>();
        ArrayList<Animal> listaPerros = new ArrayList<>();


        mascotaRepositorios.obtenerMascotas(new AppCallback<ArrayList<Animal>>() {
            @Override
            public void correcto(ArrayList<Animal> respuesta) {
                for( Animal mascota : respuesta ){
                    if( mascota.getTipo().equals(getString(R.string.perro)) ){
                        listaPerros.add(mascota);
                    } else {
                        listaGatos.add(mascota);
                    }
                }
                adapterAnimals = new AnimalAdapter(listaPerros);
                tablLayout = new TabLayout(getContext());

                rvAnimales = v.findViewById(R.id.rvAnimales);
                rvAnimales.setHasFixedSize(true);

                llmAnimales = new LinearLayoutManager(getContext());
                rvAnimales.setLayoutManager(llmAnimales);
                rvAnimales.setAdapter(adapterAnimals);
                progressDialog.dismiss();
            }
            @Override
            public void error(Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Error al traer la lista de mascotas "+ exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        tablLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                progressDialog.show();
                animalsList.clear();
                if( TAB_PERROS == tab.getPosition() ){
                    animalsList.addAll(listaPerros);
                } else if ( TAB_GATOS == tab.getPosition() ){
                    animalsList.addAll(listaGatos);
                }
                actualizarLista(animalsList);
                progressDialog.dismiss();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void actualizarLista(ArrayList<Animal> listaMascotas){
        adapterAnimals.setListaMascotas(listaMascotas);
    }

}