package com.example.buscandohogar.fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.buscandohogar.R;
import com.example.buscandohogar.adapters.AnimalAdapter;
import com.example.buscandohogar.classes.Animal;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private View v;
    private RecyclerView rvAnimalesFavorites;
    private List<Animal> animalsListFavorites;
    private AnimalAdapter adapterAnimalsFavorites;
    private LinearLayoutManager llmAnimalesFavorites;
    private ImageView ivNoFavorites;

    public FavoritesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_favorites, container, false);
        setDatos();
        return v;
    }

    private void setDatos() {
        final Animal an = new Animal();
        ivNoFavorites = v.findViewById(R.id.ivNoFavorites);
        animalsListFavorites = an.createRandomDogs();
        rvAnimalesFavorites = v.findViewById(R.id.rvAnimalesFavorites);

        adapterAnimalsFavorites = new AnimalAdapter(animalsListFavorites);
        rvAnimalesFavorites.setHasFixedSize(true);

        llmAnimalesFavorites = new LinearLayoutManager(getContext());
        rvAnimalesFavorites.setLayoutManager(llmAnimalesFavorites);
        rvAnimalesFavorites.setAdapter(adapterAnimalsFavorites);

        if( animalsListFavorites.isEmpty() ){
            ivNoFavorites.setVisibility(View.VISIBLE);
        } else {
            ivNoFavorites.setVisibility(View.GONE);
        }

    }
}