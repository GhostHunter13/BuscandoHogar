package com.example.buscandohogar.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.example.buscandohogar.model.entity.Solicitud;
import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.model.repositories.MascotaRepositorios;
import com.example.buscandohogar.model.repositories.SolicitudRepositorio;
import com.example.buscandohogar.view.activity.EditarInformacionActivity;
import com.example.buscandohogar.view.activity.MainActivity;
import com.example.buscandohogar.view.adapter.AnimalAdapter;
import com.example.buscandohogar.view.adapter.MisMascotasAdapter;
import com.example.buscandohogar.view.adapter.SolicitudAdapter;
import com.example.buscandohogar.model.entity.Animal;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MisMascotasFragment extends Fragment implements MisMascotasAdapter.OnItemClickListener {

    private static final String MASCOTA_EDITAR = "mascota";
    private View v;
    private MisMascotasAdapter.OnItemClickListener onItemClickListener;
    private static final String TAG = "MascotasFragment";
    private static final int IR_EDITAR_MASCOTA = 34;
    private Context context;
    private RecyclerView rvMisMascotas;
    private ArrayList<Animal> listMisMascotas;
    private MisMascotasAdapter adapterMisMascotas;
    private LinearLayoutManager llNoMascotas;
    private ImageView ivNoMisMascotas;
    private LinearLayout llNoMisMascotas;
    private MascotaRepositorios mascotaRepositorios;
    private SolicitudRepositorio solicitudRepositorio;

    public MisMascotasFragment(Context context) {
        this.context = context;
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
        onItemClickListener = this;
        mascotaRepositorios = new MascotaRepositorios(getContext());
        solicitudRepositorio = new SolicitudRepositorio(getContext());
        adapterMisMascotas = new MisMascotasAdapter(listMisMascotas, context);

        mascotaRepositorios.obtenerMascotasPorDueño(new AppCallback<ArrayList<Animal>>() {
            @Override
            public void correcto(ArrayList<Animal> respuesta) {
                Log.d(TAG, "correcto: "+ respuesta);
                adapterMisMascotas = new MisMascotasAdapter(respuesta, context);
                adapterMisMascotas.setOnItemClickListener(onItemClickListener);

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

    public void actualizarLista(){
        mascotaRepositorios.obtenerMascotasPorDueño(new AppCallback<ArrayList<Animal>>() {
            @Override
            public void correcto(ArrayList<Animal> respuesta) {
                adapterMisMascotas.setListaMisMascotas(respuesta);
            }

            @Override
            public void error(Exception exception) {
                Log.d("ListaMisMascotas", "error: "+ exception.toString());
            }
        });
    }

    @Override
    public void onItemClickEditarMascota(Animal animal, int posicion) {
        Intent intent = new Intent(getContext(), EditarInformacionActivity.class);
        intent.putExtra(MASCOTA_EDITAR, animal);
        startActivityForResult(intent, IR_EDITAR_MASCOTA);
    }

    @Override
    public void onItemClickEliminarMascota(Animal animal, int posicion) {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIcon(R.mipmap.ic_launcher_round);
        progressDialog.setMessage("Eliminando mascota...");
        progressDialog.show();
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder.setTitle(getString(R.string.confirmar_eliminar))
                .setPositiveButton(R.string.eliminar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        solicitudRepositorio.obtenerSolicitudesMascota(animal.getId(), new AppCallback<Boolean>() {
                            @Override
                            public void correcto(Boolean respuesta) {
                                if( respuesta ){
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "No es posible eliminar la mascota, ya que tiene solicitudes de adopción pendientes por revisar", Toast.LENGTH_SHORT).show();
                                }else {
                                    mascotaRepositorios.eliminarMascota(animal.getId(), animal.getUrlImagen(), new AppCallback<Boolean>() {
                                        @Override
                                        public void correcto(Boolean respuesta) {
                                            actualizarLista();
                                            progressDialog.dismiss();
                                        }

                                        @Override
                                        public void error(Exception exception) {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "Ha ocurrido un error inesperado "+ exception.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void error(Exception exception) {
                                Toast.makeText(context, "Ha ocurrido un error inesperado "+ exception.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressDialog.dismiss();
                    }
                });
        builder.create();
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case IR_EDITAR_MASCOTA:
                if( resultCode == RESULT_OK ){
                    actualizarLista();
                    Toast.makeText(context, getString(R.string.mensaje_edicion_mascota), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}