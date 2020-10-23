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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.buscandohogar.Database.DBHelper;
import com.example.buscandohogar.Database.DBManager;
import com.example.buscandohogar.classes.Animal;
import com.example.buscandohogar.classes.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private ImageView imageViewLogo;
    private EditText editTextTextPersonName, editTextTextPassword;
    private DBManager dbManager;
    public static final String TAG = "MainActivity";
    public static final String DATA_USER = "DatosUsuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDatos();
    }

    private void setDatos() {
        dbManager = new DBManager(this);
        btnLogin = findViewById(R.id.btnLogin);
        imageViewLogo = findViewById(R.id.imgcircular);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);

//        DBHelper db = DBHelper.getInstance(this);
//
//        SQLiteDatabase sqdb = db.getWritableDatabase();
//        User u = new User("Joaco", "123", "Guillen", "prueba@gmaill.com", "Calle 12 # 34 - 45", 321, "Barranquilla");
//        ContentValues cValuesUser = u.getContentValues(u);
//
//        sqdb.insert("usuarios", null, cValuesUser);
//        sqdb.close();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = editTextTextPersonName.getText().toString();
                String pass = editTextTextPassword.getText().toString();

                if( user.isEmpty() || pass.isEmpty() ){
                    Toast.makeText(MainActivity.this, "Ingrese los datos por favor.", Toast.LENGTH_SHORT).show();
                } else {
                    User userLogin = dbManager.checkUserExist(user,pass);

                    if( userLogin.getEmail() != null ){
                        Toast.makeText(MainActivity.this, "Bienvenido "+ userLogin.getName() + "", Toast.LENGTH_SHORT).show();
                        Intent goPrincipal = new Intent(MainActivity.this, PrincipalActivity.class);
                        startActivity(goPrincipal);
                    } else {
                        Toast.makeText(MainActivity.this, "Los datos de inicio de sesion son incorrectos/no se encuentra registrado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}