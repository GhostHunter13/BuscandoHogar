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

public class SolicitudesFragment extends Fragment {

    private static final String CHANNEL_ID = "BuscandoUnHogarCanalNotificaciones";
    private View v;
    private Context context;
    private RecyclerView rvSolicitudes;
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
        solicitudRepositorio = new SolicitudRepositorio(context);
        listSolicitudes = new ArrayList<>();
        linearNoSolicitudes = v.findViewById(R.id.linearNoSolicitudes);
        ivNoSolicitudes = v.findViewById(R.id.ivNoSolicitudes);

        solicitudRepositorio.obtenerSolicitudesPorDueño(new AppCallback<ArrayList<Solicitud>>() {
            @Override
            public void correcto(ArrayList<Solicitud> respuesta) {

                adapterSolicitud = new SolicitudAdapter(respuesta, context);

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
                Toast.makeText(getContext(), "Se ha producido un error al traer mis mascotas", Toast.LENGTH_SHORT).show();
            }
        });

        solicitudRepositorio.recibirSolicitudes(new AppCallback<ArrayList<Solicitud>>() {
            @Override
            public void correcto(ArrayList<Solicitud> respuesta) {
                enviarNotificacionPush();
            }

            @Override
            public void error(Exception exception) {
                Toast.makeText(context, "Ha ocurrido un error al actualizar la lista de solicitudes "+ exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void actualizarLista(ArrayList<Solicitud> listaSolicitudes){
        adapterSolicitud.setListaSolicitudes(listaSolicitudes);
    }

    public void enviarNotificacionPush(){
        Intent intent = new Intent(getContext(), SolicitudesFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_new_push_notificacion)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notificacion_mensaje))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

}