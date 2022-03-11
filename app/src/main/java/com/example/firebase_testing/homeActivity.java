package com.example.firebase_testing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.drm.DrmStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentChange.Type;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Query.Direction;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;


import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class homeActivity extends AppCompatActivity {
    TextView email;
    TextView pass;
    EditText direccion;
    EditText telefono;

    Button guardar;
    Button recuperar;
    Button borrar;
    Button cerrar;
    final  FirebaseFirestore db = FirebaseFirestore.getInstance();


    String e = "";
    String p = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        main();
        Bundle extras = getIntent().getExtras();

        e = (String) extras.get("email");
        p = (String) extras.get("provider");
        //String p = (String) extras.get("pass");
        email.setText(e);
        pass.setText(p);

        //Guardado de datos

        SharedPreferences prefs = (SharedPreferences) getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", e);
        editor.putString("provider",p);
        editor.commit();

    }

    private void main() {
        controls();
        guardarDatos();
        recuperarDatos();
        borrarDatos();
        closeIntent();
    }

    private void controls() {
        email = findViewById(R.id.textViewEmail);
        pass = findViewById(R.id.textViewPassword);
        guardar = findViewById(R.id.buttonGuardar);
        recuperar = findViewById(R.id.buttonRecuperar);
        borrar = findViewById(R.id.buttonBorrar);
        cerrar = findViewById(R.id.buttonClose);
        direccion = findViewById(R.id.inputDireccion);
        telefono = findViewById(R.id.inputTelefono);
    }

    private void guardarDatos() {
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap <String, Object> datosUsuario = new HashMap<>();
                datosUsuario.put("provider",p);
                datosUsuario.put("address",direccion.getText().toString().trim());
                datosUsuario.put("phone",telefono.getText().toString().trim());

                db.collection("users").document(e).set(datosUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Datos guardados", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),"Vergazo", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }

    private void recuperarDatos() {
        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void borrarDatos() {
        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }





    private void closeIntent() {
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
                onBackPressed();
            }
        });
    }

    public enum ProviderType {
        BASIC,
        GOOGLE,
    }

}

