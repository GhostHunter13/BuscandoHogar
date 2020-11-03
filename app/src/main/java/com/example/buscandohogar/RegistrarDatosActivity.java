package com.example.buscandohogar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.buscandohogar.Utils.Utilidades;
import com.example.buscandohogar.classes.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class RegistrarDatosActivity extends AppCompatActivity {

    private static String TAG = "RegistroDatos";
    private TextInputLayout txtNombres, txtApellidos, txtTelefono, txtCiudad,
            txtDireccion, txtEmail, txtContraseña, txtConfirmarContraseña;
    private Button btnContinuar, btnAtras;

    private String nombres, apellidos, ciudad, direccion, email, contraseña, confirmarContraseña;
    private Integer telefono;

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos);
        setDatos();
    }

    private void setDatos() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        txtNombres = findViewById(R.id.txtNombres);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtTelefono = findViewById(R.id.txtPhone);
        txtCiudad = findViewById(R.id.txtCiudad);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtEmail = findViewById(R.id.txtEmail);
        txtContraseña = findViewById(R.id.txtContraseña);
        txtConfirmarContraseña = findViewById(R.id.txtConfirmarContraseña);
        btnContinuar = (Button) findViewById(R.id.btnContinuar);
        btnAtras = findViewById(R.id.btnAtras);

        txtNombres.addOnEditTextAttachedListener(new TextInputLayout.OnEditTextAttachedListener() {
            @Override
            public void onEditTextAttached() {
                txtNombres.setError(null);
            }
        });

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombres = txtNombres.getEditText().getText().toString();
                apellidos = txtApellidos.getEditText().getText().toString();
                ciudad = txtCiudad.getEditText().getText().toString();
                direccion = txtDireccion.getEditText().getText().toString();
                email = txtEmail.getEditText().getText().toString();
                telefono = txtTelefono.getEditText().getText().toString().isEmpty() ? 0 : 1;
                contraseña = txtContraseña.getEditText().getText().toString();
                confirmarContraseña = txtConfirmarContraseña.getEditText().getText().toString();

                //Se valida el email primero
                if( validarEmail(email) ){
                    CollectionReference usuarios = db.collection("users");
                    Query checkIfExistUser = usuarios.whereEqualTo("email", email);
                    checkIfExistUser.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if( !queryDocumentSnapshots.isEmpty() ){
                                txtEmail.setError(getString(R.string.errorEmailExiste));
                            } else {
                                txtEmail.setError(null);
                                if( validarInfoRegistro() ){
                                    if (contraseña.length() >= 8){
                                        User user = new User();
                                        user.setName(nombres);
                                        user.setLastname(apellidos);
                                        user.setPhone(telefono);
                                        user.setCity(ciudad);
                                        user.setAddress(direccion);
                                        user.setEmail(email);
                                        user.setPassword(encryptPassword(contraseña));
                                        registerUser(user);
                                    }else{
                                        Toast.makeText(RegistrarDatosActivity.this, "la contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrarDatosActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validarEmail(String emailUser){
        boolean validar = true;
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if( emailUser.trim().isEmpty() ||
                !pattern.matcher(emailUser).matches() ){
            txtEmail.setError(getString(R.string.errorEmail));
            validar = false;
        } else {
            txtEmail.setError(null);
        }
        return validar;
    }

    /**
     * Meotod para validar los campos del formulario
     */
    private boolean validarInfoRegistro(){
        boolean valido = true;
        if( nombres.trim().isEmpty() ){
            txtNombres.setError(getString(R.string.errorNombre));
            valido = false;
        } else {
            txtNombres.setError(null);
        }
        if( apellidos.trim().isEmpty() ){
            txtApellidos.setError(getString(R.string.errorApellidos));
            valido = false;
        } else {
            txtApellidos.setError(null);
        }
        if( ciudad.trim().isEmpty() ){
            txtCiudad.setError(getString(R.string.errorCiudad));
            valido = false;
        } else {
            txtCiudad.setError(null);
        }
        if( direccion.trim().isEmpty() ){
            txtDireccion.setError(getString(R.string.errorDireccion));
            valido = false;
        } else{
            txtDireccion.setError(null);
        }
        if( telefono.toString().trim().isEmpty() || telefono <= 0 ){
            txtTelefono.setError(getString(R.string.errorTelefono));
            valido = false;
        } else {
            txtTelefono.setError(null);
        }
        if( contraseña.trim().isEmpty() ){
            txtContraseña.setError(getString(R.string.errorContraseña));
            valido = false;
        } else {
            txtContraseña.setError(null);
        }

        if( confirmarContraseña.toString().trim().isEmpty() ){
            txtConfirmarContraseña.setError(getString(R.string.errorContraseña));
            valido = false;
        } else {
            txtConfirmarContraseña.setError(null);
        }

        if( !contraseña.trim().equals(confirmarContraseña.trim()) ){
            txtConfirmarContraseña.setError(getString(R.string.errorContraseñasNoCoinciden));
            txtContraseña.setError(getString(R.string.errorContraseñasNoCoinciden));
            valido = false;
        } else {
            txtConfirmarContraseña.setError(null);
            txtContraseña.setError(null);
        }

        return valido;
    }


    /**
     * Metodo para registrar usuarios.
     */
    private void registerUser(User user){

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

    private String encryptPassword(String contraseña){
        String passEncrypt = contraseña;
        try{
            passEncrypt = Utilidades.SHA1(contraseña);
        }catch (NoSuchAlgorithmException ex){
            Log.d(TAG, "encryptPassword: "+ ex.getMessage());
        } catch (UnsupportedEncodingException ex){
            Log.d(TAG, "encryptPassword: "+ ex.getMessage());
        }
        return passEncrypt;
    }
}