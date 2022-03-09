 package com.example.firebase_testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

 public class AuthActivity extends AppCompatActivity {
    EditText inputEmail;
    EditText inputPassword;
    Button register;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Bundle b = new Bundle();
        b.putString("message","Integracion de FireBase completada");
        analytics.logEvent("InitScreen",b);
        this.setTitle("Autenticación");
        main();
        
    }

     private void main() {
        controls();
        registrarUsuario();
        loginUsuario();
     }



     private void controls() {
        inputEmail = findViewById(R.id.editTextEmail);
        inputPassword = findViewById(R.id.editTextPassword);
        register = findViewById(R.id.buttonRegister);
        login = findViewById(R.id.buttonLogin);
     }

     private void loginUsuario() {
         login.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (inputEmail.getText().toString().equals("") || inputPassword.getText().toString().equals("")) {
                     Toast.makeText(getApplicationContext(),"Introduce email y contraseña para continuar",Toast.LENGTH_SHORT).show();
                 } else {

                     String e = inputEmail.getText().toString().trim();
                     String p = inputPassword.getText().toString().trim();

                     FirebaseAuth.getInstance().signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()) {
                                 abrirHome(e, homeActivity.ProviderType.BASIC);
                             } else {
                                 String mensaje = "Fallo en el login";
                                 showAlert(mensaje);
                             }
                         }
                     });
                 }
             }
         });
     }



     private void registrarUsuario() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (inputEmail.getText().toString().equals("") || inputPassword.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(),"Introduce email y contraseña para continuar",Toast.LENGTH_SHORT).show();
            } else {

                String e = inputEmail.getText().toString().trim();
                String p = inputPassword.getText().toString().trim();

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        abrirHome(e, homeActivity.ProviderType.BASIC);

                    } else {
                        String mensaje = "Fallo en el registro de usuario";
                        showAlert(mensaje);
                        }
                    }
                });
                }
            }
        });
     }

     private void abrirHome(String email , homeActivity.ProviderType provider) {
         Intent i = new Intent(AuthActivity.this,homeActivity.class);
         i.putExtra("email",email);
         i.putExtra("provider", provider.name());
         //i.putExtra("pass",p);
         startActivity(i);

     }

     private void showAlert(String mensaje) {
         AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
         myAlert.setTitle("FireBase Rubén");
         myAlert.setMessage(mensaje);
         myAlert.setPositiveButton("Volver a intentarlo",null);
         myAlert.show();
     }
 }