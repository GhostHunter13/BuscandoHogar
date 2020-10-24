package com.example.buscandohogar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class ConocemeActivity extends AppCompatActivity {

    CarouselView carouselView;
    public int[] animalImages = {R.drawable.aboutus, R.drawable.add, R.drawable.arroba, R.drawable.buscar };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conoceme);
        setDatos();
    }

    private void setDatos() {
        carouselView = findViewById(R.id.idImagesCarousel);
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