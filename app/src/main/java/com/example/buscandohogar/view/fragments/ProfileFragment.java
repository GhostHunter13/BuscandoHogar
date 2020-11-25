package com.example.buscandohogar.view.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.view.activity.EditarInformacionActivity;
import com.example.buscandohogar.view.activity.MainActivity;
import com.example.buscandohogar.R;
import com.example.buscandohogar.model.repositories.UsuarioRepositorios;
import com.example.buscandohogar.model.entity.User;
import com.example.buscandohogar.view.activity.RegistrarDatosActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.buscandohogar.view.fragments.AdoptionFragment.TAG;

public class ProfileFragment extends Fragment {

    private static final int IR_EDITAR_USUARIO = 756;
    private static final String USUARIO = "usuario";
    View v;
    private ImageView ivProfile;
    private TextView txtMainName, txtCity, txtName, txtEmail, txtPhone, txtAddress;
    private Button logout;
    private FloatingActionButton btnEditarInfoUsuario;
    private UsuarioRepositorios usuarioRepositorios;
    private FirebaseAuth mAuth;
    private User usuarioSesion;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        setDatos();
        return v;
    }

    private void setDatos() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIcon(R.mipmap.ic_launcher_round);
        progressDialog.setMessage("Por favor espere...");
        progressDialog.show();
        usuarioRepositorios = new UsuarioRepositorios(getContext());
        mAuth = FirebaseAuth.getInstance();
        txtMainName = v.findViewById(R.id.txtNameUser);
        txtCity = v.findViewById(R.id.txtCity);
        txtName = v.findViewById(R.id.txtMainDescNameText);
        txtEmail = v.findViewById(R.id.txtMainDescEmailText);
        txtPhone = v.findViewById(R.id.txtMainDescPhoneText);
        txtAddress = v.findViewById(R.id.txtMainDescAddressText);
        logout = v.findViewById(R.id.btnlagout);
        ivProfile = v.findViewById(R.id.ivProfile);
        btnEditarInfoUsuario = v.findViewById(R.id.btnEditarInfoUsuario);

        usuarioRepositorios.obtenerById(mAuth.getUid(), new AppCallback<User>() {
            @Override
            public void correcto(User respuesta) {
                llenarDatosUsuario(respuesta);
                progressDialog.dismiss();
            }

            @Override
            public void error(Exception exception) {
                Toast.makeText(getContext(), "Ha ocurrido un error al obtener el usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cerrarSesionUsuario(View v){
        mAuth.signOut();
        Log.d("Profile", "cerrarSesionUsuario: Cerrando sesion");
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    public void llenarDatosUsuario(User usuario){
        Log.d("ProfileFragment", "llenarDatosUsuario: ");
        if( usuario.getUrlImagen() != "" ){
            Glide.with(v.getContext())
                    .load(usuario.getUrlImagen())
                    .into(ivProfile);
        }
        //Seteamos los datos
        txtCity.setText(usuario.getMunicipio()+ ", "+usuario.getDepartamento());
        txtMainName.setText(usuario.getName() + " " + usuario.getLastname());
        txtName.setText(usuario.getName() + " "+ usuario.getLastname());
        txtEmail.setText(usuario.getEmail());
        txtPhone.setText(""+usuario.getPhone());
        txtAddress.setText(usuario.getAddress());
    }

    public void editarInfoUsuario(View v){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIcon(R.mipmap.ic_launcher_round);
        progressDialog.setMessage("Por favor espere...");
        progressDialog.show();
        usuarioRepositorios.obtenerObjetoUsuarioEnSesion(new AppCallback<User>() {
            @Override
            public void correcto(User respuesta) {
                Intent intent = new Intent(getContext(), RegistrarDatosActivity.class);
                intent.putExtra(USUARIO, respuesta);
                startActivityForResult(intent, IR_EDITAR_USUARIO);
                progressDialog.dismiss();
            }

            @Override
            public void error(Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Se ha producido un error al traer la informacion del usuario.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case IR_EDITAR_USUARIO:
                if( resultCode == RESULT_OK ){
                    usuarioRepositorios.obtenerObjetoUsuarioEnSesion(new AppCallback<User>() {
                        @Override
                        public void correcto(User respuesta) {
                            llenarDatosUsuario(respuesta);
                            Toast.makeText(getContext(), "Se ha editado la información correctamente.", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void error(Exception exception) {
                            Toast.makeText(getContext(), "Ha ocurrido un error al obtener la informacion del usuario "+ exception.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            default:
                break;
        }
    }
}