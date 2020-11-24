package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer lyd = MediaPlayer.create(this, R.raw.yes); //Create sound

        final Button playsound = this.findViewById(R.id.play_sound); //Get the button

        final TextView showData = this.findViewById(R.id.show_data); //get the textview.

        final ImageView img = this.findViewById(R.id.imageView);
        img.setImageResource(R.drawable.boat);

        FirebaseDatabase database = FirebaseDatabase.getInstance(); //Get instance of database
        final DatabaseReference myRef = database.getReference("message").child("childmessage"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data

        playsound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //When button playsound is clicked.
                lyd.start(); //Play sound

                myRef.setValue("Hello, World!"); //Send data to database
                

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            //String greeting = dataSnapshot.child("message").child("childmessage").getValue().toString();
                            String greeting = dataSnapshot.getValue().toString();
                            showData.setText(greeting);
                            img.setImageResource(R.drawable.lol);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }
        });

    }
}