package com.example.buscandohogar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.buscandohogar.Database.DBManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrarDatosActivity extends AppCompatActivity {

    private AutoCompleteTextView txtNombres;
    private AutoCompleteTextView txtApellidos;
    private AutoCompleteTextView txtEmail;
    private AutoCompleteTextView txtContraseña;
    private AutoCompleteTextView txtConfirmarContraseña;
    private Button Continuar;

    //VARIABLES DE LOS DATOS QUE VAMOS A REGISTRAR

    private String nombres = "";
    private String apellidos = "";
    private String email = "";
    private String contraseña = "";
    private String confirmarcontraseña = "";

    FirebaseAuth mAuth;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_datos);

        mAuth = FirebaseAuth.getInstance();




        txtNombres = (AutoCompleteTextView) findViewById(R.id.txtNombres);
        txtApellidos = (AutoCompleteTextView) findViewById(R.id.txtApellidos);
        txtEmail = (AutoCompleteTextView) findViewById(R.id.txtEmail);
        txtContraseña = (AutoCompleteTextView) findViewById(R.id.txtContraseña);
        txtConfirmarContraseña = (AutoCompleteTextView) findViewById(R.id.txtConfirmarContraseña);
        Continuar = (Button) findViewById(R.id.btnContinuar);

        Continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombres = txtNombres.getText().toString();
                apellidos = txtApellidos.getText().toString();
                email = txtEmail.getText().toString();
                contraseña = txtContraseña.getText().toString();
                confirmarcontraseña = txtConfirmarContraseña.getText().toString();


                if (!nombres.isEmpty() && !apellidos.isEmpty() && !email.isEmpty() && !contraseña.isEmpty() && !confirmarcontraseña.isEmpty()) {

                    if (contraseña.length() >= 8){
                        registerUser();

                    }

                    else{
                        Toast.makeText(RegistrarDatosActivity.this, "la contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();

                    }


                }
                    else{
                    Toast.makeText(RegistrarDatosActivity.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void registerUser(){
        mAuth.createUserWithEmailAndPassword( email, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                }
                else {

                    Toast.makeText(RegistrarDatosActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }

            }

        });


    }
}