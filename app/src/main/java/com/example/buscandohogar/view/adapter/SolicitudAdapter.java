package com.example.buscandohogar.view.adapter;

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

import com.example.buscandohogar.R;
import com.example.buscandohogar.model.entity.Animal;

import java.util.List;

public class SolicitudAdapter extends RecyclerView.Adapter<SolicitudAdapter.SolicitudViewHolder> {

    private List<Animal> animals;

    private static final String CHECK_BTN = "BotonFavorite";
    private static final String TAG = "AnimalAdapter";

    public static class SolicitudViewHolder extends RecyclerView.ViewHolder{

        //Cardview correspondiente al animal
        CardView cvSolicitud;
        TextView name, age, breed, description;
        ImageView animalPhoto;
        CheckBox btnFavorite;
        Button btnEliminar;

        public SolicitudViewHolder(@NonNull View itemView) {
            super(itemView);
            cvSolicitud = itemView.findViewById(R.id.cvSolicitud);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);

            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String idText = view.getId()+"";
                    Log.d(TAG, "onClick: "+ idText);
                }
            });
        }
    }

    //Constructor with animals
    public SolicitudAdapter(List<Animal> animals) {
        this.animals = animals;
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

    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    public void updateAnimalsList(List<Animal> newList){
        animals.clear();
        animals.addAll(newList);
        this.notifyDataSetChanged();
    }
}
