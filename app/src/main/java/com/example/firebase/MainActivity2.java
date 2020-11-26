package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        EditText userName = findViewById(R.id.editTextTextPersonName2);

        EditText password = findViewById(R.id.editTextTextPersonName4);
        Button login = findViewById(R.id.button);

        final TextView display = findViewById(R.id.textView);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tjek om brugernavn og kode stemmer overens.
                //Bare se om den med child der er brugernavn har child med det password. Så bare sige username eller password forkert og ellers sig, velkommen + username
                display.setText("you have attempted a login.");

            }
        });


    }
}