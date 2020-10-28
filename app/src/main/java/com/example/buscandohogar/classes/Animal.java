package com.example.buscandohogar.classes;

import android.content.ContentValues;

import com.example.buscandohogar.R;

import java.util.ArrayList;
import java.util.List;

public class Animal {

    private String type;
    private String name;
    private int age;
    private String breed;
    private String description;
    private int urlImage;


    //Default constructor
    public Animal(){
    }

    //Full constructor
    public Animal(String type, String name, int age, String breed, String description, int urlImage){
        this.type = type;
        this.name = name;
        this.age = age;
        this.breed = breed;
        this.description = description;
        this.urlImage = urlImage;
    }

    //Method for create random animals
    public List<Animal> createRandomDogs(){
        List<Animal> animales = new ArrayList<>();
//        animales.add(new Animal("Perro", "Walter", 2, "Criollo" , "Un perro muy educado, amado y jugueton" ,R.drawable.rotweillermodelo ));
//        animales.add(new Animal("Perro","Pugstavo", 2,"Criollo" , "Un perro muy educado, amado y jugueton" , R.drawable.perro_modelo ));
//        animales.add(new Animal("Perro","Timoteo", 1, "Criollo" , "Un perro muy educado, amado y jugueton" ,R.drawable.perro_modelo ));
//        animales.add(new Animal("Perro","Kira", 3, "Criollo" , "Un perro muy educado, amado y jugueton" ,R.drawable.rotweillermodelo ));
//        animales.add(new Animal("Perro","Kassandra", 6, "Criollo" , "Un perro muy educado, amado y jugueton" , R.drawable.perro_modelo ));
//        animales.add(new Animal("Perro","Osito", 7, "Criollo" , "Un perro muy educado, amado y jugueton" ,R.drawable.rotweillermodelo ));

        return animales;
    }

    public List<Animal> createRandomCats(){
        List<Animal> animales = new ArrayList<>();
//        animales.add(new Animal("Gato","Camila", 3, "Criollo" , "Un gato jugueton" , R.drawable.cat_modelo ));
//        animales.add(new Animal("Gato","Juancho", 4, "Criollo" , "Un gato jugueton" , R.drawable.cat_modelo ));
//        animales.add(new Animal("Gato","Stiven", 9, "Criollo" , "Un gato jugueton" ,R.drawable.cat_modelo ));
//        animales.add(new Animal("Gato","Otto", 6, "Criollo" , "Un gato jugueton" , R.drawable.cat_modelo ));
//        animales.add(new Animal("Gato","Bella", 1, "Criollo" , "Un gato jugueton" ,R.drawable.cat_modelo ));
//        animales.add(new Animal("Gato","Lucas", 8, "Criollo" , "Un gato jugueton" , R.drawable.cat_modelo ));

        return animales;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(int urlImage) {
        this.urlImage = urlImage;
    }

    public int getType() {
        return urlImage;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ContentValues getContentValues(Animal animal){
        ContentValues values = new ContentValues();
        values.put("name", animal.getName());
        values.put("age", animal.getAge());
        values.put("breed", animal.getBreed());
        values.put("description", animal.getDescription());
        values.put("type", animal.getType());

        return values;
    }


}
