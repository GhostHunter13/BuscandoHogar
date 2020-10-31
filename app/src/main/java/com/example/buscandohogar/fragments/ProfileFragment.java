package com.example.buscandohogar.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.buscandohogar.MainActivity;
import com.example.buscandohogar.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.net.URL;

public class ProfileFragment extends Fragment {

    View v;
    private ImageView ivProfile;
    private TextView txtMainName, txtCity, txtName, txtEmail, txtPhone, txtAddress;
    Button logout;
    public static final String TAG = "BUCKETNAME";
    FirebaseUser user;
    FirebaseAuth mAuth;
    Context context;

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
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        txtMainName = v.findViewById(R.id.txtNameUser);
        txtCity = v.findViewById(R.id.txtCity);
        txtName = v.findViewById(R.id.txtMainDescNameText);
        txtEmail = v.findViewById(R.id.txtMainDescEmailText);
        txtPhone = v.findViewById(R.id.txtMainDescPhoneText);
        txtAddress = v.findViewById(R.id.txtMainDescAddressText);
        logout = v.findViewById(R.id.btnlagout);








        mAuth = FirebaseAuth.getInstance();
        user =  mAuth.getCurrentUser();
        context = getContext();
        ivProfile = v.findViewById(R.id.ivProfile);

        if( user != null ){
            Glide.with(context)
                    .load(user.getPhotoUrl())
                    .into(ivProfile);
            //Seteamos los datos
            txtMainName.setText(user.getDisplayName());
            txtCity.setText(user.getProviderId());
            txtName.setText(user.getDisplayName());
            txtEmail.setText(user.getEmail());
            txtPhone.setText(user.getPhoneNumber());
            txtAddress.setText(user.getUid());
        }


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }


        });



//        storageReference.child("BuscandoUnHogar/Frieza1.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                if( context != null ){
//                    Glide.with(context)
//                            .load(uri)
//                            .into(ivProfile);
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "onFailure: Falló");
//            }
//        });

    }


}