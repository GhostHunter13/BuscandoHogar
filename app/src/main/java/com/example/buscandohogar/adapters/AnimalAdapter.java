package com.example.buscandohogar.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buscandohogar.ConocemeActivity;
import com.example.buscandohogar.MainActivity;
import com.example.buscandohogar.PrincipalActivity;
import com.example.buscandohogar.R;
import com.example.buscandohogar.classes.Animal;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {

    private List<Animal> animals;

    private static final String CHECK_BTN = "BotonFavorite";
    private static final String TAG = "AnimalAdapter";

    public static class AnimalViewHolder extends RecyclerView.ViewHolder{

        //Cardview correspondiente al animal
        CardView cvAnimal;
        TextView name, age, breed, description;
        ImageView animalPhoto;
        CheckBox btnFavorite;
        Button btnConoceme;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            cvAnimal = itemView.findViewById(R.id.cvAnimal);
            name = itemView.findViewById(R.id.txtName);
            age = itemView.findViewById(R.id.txtAge);
            breed = itemView.findViewById(R.id.txtBreed);
            description = itemView.findViewById(R.id.txtDescription);
            animalPhoto = itemView.findViewById(R.id.imgAnimal);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnConoceme = itemView.findViewById(R.id.btnConoceme);

            btnConoceme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String idText = view.getId()+"";
                    Log.d(TAG, "onClick: "+ idText);
                }
            });
        }
    }

    //Constructor with animals
    public AnimalAdapter(List<Animal> animals) {
        this.animals = animals;
    }

    @NonNull
    @Override
    public AnimalAdapter.AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_animal, parent, false);
        AnimalViewHolder avh = new AnimalViewHolder(view);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalAdapter.AnimalViewHolder holder, int position) {
        holder.name.setText(animals.get(position).getName());
        holder.age.setText(animals.get(position).getAge()+" años");
        holder.breed.setText(animals.get(position).getBreed());
        holder.description.setText(animals.get(position).getDescription());
        holder.animalPhoto.setImageResource(animals.get(position).getUrlImage());
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
