package com.example.buscandohogar.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buscandohogar.R;
import com.example.buscandohogar.model.entity.Animal;
import com.example.buscandohogar.model.entity.User;
import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.model.repositories.UsuarioRepositorios;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {

    private static final String CHECK_BTN = "BotonFavorite";
    private static final String TAG = "AnimalAdapter";
    private Animal mascota;
    private ArrayList<Animal> listaMascotas;
    private OnItemClickListener onItemClickListener;
    private UsuarioRepositorios usuarioRepositorios;

    public void setListaMascotas(ArrayList<Animal> listaMascotas){
        this.listaMascotas = listaMascotas;
        notifyDataSetChanged();
    }

    public AnimalAdapter(ArrayList<Animal> listaMascotas, Context context){
        this.listaMascotas = listaMascotas;
        this.usuarioRepositorios = new UsuarioRepositorios(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    class AnimalViewHolder extends RecyclerView.ViewHolder{

        //Cardview correspondiente al animal
        private ImageView ivPerfilAddMascota, ivMascota;
        private TextView tvPerfilAddMascota, tvPerfilAddLocacion, tvMascotaName,
                        tvMascotaRaza, tvMascotaEdad, tvMascotaDescripcion;
        private Button btnConoceme;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPerfilAddMascota = itemView.findViewById(R.id.imgProfileAddAnimal);
            ivMascota = itemView.findViewById(R.id.imgAnimal);
            tvPerfilAddMascota = itemView.findViewById(R.id.txtMainNameText);
            tvPerfilAddLocacion = itemView.findViewById(R.id.txtMainDescNameText);
            tvMascotaName = itemView.findViewById(R.id.txtName);
            tvMascotaEdad = itemView.findViewById(R.id.txtAge);
            tvMascotaRaza = itemView.findViewById(R.id.txtBreed);
            tvMascotaDescripcion = itemView.findViewById(R.id.txtDescription);
            btnConoceme = itemView.findViewById(R.id.btnConoceme);

        }

        public void cargarDatos(final Animal animal){
            usuarioRepositorios.obtenerById(animal.getIdDueño(), new AppCallback<User>() {
                @Override
                public void correcto(User respuesta) {

                    Glide.with(itemView.getContext())
                            .load(animal.getUrlImagen())
                            .into(ivMascota);

                    Glide.with(itemView.getContext())
                            .load(respuesta.getUrlImagen())
                            .into(ivPerfilAddMascota);

                    tvPerfilAddMascota.setText(respuesta.getName());
                    tvPerfilAddLocacion.setText(respuesta.getMunicipio()+ ", "+ respuesta.getDepartamento());

                    tvMascotaName.setText(animal.getNombre());
                    tvMascotaEdad.setText(animal.getEdad()+" año(s)");
                    tvMascotaRaza.setText(animal.getRaza());
                    tvMascotaDescripcion.setText(animal.getDescripcion());



                    if( onItemClickListener != null ){
                        ivPerfilAddMascota.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onItemClickListener.onItemClickImagen(animal, getAdapterPosition());
                            }
                        });

                        btnConoceme.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onItemClickListener.onItemClickConoceme(animal, getAdapterPosition());
                            }
                        });
                    }
                }
                @Override
                public void error(Exception exception) {

                }
            });
        }
    }

    @NonNull
    @Override
    public AnimalAdapter.AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_animal, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalAdapter.AnimalViewHolder holder, int position) {
        Animal animal = listaMascotas.get(position);
        holder.cargarDatos(animal);
    }

    @Override
    public int getItemCount() {
        return listaMascotas.size();
    }

    public interface OnItemClickListener{

        void onItemClickConoceme(Animal mascota, int posicion);
        void onItemClickImagen(Animal mascota, int posicion);

    }

}
