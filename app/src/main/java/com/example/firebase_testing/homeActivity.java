package com.example.firebase_testing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        String p = (String) extras.get("pass");
        email.setText(e);
        pass.setText(p);
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
                onBackPressed();
            }
        });
    }
}