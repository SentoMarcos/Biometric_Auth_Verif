package com.example.smariba_upv.biometric_auth_verif;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

public class MainActivity extends AppCompatActivity implements BiometricCTR.BiometricAuthListener {

    private BiometricCTR biometricCTR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializar BiometricCTR con esta actividad como listener
        biometricCTR = new BiometricCTR(this, this);

        // Inicializar el hardware biométrico y prompt
        biometricCTR.initBiometricPrompt();
        biometricCTR.setupPromptInfo();

        // Botón para iniciar la autenticación biométrica
        Button biometricLoginButton = findViewById(R.id.biometric_login);
        biometricLoginButton.setOnClickListener(view -> biometricCTR.startAuthentication());
    }

    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        // Aquí manejamos la autenticación exitosa
        Toast.makeText(this, "Autenticación biométrica exitosa", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationFailed() {
        // Aquí manejamos la autenticación fallida
        Toast.makeText(this, "Fallo en la autenticación biométrica", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        // Aquí manejamos cualquier error en la autenticación
        Toast.makeText(this, "Error de autenticación: " + errString, Toast.LENGTH_SHORT).show();
    }
}
