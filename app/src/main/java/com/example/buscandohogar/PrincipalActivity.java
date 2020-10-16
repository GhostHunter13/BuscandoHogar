package com.example.buscandohogar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buscandohogar.adapters.AnimalAdapter;
import com.example.buscandohogar.classes.Animal;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private static final String TAB_LAYOUT = "TabLayout";
    private static final int TAB_PERROS = 0;
    private static final int TAB_GATOS = 1;

    private List<Animal> animalsList;
    private AnimalAdapter adapterAnimals;
    private RecyclerView rvAnimales;
    private LinearLayoutManager llmAnimales;
    private TabLayout tablLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        setDatos();
    }

    public void setDatos(){
        final Animal an = new Animal();
        animalsList = an.createRandomDogs();

        adapterAnimals = new AnimalAdapter(animalsList);
        tablLayout = new TabLayout(this);

        rvAnimales = findViewById(R.id.rvAnimales);
        rvAnimales.setHasFixedSize(true);

        llmAnimales = new LinearLayoutManager(this);
        rvAnimales.setLayoutManager(llmAnimales);
        rvAnimales.setAdapter(adapterAnimals);

        tablLayout = findViewById(R.id.tabLayout);
        tablLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                List<Animal> newList = new ArrayList<>();
                Log.d(TAB_LAYOUT, "onTabSelected: "+ tab.getPosition());
                animalsList.clear();
                if( TAB_PERROS == tab.getPosition() ){
                    newList.addAll(an.createRandomDogs());
                } else if ( TAB_GATOS == tab.getPosition() ){
                    newList.addAll(an.createRandomCats());
                }
                adapterAnimals.updateAnimalsList(newList);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}