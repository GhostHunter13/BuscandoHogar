package com.example.buscandohogar.view.fragments;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.buscandohogar.R;
import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.model.repositories.MascotaRepositorios;
import com.example.buscandohogar.view.activity.MainActivity;
import com.example.buscandohogar.view.adapter.AnimalAdapter;
import com.example.buscandohogar.view.adapter.MisMascotasAdapter;
import com.example.buscandohogar.view.adapter.SolicitudAdapter;
import com.example.buscandohogar.model.entity.Animal;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MisMascotasFragment extends Fragment {

    private View v;
    private RecyclerView rvMisMascotas;
    private List<Animal> listMisMascotas;
    private MisMascotasAdapter adapterMisMascotas;
    private LinearLayoutManager llNoMascotas;
    private ImageView ivNoMisMascotas;
    private LinearLayout llNoMisMascotas;
    private MascotaRepositorios mascotaRepositorios;

    public MisMascotasFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mis_mascotas, container, false);
        setDatos();
        return v;
    }

    private void setDatos() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIcon(R.mipmap.ic_launcher_round);
        progressDialog.setMessage("Cargando mascotas...");
        progressDialog.show();
        listMisMascotas = new ArrayList<>();
        llNoMisMascotas = v.findViewById(R.id.llNoMascotas);
        ivNoMisMascotas = v.findViewById(R.id.ivNoMascotas);
//        rvMisMascotas = v.findViewById(R.id.rvMisMascotas);
        mascotaRepositorios = new MascotaRepositorios(getContext());
        adapterMisMascotas = new MisMascotasAdapter(listMisMascotas);

        mascotaRepositorios.obtenerMascotasPorDueño(new AppCallback<ArrayList<Animal>>() {
            @Override
            public void correcto(ArrayList<Animal> respuesta) {

                adapterMisMascotas = new MisMascotasAdapter(respuesta);

                rvMisMascotas = v.findViewById(R.id.rvMisMascotas);
                rvMisMascotas.setHasFixedSize(true);

                llNoMascotas = new LinearLayoutManager(getContext());
                rvMisMascotas.setLayoutManager(llNoMascotas);
                rvMisMascotas.setAdapter(adapterMisMascotas);
                llNoMisMascotas.setVisibility( respuesta.isEmpty() ? View.VISIBLE : View.GONE );
                progressDialog.dismiss();
            }

            @Override
            public void error(Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Se ha producido un error al traer mis mascotas", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void actualizarLista(ArrayList<Animal> listaMascotas){
            adapterMisMascotas.setListaMisMascotas(listaMascotas);
    }
}