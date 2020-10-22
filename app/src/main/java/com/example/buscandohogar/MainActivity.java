package com.example.buscandohogar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.buscandohogar.Database.DBHelper;
import com.example.buscandohogar.classes.Animal;
import com.example.buscandohogar.classes.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private ImageView imageViewLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDatos();
    }

    private void setDatos() {

        btnLogin = findViewById(R.id.btnLogin);
        imageViewLogo = findViewById(R.id.imgcircular);
        DBHelper db = DBHelper.getInstance(this);

        SQLiteDatabase sqdb = db.getWritableDatabase();
        User u = new User("Joaco", "123", "Guillen", "prueba@gmaill.com", "Calle 12 # 34 - 45", 321, "Barranquilla");
        ContentValues cValuesUser = u.getContentValues(u);

        sqdb.insert("usuarios", null, cValuesUser);
        sqdb.close();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goPrincipal = new Intent(MainActivity.this, PrincipalActivity.class);
                startActivity(goPrincipal);
            }
        });
    }
}