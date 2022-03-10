package com.example.firebase_testing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;

public class homeActivity extends AppCompatActivity {
    TextView email;
    TextView pass;
    Button cerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        main();
        Bundle extras = getIntent().getExtras();

        String e = (String) extras.get("email");
        String p = (String) extras.get("provider");
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
        closeIntent();
    }

    private void controls() {
        email = findViewById(R.id.textViewEmail);
        pass = findViewById(R.id.textViewPassword);
        cerrar = findViewById(R.id.buttonClose);
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

