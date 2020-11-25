package com.example.buscandohogar.view.fragments;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
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
import com.example.buscandohogar.model.entity.Animal;
import com.example.buscandohogar.model.entity.Solicitud;
import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.model.repositories.MascotaRepositorios;
import com.example.buscandohogar.model.repositories.SolicitudRepositorio;
import com.example.buscandohogar.view.adapter.MisMascotasAdapter;
import com.example.buscandohogar.view.adapter.SolicitudAdapter;

import java.util.ArrayList;
import java.util.List;

public class SolicitudesFragment extends Fragment implements SolicitudAdapter.OnItemClickListener {


    private static final String VALOR_NEGADO = "N";
    private static final String VALOR_ACEPTAR = "A";
    private static final String VALOR_PROCESO = "P";

    private View v;
    private Context context;
    private RecyclerView rvSolicitudes;
    private SolicitudAdapter.OnItemClickListener onItemClickListener;
    private ArrayList<Solicitud> listSolicitudes;
    private SolicitudAdapter adapterSolicitud;
    private LinearLayoutManager llNoSolicitudes;
    private ImageView ivNoSolicitudes;
    private LinearLayout linearNoSolicitudes;
    private SolicitudRepositorio solicitudRepositorio;

    public SolicitudesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_solicitudes, container, false);
        setDatos();
        return v;
    }

    private void setDatos() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIcon(R.mipmap.ic_launcher_round);
        progressDialog.setMessage("Cargando solicitudes...");
        progressDialog.show();
        context = getContext();
        onItemClickListener = this;
        solicitudRepositorio = new SolicitudRepositorio(context);
        listSolicitudes = new ArrayList<>();
        linearNoSolicitudes = v.findViewById(R.id.linearNoSolicitudes);
        ivNoSolicitudes = v.findViewById(R.id.ivNoSolicitudes);

        solicitudRepositorio.obtenerSolicitudesPorDueño(new AppCallback<ArrayList<Solicitud>>() {
            @Override
            public void correcto(ArrayList<Solicitud> respuesta) {
                adapterSolicitud = new SolicitudAdapter(respuesta, context);
                adapterSolicitud.setOnItemClickListener(onItemClickListener);

                rvSolicitudes = v.findViewById(R.id.rvSolicitudes);
                rvSolicitudes.setHasFixedSize(true);

                llNoSolicitudes = new LinearLayoutManager(getContext());
                rvSolicitudes.setLayoutManager(llNoSolicitudes);
                rvSolicitudes.setAdapter(adapterSolicitud);
                linearNoSolicitudes.setVisibility( respuesta.isEmpty() ? View.VISIBLE : View.GONE );
                progressDialog.dismiss();
            }
            @Override
            public void error(Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Se ha producido un error al traer solicitudes", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void actualizarLista(){
        solicitudRepositorio.obtenerSolicitudesPorDueño(new AppCallback<ArrayList<Solicitud>>() {
            @Override
            public void correcto(ArrayList<Solicitud> respuesta) {
                adapterSolicitud.setListaSolicitudes(respuesta);
            }

            @Override
            public void error(Exception exception) {
                Toast.makeText(context, "Se ha producido un error "+ exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClickNegarSolicitud(Solicitud solicitud, int posicion) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIcon(R.mipmap.ic_launcher_round);
        progressDialog.setMessage("Negando solicitud...");
        progressDialog.show();
        solicitud.setEstado(VALOR_NEGADO);
        solicitudRepositorio.cambiarEstadoSolicitud(solicitud, new AppCallback<Boolean>() {
            @Override
            public void correcto(Boolean respuesta) {
                actualizarLista();
                progressDialog.dismiss();
                Toast.makeText(context, "Se ha negado la solicitud exitosamente.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void error(Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(context, "Ha ocurrido un error "+ exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClickAceptarSolicitud(Solicitud solicitud, int posicion) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIcon(R.mipmap.ic_launcher_round);
        progressDialog.setMessage("Aceptando solicitud...");
        progressDialog.show();
        solicitud.setEstado(VALOR_ACEPTAR);
        solicitudRepositorio.cambiarEstadoSolicitud(solicitud, new AppCallback<Boolean>() {
            @Override
            public void correcto(Boolean respuesta) {
                actualizarLista();
                progressDialog.dismiss();
                Toast.makeText(context, "Se ha aceptado a la solicitud exitosamente. El usuario solicitante podrá ver tu numero de contacto", Toast.LENGTH_LONG).show();
            }

            @Override
            public void error(Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(context, "Ha ocurrido un error "+ exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}