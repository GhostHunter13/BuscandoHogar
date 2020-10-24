package com.example.buscandohogar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.buscandohogar.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class AdoptameFragment extends Fragment {

    View v;
    CarouselView carouselView;
    public int[] animalImages = {R.drawable.aboutus, R.drawable.add, R.drawable.arroba, R.drawable.buscar };

    public AdoptameFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_adoptame, container, false);
        setDatos();
        return v;
    }

    private void setDatos() {
        carouselView = v.findViewById(R.id.idImagesCarousel);
        carouselView.setPageCount(animalImages.length);
        carouselView.setImageListener(imageListener);

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            //Podemos usar Glide  Picasso para poner las imagenes
            imageView.setImageResource(animalImages[position]);
        }
    };
}