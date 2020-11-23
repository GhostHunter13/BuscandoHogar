package com.example.buscandohogar.view.Utils;

import com.example.buscandohogar.R;
import com.example.buscandohogar.model.network.AppCallback;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class Utilidades {

    private static final String EMAIL_INVALIDO = "ERROR_INVALID_EMAIL";
    private static final String PASSWORD_DEBIL = "ERROR_WEAK_PASSWORD";
    private static final String CREDENCIALES_EN_USO = "ERROR_CREDENTIAL_ALREADY_IN_USE";

    public static void procesarMensajeError(Exception exception, AppCallback<String> response) {
        if( exception instanceof FirebaseAuthInvalidCredentialsException){
            switch ( ((FirebaseAuthInvalidCredentialsException) exception).getErrorCode() ){
                case EMAIL_INVALIDO:
                    response.correcto(EMAIL_INVALIDO);
                    break;
                case PASSWORD_DEBIL:
                    response.correcto(PASSWORD_DEBIL);
                    break;
                default:
                    break;
            }
        } else if( exception instanceof FirebaseAuthUserCollisionException){
            switch (((FirebaseAuthUserCollisionException) exception).getErrorCode()){
                case CREDENCIALES_EN_USO:
                    response.correcto(CREDENCIALES_EN_USO);
                    break;
                default:
                    break;
            }
        }
    }

}
