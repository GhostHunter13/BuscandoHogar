package com.example.buscandohogar.view.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buscandohogar.R;
import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.model.repositories.UsuarioRepositorios;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class
MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "BuscandoUnHogarCanalNotificaciones";
    private Button btnLogin, btnRegistrarse, btnFacebookLogin, btnGoogleLogin;
    private ImageView imageViewLogo, imageViewGoogle;
    private TextInputLayout email,contraseña;

    public static final String TAG = "MainActivity";
    public static final int REGISTRAR_USUARIO_CODE = 1;

    //ESTOS SON LOS PRIVATE PARA EL LOGIN CON GOOGLE
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private UsuarioRepositorios usuarioRepositorios;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            Log.d(TAG, "onStart: "+ user.toString());
            Intent intent = new Intent(getApplicationContext(),PrincipalActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDatos();
        createRequest();
        crearCanalNotificaciones();
    }

    private void crearCanalNotificaciones() {
        // Se crea el canal de notificaciones.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.nombre_canal);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn. getClient ( this , gso );
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        Toast.makeText(this, "No se ha podido iniciar sesion con Google. Mensaje de error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                break;
            case REGISTRAR_USUARIO_CODE:
                    if( resultCode == RESULT_OK ){
                        Snackbar.make(MainActivity.this.getCurrentFocus(), "Se ha creado el usuario correctamente", Snackbar.LENGTH_LONG)
                                .show();
//                      Toast.makeText(this, "Se ha creado el usuario correctamente.", Toast.LENGTH_SHORT).show();
                    }

                break;
            default:
                break;
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "User info: " + user.getEmail() + " Nombre: "+ user.getPhoneNumber() + " imagen: "+ user.getPhotoUrl() );
                            Intent intent = new Intent(MainActivity.this,PrincipalActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Hubo un error de autenticacion.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setDatos() {
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.txtEmail);
        btnLogin = findViewById(R.id.btnLogin);
        contraseña = findViewById(R.id.txtContraseña);
        imageViewLogo = findViewById(R.id.imgcircular);
        btnGoogleLogin = findViewById(R.id.google_signIn);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        btnFacebookLogin = findViewById(R.id.facebookLogin);
        email.requestFocus();
        usuarioRepositorios = new UsuarioRepositorios(this);

        //Metodo onClick para iniciar sesion con Facebook.
        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //Metodo onClick para iniciar sesion con Google.
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //Metodo onClick para enviar al form de Registrar Usuario.
        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrarDatosActivity.class);
                startActivityForResult(intent, REGISTRAR_USUARIO_CODE);
            }
        });

        //Metodo onClick para iniciar sesion con Usuario y Contraseña.
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setIcon(R.mipmap.ic_launcher_round);
                progressDialog.setMessage("Por favor espere...");
                final String user = email.getEditText().getText().toString();
                final String pass = contraseña.getEditText().getText().toString();

                if( validarDatos() ){
                    progressDialog.show();
                    usuarioRepositorios.iniciarSesion(user, pass, new AppCallback<Boolean>() {
                        @Override
                        public void correcto(Boolean respuesta) {
                            Toast.makeText(MainActivity.this, "Bienvenido a Buscando un Hogar.", Toast.LENGTH_SHORT).show();
                            Intent goPrincipal = new Intent(MainActivity.this, PrincipalActivity.class);
                            startActivity(goPrincipal);
                        }

                        @Override
                        public void error(Exception exception) {
                            Toast.makeText(MainActivity.this, "Hubo un error al iniciar sesión."+ exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    progressDialog.dismiss();
                }
            }
        });
    }

    public boolean validarDatos(){
        boolean valido = true;

        if( email.getEditText().getText().toString().trim().isEmpty() ){
            email.setError(getString(R.string.colocar_email));
            return false;
        }

        if( contraseña.getEditText().getText().toString().trim().isEmpty() ){
            contraseña.setError(getString(R.string.colocar_contraseña));
            return false;
        }

        return valido;
    }


}