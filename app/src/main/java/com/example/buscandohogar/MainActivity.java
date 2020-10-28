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
import android.provider.ContactsContract;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class
MainActivity extends AppCompatActivity {
    private Button btnLogin, btnRegistrarse;
    private ImageView imageViewLogo;
    private TextInputLayout email,contraseña;
    private DBManager dbManager;
    private DocumentReference cR;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference usuarios;
    private CollectionReference animales;
    private CollectionReference solicitudes;

    public static final String TAG = "MainActivity";
    public static final String TAG_QUERY = "Firestore";
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
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        imageViewLogo = findViewById(R.id.imgcircular);
        email = findViewById(R.id.txtEmail);
        contraseña = findViewById(R.id.txtContraseña);
        firebaseFirestore = FirebaseFirestore.getInstance();
        usuarios = firebaseFirestore.collection("users");

        //Se usa para registrar datos.
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrarDatosActivity.class);
                startActivity(intent);
            }
        });

        //Metodo usado para iniciar sesion
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String user = email.getEditText().getText().toString();
                final String pass = contraseña.getEditText().getText().toString();
                final Query checkIfExistUser = usuarios.whereEqualTo("email", user);
                checkIfExistUser.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if( !queryDocumentSnapshots.isEmpty() ){
                            for( DocumentSnapshot document : queryDocumentSnapshots.getDocuments() ){
                                Log.d(TAG, "Hola, hemos encontrato el correo "+ document.get("email") + " contraseña: "+ document.get("password") + " ");
                                if( pass.equals(document.get("password")) ){
                                    Toast.makeText(MainActivity.this, "Hola "+ document.get("name") + ", Bienvenido a Buscando un Hogar.", Toast.LENGTH_SHORT).show();
                                    Intent goPrincipal = new Intent(MainActivity.this, PrincipalActivity.class);
                                    startActivity(goPrincipal);                                    
                                } else {
                                    Toast.makeText(MainActivity.this, "La contraseña es incorrecta, intenta nuevamente.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.d(TAG, "No se encontró el usuario");
                            Toast.makeText(MainActivity.this, "No hemos encontrado un usuario con ese email. Registrate primero.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
    }
}