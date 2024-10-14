package com.example.smariba_upv.biometric_auth_verif;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class BiometricCTR {

    private final Executor executor;
    private final BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private final Context context;
    private final BiometricAuthListener listener;

    public BiometricCTR(Context context, BiometricAuthListener listener) {
        this.context = context;
        this.executor = ContextCompat.getMainExecutor(context);
        this.listener = listener;

        // Crear BiometricPrompt con el callback incluido
        this.biometricPrompt = new BiometricPrompt((AppCompatActivity) context, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(context, "Error de autenticación: " + errString, Toast.LENGTH_SHORT).show();
                listener.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(context, "¡Autenticación exitosa!", Toast.LENGTH_SHORT).show();
                listener.onAuthenticationSucceeded(result);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(context, "Fallo en la autenticación", Toast.LENGTH_SHORT).show();
                listener.onAuthenticationFailed();
            }
        });
    }

    public void initBiometricPrompt() {
        BiometricManager biometricManager = BiometricManager.from(context);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // El dispositivo soporta autenticación biométrica
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(context, "Este dispositivo no tiene hardware biométrico", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(context, "Hardware biométrico no disponible", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(context, "No hay datos biométricos registrados", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void setupPromptInfo() {
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Inicio de sesión biométrico")
                .setSubtitle("Inicie sesión con huella dactilar o reconocimiento facial")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();
    }

    public void startAuthentication() {
        if (biometricPrompt != null && promptInfo != null) {
            biometricPrompt.authenticate(promptInfo);
        }
    }

    // Interface para gestionar los eventos de autenticación desde otras actividades
    public interface BiometricAuthListener {
        void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result);
        void onAuthenticationFailed();
        void onAuthenticationError(int errorCode, CharSequence errString);
    }
}
