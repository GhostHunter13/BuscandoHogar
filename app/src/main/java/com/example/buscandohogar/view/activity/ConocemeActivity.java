package com.example.buscandohogar.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.buscandohogar.R;
import com.example.buscandohogar.model.entity.Animal;
import com.example.buscandohogar.model.entity.Solicitud;
import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.model.repositories.MascotaRepositorios;
import com.example.buscandohogar.model.repositories.SolicitudRepositorio;
import com.example.buscandohogar.model.repositories.UsuarioRepositorios;

public class ConocemeActivity extends AppCompatActivity {

    private static final String INFO_MASCOTA = "mascota";
    private static final String VALOR_ACTIVO = "A";
    private static final String VALOR_INACTIVO = "I";

    private ImageView ivMascotaDetalle;
    private TextView tvNombreMascotaDetalle, tvDescripcionMascotaDetalle, tvRazaMascotaDetalle,
                        tvEdadMascotaDetalle;
    private Button btnAtrasDetalle, btnSolicitarAdopcion;
    private MascotaRepositorios mascotaRepositorios;
    private UsuarioRepositorios usuarioRepositorios;
    private SolicitudRepositorio solicitudRepositorio;
    private Animal mascota;
    private Solicitud solicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conoceme);
        Intent intent = new Intent(getIntent());
        mascota = (Animal) intent.getSerializableExtra(INFO_MASCOTA);
        setDatos();
    }

    private void setDatos() {
        ivMascotaDetalle = findViewById(R.id.ivMascotaDetalle);
        tvNombreMascotaDetalle = findViewById(R.id.tvNombreMascotaDetalle);
        tvDescripcionMascotaDetalle = findViewById(R.id.tvDescripcionMascotaDetalle);
        tvRazaMascotaDetalle = findViewById(R.id.tvRazaMascotaDetalle);
        tvEdadMascotaDetalle = findViewById(R.id.tvEdadMascotaDetalle);
        btnAtrasDetalle = findViewById(R.id.btnAtrasDetalle);
        btnSolicitarAdopcion = findViewById(R.id.btnSolicitarAdopcion);
        mascotaRepositorios = new MascotaRepositorios(this);
        usuarioRepositorios = new UsuarioRepositorios(this);
        solicitudRepositorio = new SolicitudRepositorio(this);

        Glide.with(this)
                .load(mascota.getUrlImagen())
                .into(ivMascotaDetalle);

        tvNombreMascotaDetalle.setText(getString(R.string.hola_soy, mascota.getNombre()));
        tvDescripcionMascotaDetalle.setText(mascota.getDescripcion());
        tvRazaMascotaDetalle.setText(getString(R.string.raza_formulario, mascota.getRaza()));
        tvEdadMascotaDetalle.setText(getString(R.string.edad_formulario, ""+mascota.getEdad()));

    }

    public void irAtras(View v){
        setResult(RESULT_CANCELED);
        finish();
    }

    public void solicitarAdopcion(View v){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher_round);
        progressDialog.setMessage("Cargando mascotas...");
        progressDialog.show();
        solicitud = new Solicitud("", solicitudRepositorio.obtenerUsuarioSesion(), mascota.getIdDueño(), mascota.getId(), VALOR_ACTIVO);
        solicitudRepositorio.crearSolicitud(solicitud, new AppCallback<Boolean>() {
            @Override
            public void correcto(Boolean respuesta) {
                setResult(RESULT_OK);
                progressDialog.dismiss();
                finish();
            }

            @Override
            public void error(Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(ConocemeActivity.this, "Se ha producido un error "+ exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}