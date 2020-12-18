package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class Profile extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final TextView homeName = findViewById(R.id.homeName);
        final Button logoutBtn = findViewById(R.id.logoutButton);
        final TextView compStatus = findViewById(R.id.compStatus);
        firebaseAuth = FirebaseAuth.getInstance();

        //Skrive på harddisk, gemme hvem der er login. Kunne også gemme password i guess, for at tjekke hashcode og sådan.
        final SharedPreferences gemmeobjekt = PreferenceManager.getDefaultSharedPreferences(this);
        final String user = gemmeobjekt.getString("username", "");

        //Database
        final FirebaseDatabase[] database = {FirebaseDatabase.getInstance()}; //Get instance of database
        final DatabaseReference myRef = database[0].getReference("User"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data. Også når jeg indsætter data.
        final DatabaseReference myRefComp = database[0].getReference("Competition");
        final User user1 = User.getInstance(this); //Context er ligegyldig, den henter alligevel i mainActivity


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            int competitionID;
            int userCompetitionID;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(user1.getUser()).child("CompetitionID").exists()) {
                    competitionID =  dataSnapshot.child(user1.getUser()).child("CompetitionID").getValue(Integer.class);
                    user1.setCompetitionID(competitionID);
                    userCompetitionID = Integer.parseInt(dataSnapshot.child(user1.getUser()).child("CompetitionID" + competitionID).
                            child(user1.getUser() + "UserValue").getValue(String.class));
                    user1.setUserCompetitionID(userCompetitionID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user).child("BMI").exists()) {
                    homeName.setText("hello " + firebaseAuth.getCurrentUser().getDisplayName() +
                            ", your BMI is " + dataSnapshot.child(user).child("BMI").getValue().toString());
                } else {
                    homeName.setText("Please input your Height and Weight in Insights, to see your BMI here");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gemmeobjekt.edit().remove("username").apply(); //Kun her og ved login at gemmeobjekt skal bruges.
                user1.logOut();
                firebaseAuth.signOut();
                Intent logoutIntent = new Intent(Profile.this, MainActivity.class);
                startActivity(logoutIntent);
            }
        });


        myRefComp.addListenerForSingleValueEvent(new ValueEventListener() {
            int otherUserCompReps;
            int userCompReps;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(String.valueOf(user1.getCompetitionID())).exists()) { //denne ender forkert, burde dække hele sætningen.
                    if (user1.getUserCompetitionID() == 1) { //Finder den anden brugers id baseret på ens egen, da der kun burde være 2 i konkurrencen.
                        int otherUserCompID = 2;
                        if (dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(otherUserCompID)).child("CompReps").exists()) {
                            otherUserCompReps = Integer.parseInt(dataSnapshot.child(String.valueOf(user1.getCompetitionID())).
                                    child(String.valueOf(otherUserCompID)).child("CompReps").getValue(String.class));
                        } else {
                            compStatus.setText("No one has joined your comp yet. Send the CompID to them, so they can join: " + user1.getCompetitionID());
                        }
                        if (dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(user1.getUserCompetitionID())).child("CompReps").exists()) {
                            userCompReps = Integer.parseInt(dataSnapshot.child(String.valueOf(user1.getCompetitionID())).
                                    child(String.valueOf(user1.getUserCompetitionID())).child("CompReps").getValue(String.class));
                        }
                        if (dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(otherUserCompID)).child("CompReps").exists()
                                && otherUserCompReps > userCompReps) {
                            compStatus.setText("you are losing your competition, get to work " + firebaseAuth.getCurrentUser().getDisplayName());
                        } else if(dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(otherUserCompID)).child("CompReps").exists()
                                && otherUserCompReps < userCompReps){
                            compStatus.setText("You are winning your competition, " + firebaseAuth.getCurrentUser().getDisplayName() + " you absolute champion");
                        }

                    } else if (user1.getUserCompetitionID() == 2) {
                        int otherUserCompID = 1;
                        if (dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(otherUserCompID)).child("CompReps").exists()) {
                            if (dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(otherUserCompID)).child("CompReps").exists()) {
                                otherUserCompReps = Integer.parseInt(dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(otherUserCompID)).child("CompReps").getValue(String.class));
                            } //Behøver ikke tjekke om den anden bruger er oprette og skrive at de ikke er, da man er nummer 2 og altså ikke den der oprettede. Der er et andet medlem.
                            if (dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(user1.getUserCompetitionID())).child("CompReps").exists()) {
                                userCompReps = Integer.parseInt(dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(user1.getUserCompetitionID())).child("CompReps").getValue(String.class));
                            }
                            if (otherUserCompReps > userCompReps) {
                                compStatus.setText("you are losing your competition, get to work " + firebaseAuth.getCurrentUser().getDisplayName());
                            } else {
                                compStatus.setText("You are winning your competition, " + firebaseAuth.getCurrentUser().getDisplayName() + " you absolute champion");
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



    }
        }