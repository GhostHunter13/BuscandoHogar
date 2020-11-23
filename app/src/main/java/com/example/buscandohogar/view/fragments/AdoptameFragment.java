package com.example.buscandohogar.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.buscandohogar.R;

public class AdoptameFragment extends Fragment {

    View v;
    public int[] animalImages = {R.drawable.aboutus, R.drawable.add, R.drawable.arroba, R.drawable.buscar };

    public AdoptameFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_adoptame, container, false);
        setDatos();
        return v;
    }

    private void setDatos() {

    }
}