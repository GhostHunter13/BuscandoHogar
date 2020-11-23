package com.example.buscandohogar.view.fragments;

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
    private List<Animal> animalsList;
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
        animalsList = new ArrayList<>();
        listaMascotasAnimales = new HashMap<>();
        mascotaRepositorios = new MascotaRepositorios(getContext());
        tablLayout = v.findViewById(R.id.tabLayout);
        ArrayList<Animal> listaGatos = new ArrayList<>();
        ArrayList<Animal> listaPerros = new ArrayList<>();


        mascotaRepositorios.obtenerMascotasMap(new AppCallback<HashMap<User, Animal>>() {
            @Override
            public void correcto(HashMap<User, Animal> respuesta) {
                for( Map.Entry<User, Animal> entry : respuesta.entrySet() ){
                    Animal mascota = entry.getValue();
                    if( mascota.getTipo().equals(getString(R.string.perro)) ){
                        listaPerros.add(mascota);
                    } else {
                        listaGatos.add(mascota);
                    }
                }
                adapterAnimals = new AnimalAdapter(animalsList);
                tablLayout = new TabLayout(getContext());

                rvAnimales = v.findViewById(R.id.rvAnimales);
                rvAnimales.setHasFixedSize(true);

                llmAnimales = new LinearLayoutManager(getContext());
                rvAnimales.setLayoutManager(llmAnimales);
                rvAnimales.setAdapter(adapterAnimals);
            }
            @Override
            public void error(Exception exception) {
                Toast.makeText(getContext(), "Error al traer la lista de mascotas "+ exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        tablLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                List<Animal> newList = new ArrayList<>();
                Log.d(TAB_LAYOUT, "onTabSelected: "+ tab.getPosition());
                animalsList.clear();
                if( TAB_PERROS == tab.getPosition() ){
                    animalsList.addAll(listaPerros);
                } else if ( TAB_GATOS == tab.getPosition() ){
                    animalsList.addAll(listaGatos);
                }
                actualizarLista();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void actualizarLista(){
        mascotaRepositorios.obtenerMascotas(new AppCallback<ArrayList<Animal>>() {
            @Override
            public void correcto(ArrayList<Animal> respuesta) {
                adapterAnimals.setListaMascotas(respuesta);
            }

            @Override
            public void error(Exception exception) {
                Log.d("ListadoProductos", "error: "+ exception.toString());
            }
        });
    }

}