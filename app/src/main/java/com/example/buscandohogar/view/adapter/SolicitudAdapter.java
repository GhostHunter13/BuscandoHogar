package com.example.buscandohogar.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buscandohogar.R;
import com.example.buscandohogar.model.entity.Animal;
import com.example.buscandohogar.model.entity.Solicitud;
import com.example.buscandohogar.model.entity.User;
import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.model.repositories.MascotaRepositorios;
import com.example.buscandohogar.model.repositories.UsuarioRepositorios;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SolicitudAdapter extends RecyclerView.Adapter<SolicitudAdapter.SolicitudViewHolder> {


    private static final String TAG = "SolicitudAdapter";
    private Solicitud solicitud;
    private UsuarioRepositorios usuarioRepositorios;
    private MascotaRepositorios mascotaRepositorios;
    private ArrayList<Solicitud> listaSolicitudes;
    private OnItemClickListener onItemClickListener;
    private FirebaseAuth mAuth;

    public void setListaSolicitudes(ArrayList<Solicitud> listaSolicitudes){
        this.listaSolicitudes = listaSolicitudes;
        notifyDataSetChanged();
    }

    public SolicitudAdapter(ArrayList<Solicitud> listaSolicitudes, Context context){
        this.listaSolicitudes = listaSolicitudes;
        this.mascotaRepositorios = new MascotaRepositorios(context);
        this.usuarioRepositorios = new UsuarioRepositorios(context);
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    class SolicitudViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivProfileAddMascota;
        private TextView tvLocacionDueño,tvEstadoSolicitud,
                tvMensajeSolicitud;
        private Button btnNegarSolicitud, btnAceptarSolicitud;

        public SolicitudViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileAddMascota = itemView.findViewById(R.id.ivProfileAddMascota);
            tvLocacionDueño = itemView.findViewById(R.id.tvLocacionDueño);
            tvMensajeSolicitud = itemView.findViewById(R.id.tvMensajeSolicitud);
            btnNegarSolicitud = itemView.findViewById(R.id.btnNegarSolicitud);
            btnAceptarSolicitud = itemView.findViewById(R.id.btnAceptarSolicitud);
            tvEstadoSolicitud = itemView.findViewById(R.id.estadoSolicitud);

        }

        public void cargarDatos(final Solicitud solicitud){
            usuarioRepositorios.obtenerById(solicitud.getIdUsuarioSolicitante(), new AppCallback<User>() {
                @Override
                public void correcto(User user) {
                    mascotaRepositorios.obtenerMascotaById(solicitud.getIdAnimal(), new AppCallback<Animal>() {
                        @Override
                        public void correcto(Animal mascota) {
                            Glide.with(itemView.getContext())
                                    .load(user.getUrlImagen())
                                    .into(ivProfileAddMascota);

                                tvLocacionDueño.setText(user.getMunicipio() + ", " + user.getDepartamento() );
                                tvMensajeSolicitud.setText(itemView.getContext().getString(R.string.mensaje_solicitud, user.getName(), mascota.getNombre()));

                            if( onItemClickListener != null ){
                                btnAceptarSolicitud.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onItemClickListener.onItemClickAceptarSolicitud(solicitud, getAdapterPosition());
                                    }
                                });

                                btnNegarSolicitud.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        onItemClickListener.onItemClickNegarSolicitud(solicitud, getAdapterPosition());
                                    }
                                });

                            }
                        }
                        @Override
                        public void error(Exception exception) {
                            Log.d(TAG, "error: "+ exception.getMessage());
                        }
                    });
                }

                @Override
                public void error(Exception exception) {

                }
            });
        }
    }

    @NonNull
    @Override
    public SolicitudAdapter.SolicitudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_solicitud, parent, false);
        SolicitudViewHolder avh = new SolicitudViewHolder(view);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitudAdapter.SolicitudViewHolder holder, int position) {
        Solicitud solicitud = listaSolicitudes.get(position);
        holder.cargarDatos(solicitud);
    }

    @Override
    public int getItemCount() {
        return listaSolicitudes.size();
    }

    public interface OnItemClickListener{

        void onItemClickNegarSolicitud(Solicitud solicitud, int posicion);
        void onItemClickAceptarSolicitud(Solicitud solicitud, int posicion);

    }
}
