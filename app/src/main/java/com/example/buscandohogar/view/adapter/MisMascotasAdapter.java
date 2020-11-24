package com.example.buscandohogar.view.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.buscandohogar.model.entity.User;
import com.example.buscandohogar.model.repositories.UsuarioRepositorios;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MisMascotasAdapter extends RecyclerView.Adapter<MisMascotasAdapter.MisMascotasViewHolder> {

    private List<Animal> animals;

    private static final String TAG = "MisMascotasAdapter";
    private Animal mascota;
    private ArrayList<Animal> listaMisMascotas;
    private OnItemClickListener onItemClickListener;

    public void setListaMisMascotas(ArrayList<Animal> listaMascotas){
        this.listaMisMascotas = listaMascotas;
        notifyDataSetChanged();
    }

    public MisMascotasAdapter(ArrayList<Animal> listaMascotas){
        this.listaMisMascotas = listaMascotas;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    class MisMascotasViewHolder extends RecyclerView.ViewHolder{

        //Cardview correspondiente al animal
        private ImageView ivMascota;
        private TextView tvMascotaName,
                tvMascotaRaza, tvMascotaEdad, tvMascotaDescripcion;
        private Button btnEditar, btnEliminar;

        public MisMascotasViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMascota = itemView.findViewById(R.id.imgAnimal);
            tvMascotaName = itemView.findViewById(R.id.txtName);
            tvMascotaEdad = itemView.findViewById(R.id.txtAge);
            tvMascotaRaza = itemView.findViewById(R.id.txtBreed);
            tvMascotaDescripcion = itemView.findViewById(R.id.txtDescription);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);

        }

        public void cargarDatos(final Animal animal){
            Glide.with(itemView.getContext())
                    .load(animal.getUrlImagen())
                    .into(ivMascota);

            tvMascotaName.setText(animal.getNombre());
            tvMascotaEdad.setText(animal.getEdad()+" año/s");
            tvMascotaRaza.setText(animal.getRaza());
            tvMascotaDescripcion.setText(animal.getDescripcion());

            if( onItemClickListener != null ){
                btnEditar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClickEditarMascota(animal, getAdapterPosition());
                    }
                });

                btnEliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClickEliminarMascota(animal, getAdapterPosition());
                    }
                });

            }
        }
    }

    //Constructor with animals
    public MisMascotasAdapter(List<Animal> animals) {
        this.animals = animals;
    }

    @NonNull
    @Override
    public MisMascotasAdapter.MisMascotasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_mis_mascotas, parent, false);
        return new MisMascotasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MisMascotasAdapter.MisMascotasViewHolder holder, int position) {
        Animal animal = listaMisMascotas.get(position);
        holder.cargarDatos(animal);
    }

    @Override
    public int getItemCount() {
        return listaMisMascotas.size();
    }

    public interface OnItemClickListener{

        void onItemClickEditarMascota(Animal animal, int posicion);
        void onItemClickEliminarMascota(Animal animal, int posicion);

    }

}
