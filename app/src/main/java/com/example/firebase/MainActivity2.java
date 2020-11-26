package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final EditText userName = findViewById(R.id.editTextTextPersonName2);

        final EditText password = findViewById(R.id.editTextTextPersonName4);
        Button login = findViewById(R.id.button);

        final TextView display = findViewById(R.id.textView);

        final FirebaseDatabase[] database = {FirebaseDatabase.getInstance()}; //Get instance of database
        final DatabaseReference myRef = database[0].getReference("User"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data. Også når jeg indsætter data.


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Tjek om brugernavn og kode stemmer overens.
                //Bare se om den med child der er brugernavn har child med det password. Så bare sige username eller password forkert og ellers sig, velkommen + username

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //display.setText(dataSnapshot.child(userName.getText().toString()).child("password").getValue().toString());

                            if(dataSnapshot.child(userName.getText().toString()).child("password").getValue().toString().equals(password.getText().toString())){
                                display.setText("You have inputtet a matching pair of username and password! :)");
                            } else{
                                display.setText("wrong username or password. :(");
                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                //display.setText("you have attempted a login.");

            }
        });


    }
}