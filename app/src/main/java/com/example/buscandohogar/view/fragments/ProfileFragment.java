package com.example.buscandohogar.view.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

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
import com.example.buscandohogar.view.activity.MainActivity;
import com.example.buscandohogar.R;
import com.example.buscandohogar.model.repositories.UsuarioRepositorios;
import com.example.buscandohogar.model.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    View v;
    private ImageView ivProfile;
    private TextView txtMainName, txtCity, txtName, txtEmail, txtPhone, txtAddress;
    private Button logout;
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
        if( usuario.getUrlImagen() != "" ){
            Glide.with(getContext())
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


}