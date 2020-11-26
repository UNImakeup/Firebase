package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    boolean userIs = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer lyd = MediaPlayer.create(this, R.raw.yes); //Create sound

        final Button playsound = this.findViewById(R.id.play_sound); //Get the button

        final TextView showData = this.findViewById(R.id.show_data); //get the textview.

        final ImageView img = this.findViewById(R.id.imageView);

        final EditText username = this.findViewById(R.id.editTextTextPersonName);

        final EditText password = this.findViewById(R.id.editTextTextPersonName3);

        final Button loginBtn = this.findViewById(R.id.login_button);
        //loginBtn.setVisibility(View.INVISIBLE);

        img.setImageResource(R.drawable.boat);

        final FirebaseDatabase[] database = {FirebaseDatabase.getInstance()}; //Get instance of database
        final DatabaseReference myRef = database[0].getReference("User"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data. Også når jeg indsætter data.

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String a= dataSnapshot.getValue().toString();
                if(dataSnapshot.child("4").exists()) { //Kan se om en bestemt bruger eksisterer. Kan bruges når vi skal oprette ny bruger. Hvis den ikke eksisterer, tilføjer vi data, ved at sige setValue, ved et bestemt child.
                    showData.setText("has aids");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        playsound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //When button playsound is clicked.
                final boolean[] userExists = {false};


                lyd.start(); //Play sound

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //String a= dataSnapshot.getValue().toString();
                        if(dataSnapshot.child(username.getText().toString()).exists()) { //Kan se om en bestemt bruger eksisterer. Kan bruges når vi skal oprette ny bruger. Hvis den ikke eksisterer, tilføjer vi data, ved at sige setValue, ved et bestemt child.
                            showData.setText("username taken"); //Den kan nu se at brugernavnet er taget, skal så bare stoppe den fra at lave det nye.
                            userExists[0] = true;
                            userIs = true;
                        }

                        if(userIs == false) {
                            myRef.child(username.getText().toString()).child("password").setValue(password.getText().toString()); //Send data to database
                            showData.setText("user created");
                            //loginBtn.setVisibility(View.VISIBLE);
                        }

                        userIs = false;


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            //String greeting = dataSnapshot.child("message").child("childmessage").getValue().toString();
                            String greeting = dataSnapshot.getValue().toString();
                            /*
                            if(!dataSnapshot.getValue().toString().equals(null)){
                                System.out.println("wagwan blud, no user");
                                showData.setText("no User");
                            }
                             */
                            //showData.setText(greeting);
                            img.setImageResource(R.drawable.lol);
                            //img.setVisibility(View.INVISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);

            }
        });

    }
}