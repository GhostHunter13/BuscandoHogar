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
import android.widget.LinearLayout;

import com.example.buscandohogar.R;
import com.example.buscandohogar.adapters.AnimalAdapter;
import com.example.buscandohogar.adapters.SolicitudAdapter;
import com.example.buscandohogar.classes.Animal;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private View v;
    private RecyclerView rvAnimalesFavorites;
    private List<Animal> animalsListFavorites;
    private SolicitudAdapter adapterAnimalsFavorites;
    private LinearLayoutManager llmAnimalesFavorites;
    private ImageView ivNoFavorites;
    private LinearLayout llNoFavorites;

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
        llNoFavorites = v.findViewById(R.id.llNoFavorites);
        ivNoFavorites = v.findViewById(R.id.ivNoFavorites);
        animalsListFavorites = an.createRandomDogs();
        animalsListFavorites.clear();
        rvAnimalesFavorites = v.findViewById(R.id.rvAnimalesFavorites);

        adapterAnimalsFavorites = new SolicitudAdapter(animalsListFavorites);
        rvAnimalesFavorites.setHasFixedSize(true);

        llmAnimalesFavorites = new LinearLayoutManager(getContext());
        rvAnimalesFavorites.setLayoutManager(llmAnimalesFavorites);
        rvAnimalesFavorites.setAdapter(adapterAnimalsFavorites);

        llNoFavorites.setVisibility( animalsListFavorites.isEmpty() ? View.VISIBLE : View.GONE );

    }
}