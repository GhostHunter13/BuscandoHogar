package com.example.buscandohogar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buscandohogar.Database.DBManager;
import com.example.buscandohogar.classes.Solicitud;
import com.example.buscandohogar.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class RegistrarDatosActivity extends AppCompatActivity {

    private TextInputLayout txtNombres;
    private TextInputLayout txtApellidos;
    private TextInputLayout txtTelefono;
    private TextInputLayout txtCiudad;
    private TextInputLayout txtDireccion;
    private TextInputLayout txtEmail;
    private TextInputLayout txtContraseña;
    private TextInputLayout txtConfirmarContraseña;
    private Button Continuar;

    //VARIABLES DE LOS DATOS QUE VAMOS A REGISTRAR

    private String nombres = "";
    private String apellidos = "";
    private int telefono = 0;
    private String ciudad = "";
    private String direccion = "";
    private String email = "";
    private String contraseña = "";
    private String confirmarcontraseña = "";

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        txtNombres = findViewById(R.id.txtNombres);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtTelefono = findViewById(R.id.txtTel);
        txtCiudad = findViewById(R.id.txtCiudad);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtEmail = findViewById(R.id.txtEmail);
        txtContraseña = findViewById(R.id.txtContraseña);
        txtConfirmarContraseña = findViewById(R.id.txtConfirmarContraseña);
        Continuar = (Button) findViewById(R.id.btnContinuar);

        Continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombres = txtNombres.getEditText().getText().toString();
                apellidos = txtApellidos.getEditText().getText().toString();
                ciudad = txtCiudad.getEditText().getText().toString();
                direccion = txtDireccion.getEditText().getText().toString();
                email = txtEmail.getEditText().getText().toString();
                telefono = txtTelefono.getEditText().getText().toString().isEmpty() ? 0 : 1;
                contraseña = txtContraseña.getEditText().getText().toString();
                confirmarcontraseña = txtConfirmarContraseña.getEditText().getText().toString();


                if (!nombres.isEmpty() && !apellidos.isEmpty() && telefono > 0 && !ciudad.isEmpty() && !direccion.isEmpty() && !email.isEmpty() && !contraseña.isEmpty() && !confirmarcontraseña.isEmpty()) {

                    if (contraseña.length() >= 8){
                        registerUser();
                    }else{
                        Toast.makeText(RegistrarDatosActivity.this, "la contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                    }
                }
                    else{
                    Toast.makeText(RegistrarDatosActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * Metodo para registrar usuarios.
     */
    private void registerUser(){
        User user = new User();
        user.setName(nombres);
        user.setLastname(apellidos);
        user.setPhone(telefono);
        user.setCity(ciudad);
        user.setAddress(direccion);
        user.setEmail(email);
        user.setPassword(contraseña);

        db.collection("users").add(user)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        Log.d("SUCCESS", "Se ha creado el usuario correctamente.");
                        Toast.makeText(RegistrarDatosActivity.this, "Se ha creaco el usuario correctamente!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrarDatosActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FAILURE", "Ha habido problemas al crear el usuario. Lo sentimos", e);
                    }
                });
    }
}