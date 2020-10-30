package com.example.buscandohogar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.buscandohogar.Database.DBManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
import java.security.Principal;
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

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            Intent intent = new Intent(getApplicationContext(),PrincipalActivity.class);
            startActivity(intent);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDatos();

        mAuth = FirebaseAuth.getInstance();

        createRequest();

        findViewById(R.id.google_signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });


    }

    private void createRequest() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Cree un GoogleSignInClient con las opciones especificadas por gso.
        mGoogleSignInClient = GoogleSignIn. getClient ( this , gso );
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(),PrincipalActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(MainActivity.this, "sorry out failed", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
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

        //Se usa para enviar al activity de registrar datos.
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