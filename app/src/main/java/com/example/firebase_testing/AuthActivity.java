 package com.example.firebase_testing;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

 public class AuthActivity extends AppCompatActivity {
    EditText inputEmail;
    EditText inputPassword;
    Button register;
    Button loginEmail;
    Button botonLoginGoogle;

    //585709910502-sodpt1knckp54pe2oablbjrt4ot9n9oo.apps.googleusercontent.com
    private GoogleSignInClient googleClient;
    private final int GOOGLE_SIGN_IN_CODE = 100;
    private FirebaseAuth mAuth;

     private GoogleSignInClient mGoogleSignInClient;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
        Bundle b = new Bundle();
        b.putString("message","Integracion de FireBase completada");
        analytics.logEvent("InitScreen",b);
        this.setTitle("Autenticación");

        //Se comprueba en preferencias si hay email y provider para iniciar sesión si lo hubiera
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email_activo = prefs.getString("email",null);
        String provider_activo = prefs.getString("provider",null);
        if (email_activo!=null && provider_activo!=null) {
            abrirHome(email_activo, homeActivity.ProviderType.valueOf(provider_activo));
        }

        //llamada a metodos y casteo de botones
        main();

    }

     private void main() {
        controls();
        registrarUsuario();
        loginUsuarioConEmail();
        loginUsuarioConGoogle();

     }

     private void loginUsuarioConGoogle() {
            botonLoginGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken("585709910502-sodpt1knckp54pe2oablbjrt4ot9n9oo.apps.googleusercontent.com")
                            .requestEmail()
                            .build();

                    mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
                    mGoogleSignInClient.signOut();
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    //googleClient.signOut();
                    startActivityForResult(signInIntent, GOOGLE_SIGN_IN_CODE);

                }
            });
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == GOOGLE_SIGN_IN_CODE) {
             Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

             try {
                 // Google Sign In was successful, authenticate with Firebase
                 GoogleSignInAccount account = task.getResult(ApiException.class);

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                    abrirHome(account.getEmail(),homeActivity.ProviderType.GOOGLE);
                    } else {
                        showAlert("pollas de google");
                    }
                }
                });
             } catch (ApiException e) {
                 // Google Sign In failed, update UI appropriately
                 e.printStackTrace();
             }
         }
     }

     private void controls() {
        inputEmail = findViewById(R.id.editTextEmail);
        inputPassword = findViewById(R.id.editTextPassword);
        register = findViewById(R.id.buttonRegister);
        loginEmail = findViewById(R.id.buttonLogin);
        botonLoginGoogle = findViewById(R.id.buttonGoogle);

     }

     private void loginUsuarioConEmail() {
         loginEmail.setOnClickListener(new View.OnClickListener() {
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