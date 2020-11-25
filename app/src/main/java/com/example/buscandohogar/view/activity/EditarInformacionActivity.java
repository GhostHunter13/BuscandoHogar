package com.example.buscandohogar.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.buscandohogar.R;
import com.example.buscandohogar.model.entity.Animal;
import com.example.buscandohogar.model.entity.User;
import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.model.repositories.MascotaRepositorios;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.buscandohogar.view.fragments.AdoptionFragment.CODIGO_CARRETE;
import static com.example.buscandohogar.view.fragments.AdoptionFragment.CODIGO_TOMAR_FOTO;

public class EditarInformacionActivity extends AppCompatActivity {

    public static final String MASCOTA_EDITAR = "mascota";
    public static final String TAG = "EditarInformacion";

    private TextInputLayout etNombre, etTipoMascota, etRaza,
            etEdad, etDescripcion;
    private ImageView ivFoto;
    private ImageButton ibCamara, ibCarrete;
    private Button btnAtras, btnModificarMascota;
    private Animal mascotaEditar;
    private Uri uriImagenEditar;
    private MascotaRepositorios mascotaRepositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Se crea la intencion que va a traer la informacion del objeto a editar
        Intent intent = getIntent();
        mascotaEditar = (Animal) intent.getSerializableExtra(MASCOTA_EDITAR);
        setContentView(R.layout.activity_editar_informacion);
        setDatos();
    }

    private void setDatos() {
        etNombre = findViewById(R.id.etNombre);
        etTipoMascota = findViewById(R.id.etTipoMascota);
        etRaza = findViewById(R.id.etRaza);
        etEdad = findViewById(R.id.etEdad);
        etDescripcion = findViewById(R.id.etDescripcion);
        ivFoto = findViewById(R.id.ivFoto);
        ibCamara = findViewById(R.id.ibCamara);
        ibCarrete = findViewById(R.id.ibCarrete);
        mascotaRepositorio = new MascotaRepositorios(this);

        etNombre.getEditText().setText(mascotaEditar.getNombre());
        etTipoMascota.getEditText().setText(mascotaEditar.getTipo());
        etRaza.getEditText().setText(mascotaEditar.getRaza());
        etEdad.getEditText().setText(mascotaEditar.getEdad()+"");
        etDescripcion.getEditText().setText(mascotaEditar.getDescripcion());

        Glide.with(this)
                .load(mascotaEditar.getUrlImagen())
                .into(ivFoto);


    }
    public void cancelarEditar(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void modificarMascota(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher_round);
        progressDialog.setMessage("Modificando mascota...");
        progressDialog.show();

        mascotaEditar.setDescripcion(etDescripcion.getEditText().getText().toString());
        mascotaEditar.setEdad(etEdad.getEditText().getText().toString().isEmpty() ? -1 : Integer.parseInt(etEdad.getEditText().getText().toString()));
        mascotaEditar.setNombre(etNombre.getEditText().getText().toString());
        mascotaEditar.setRaza(etRaza.getEditText().getText().toString());
        mascotaEditar.setTipo(etTipoMascota.getEditText().getText().toString());

        if( validarCampos() ){
            if( uriImagenEditar != null ){
                String ruta = uriImagenEditar.toString();
                String imagen = ruta.substring(ruta.lastIndexOf("/"));
                mascotaRepositorio.subirImagenMascota(imagen, uriImagenEditar, new AppCallback<String>() {
                    @Override
                    public void correcto(String respuesta) {
                        mascotaEditar.setUrlImagen(respuesta);
                        mascotaRepositorio.editarMascota(mascotaEditar, new AppCallback<Boolean>() {
                            @Override
                            public void correcto(Boolean respuesta) {
                                setResult(RESULT_OK);
                                finish();
                                progressDialog.dismiss();
                            }
                            @Override
                            public void error(Exception exception) {
                                Toast.makeText(EditarInformacionActivity.this, "Se ha producido un error "+ exception.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                    @Override
                    public void error(Exception exception) {
                        progressDialog.dismiss();
                    }
                });
            } else {
                mascotaRepositorio.editarMascota(mascotaEditar, new AppCallback<Boolean>() {
                    @Override
                    public void correcto(Boolean respuesta) {
                        setResult(RESULT_OK);
                        finish();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void error(Exception exception) {
                        Toast.makeText(EditarInformacionActivity.this, "Se ha producido un error "+ exception.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        }

    }

    private boolean validarCampos() {
        boolean validar = true;
        if( etNombre.getEditText().toString().trim().isEmpty() ){
            etNombre.setError(getString(R.string.errorNombreMascota));
            validar = false;
        } else {
            etNombre.setError(null);
        }

        if( etTipoMascota.getEditText().toString().trim().isEmpty() ) {
            etTipoMascota.setError(getString(R.string.errorTipoMascota));
            validar = false;
        } else {
            etTipoMascota.setError(null);
        }

        if( etRaza.getEditText().toString().trim().isEmpty() ){
            etRaza.setError(getString(R.string.errorRaza));
            validar = false;
        } else {
            etRaza.setError(null);
        }

        if( etEdad.getEditText().toString().trim().isEmpty() ){
            etEdad.setError(getString(R.string.errorEdad));
            validar = false;
        } else {
            etEdad.setError(null);
        }

        if( etDescripcion.getEditText().toString().trim().isEmpty() ){
            etDescripcion.setError(getString(R.string.errorDescripcionMascota));
            validar = false;
        } else {
            etDescripcion.setError(null);
        }

        return validar;
    }

    public void tomarFoto(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if( intent.resolveActivity(this.getPackageManager()) != null ){
            File foto = null;
            try{
                foto = crearFoto();
            }catch (IOException e){
                e.printStackTrace();
            }
            if( foto != null ){
                    uriImagenEditar = FileProvider.getUriForFile(this,
                            "com.example.buscandohogar.fileprovider", foto);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagenEditar);
                startActivityForResult(intent, CODIGO_TOMAR_FOTO);
            }

        }

    }

    private File crearFoto() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File foto = File.createTempFile(
                timeStamp,
                ".jpg",
                this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        );
        return foto;
    }

    public void buscarGaleria(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CODIGO_CARRETE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_OK ){
            switch (requestCode){
                case CODIGO_TOMAR_FOTO:
                    Glide.with(this).load(uriImagenEditar).into(ivFoto);
                    break;
                case CODIGO_CARRETE:
                    uriImagenEditar = data.getData();
                    Glide.with(this).load(uriImagenEditar).into(ivFoto);
                    break;
                default:
                    break;
            }
        }
    }

}