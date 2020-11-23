package com.example.buscandohogar.view.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.buscandohogar.R;
import com.example.buscandohogar.model.entity.Animal;
import com.example.buscandohogar.model.network.AppCallback;
import com.example.buscandohogar.model.repositories.MascotaRepositorios;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AdoptionFragment extends Fragment {

    public static final String TAG = "AdoptionFragment";

    public static final int CODIGO_TOMAR_FOTO = 124;
    public static final int CODIGO_CARRETE = 125;

    private View v;
    private MascotaRepositorios mascotaRepositorio;
    private TextInputLayout etNombreMascota, etTipoMascota, etRaza, etEdad, etDescripcionMascota;
    private ImageView ivFoto;
    private AutoCompleteTextView dropdwonTipoMascota;
    private ImageButton ibCamara, ibCarrete;
    private Button btnRegistrarMascota;
    private Uri uriAnimal;

    private String nombre, tipo, raza, descripcion, idDueño;
    private int edad;

    public AdoptionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_adoption, container, false);
        setDatos();
        return v;
    }

    private void setDatos() {
        etNombreMascota = v.findViewById(R.id.etNombreMascota);
        etTipoMascota = v.findViewById(R.id.etTipoMascota);
        etRaza = v.findViewById(R.id.etRaza);
        etEdad = v.findViewById(R.id.etEdad);
        etDescripcionMascota = v.findViewById(R.id.etDescripcion);
        ivFoto = v.findViewById(R.id.ivFoto);
        ibCamara = v.findViewById(R.id.ibCamara);
        ibCarrete = v.findViewById(R.id.ibCarrete);
        dropdwonTipoMascota = v.findViewById(R.id.dropdownTipoMascota);
        mascotaRepositorio = new MascotaRepositorios(getContext());
        btnRegistrarMascota = v.findViewById(R.id.btnRegistrarMascota);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        getContext(),
                        R.layout.dropdown_menu_item,
                        getResources().getStringArray(R.array.typesAnimals));
        dropdwonTipoMascota.setAdapter(adapter);

        //Se crea el evento para probar, hacen falta los otros campos
        etNombreMascota.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean tieneFoco) {
               if( tieneFoco )
                   etNombreMascota.setError(null);
            }
        });



    }

    public void registrarMascota(View v){
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIcon(R.mipmap.ic_launcher_round);
        progressDialog.setMessage("Registrando mascota...");

        nombre = etNombreMascota.getEditText().getText().toString();
        tipo = etTipoMascota.getEditText().getText().toString();
        raza = etRaza.getEditText().getText().toString();
        edad = etEdad.getEditText().getText().toString().isEmpty() ? null : Integer.parseInt(etEdad.getEditText().getText().toString());
        descripcion = etDescripcionMascota.getEditText().getText().toString();

        final Animal mascota = new Animal(nombre, tipo, raza, edad, descripcion, "", "");

        if( validarDatos() ){
            progressDialog.show();
            String ruta = uriAnimal.toString();
            String imagen = ruta.substring(ruta.lastIndexOf("/"));
            mascotaRepositorio.subirImagenMascota(imagen, uriAnimal, new AppCallback<String>() {
                @Override
                public void correcto(String respuesta) {
                    mascota.setUrlImagen(respuesta);
                    mascotaRepositorio.crearMascota(mascota, new AppCallback<String>() {
                        @Override
                        public void correcto(String respuesta) {
                            mascota.setId(respuesta);
                            progressDialog.dismiss();
                            Snackbar.make(getView(), "Se ha guardado la información de la mascota exitosamente.", Snackbar.LENGTH_LONG).show();
                        }
                        @Override
                        public void error(Exception exception) {
                            progressDialog.dismiss();
                            Snackbar.make(getView(), "Se ha producido un error al guardar la info de la mascota.", Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
                @Override
                public void error(Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Se ha producido un error al subir la imagen de la mascota "+ exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressDialog.dismiss();
        }
    }

    private boolean validarDatos() {
        boolean validar = true;

        if( nombre.trim().isEmpty() ){
            etNombreMascota.setError(getString(R.string.errorNombre));
            validar = false;
        } else {
            etNombreMascota.setError(null);
        }

        if( tipo.trim().isEmpty() ){
            etTipoMascota.setError(getString(R.string.errorTipoMascota));
            validar = false;
        } else {
            etTipoMascota.setError(null);
        }

        if( raza.trim().isEmpty() ){
            etRaza.setError(getString(R.string.errorRaza));
            validar = false;
        }else {
            etRaza.setError(null);
        }

        if( etEdad.getEditText().getText().toString().isEmpty() ){
            etEdad.setError(getString(R.string.errorEdad));
            validar = false;
        } else {
            etEdad.setError(null);
        }

        if( descripcion.trim().isEmpty() ){
            etDescripcionMascota.setError(getString(R.string.errorDescripcionMascota));
            validar = false;
        } else {
            etDescripcionMascota.setError(null);
        }

        return validar;
    }

    public void tomarFoto(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if( intent.resolveActivity(getContext().getPackageManager()) != null ){
            File foto = null;
            try{
                foto = crearFoto();
            }catch (IOException e){
                e.printStackTrace();
            }
            if( foto != null ){
                uriAnimal = FileProvider.getUriForFile(getContext(),
                        "com.example.buscandohogar.fileprovider", foto);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriAnimal);
                startActivityForResult(intent, CODIGO_TOMAR_FOTO);
            }

        }

    }

    private File crearFoto() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File foto = File.createTempFile(
                timeStamp,
                ".jpg",
                 getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        );
        return foto;
    }

    public void buscarCarrete(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CODIGO_CARRETE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_OK ){
            switch (requestCode){
                case CODIGO_TOMAR_FOTO:

                    Glide.with(getContext()).load(uriAnimal).into(ivFoto);
                    break;
                case CODIGO_CARRETE:
                    uriAnimal = data.getData();
                    Glide.with(getContext()).load(uriAnimal).into(ivFoto);
                    break;
                default:
                    break;
            }
        }
    }


}