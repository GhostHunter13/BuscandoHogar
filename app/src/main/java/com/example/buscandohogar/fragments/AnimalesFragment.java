package com.example.buscandohogar.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.buscandohogar.R;
import com.example.buscandohogar.adapters.AnimalAdapter;
import com.example.buscandohogar.classes.Animal;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AnimalesFragment extends Fragment {

    private static final String TAB_LAYOUT = "TabLayout";
    private static final int TAB_PERROS = 0;
    private static final int TAB_GATOS = 1;

    private View v;
    private List<Animal> animalsList;
    private AnimalAdapter adapterAnimals;
    private RecyclerView rvAnimales;
    private LinearLayoutManager llmAnimales;
    private TabLayout tablLayout;

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
        final Animal an = new Animal();
        animalsList = an.createRandomDogs();

        adapterAnimals = new AnimalAdapter(animalsList);
        tablLayout = new TabLayout(getContext());

        rvAnimales = v.findViewById(R.id.rvAnimales);
        rvAnimales.setHasFixedSize(true);

        llmAnimales = new LinearLayoutManager(getContext());
        rvAnimales.setLayoutManager(llmAnimales);
        rvAnimales.setAdapter(adapterAnimals);

        tablLayout = v.findViewById(R.id.tabLayout);
        tablLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                List<Animal> newList = new ArrayList<>();
                Log.d(TAB_LAYOUT, "onTabSelected: "+ tab.getPosition());
                animalsList.clear();
                if( TAB_PERROS == tab.getPosition() ){
                    newList.addAll(an.createRandomDogs());
                } else if ( TAB_GATOS == tab.getPosition() ){
                    newList.addAll(an.createRandomCats());
                }
                adapterAnimals.updateAnimalsList(newList);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}