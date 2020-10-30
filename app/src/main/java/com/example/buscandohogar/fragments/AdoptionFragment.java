package com.example.buscandohogar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.buscandohogar.R;

import java.util.ArrayList;

public class AdoptionFragment extends Fragment {

    View v;

    public AdoptionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_adoption, container, false);
        setDatos();
        return v;
    }

    private void setDatos() {


    }
}