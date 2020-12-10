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
    FirebaseStorage storage;
    StorageReference storageReference;
    Button uploadProfilePic;
    ImageView profilePic;
    Button btnSave;
    private FirebaseAuth firebaseAuth;

    private final int IMG_REQUEST_ID = 1;
    private Uri imgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        
        final TextView homeName = findViewById(R.id.homeName);
        final Button logoutBtn = findViewById(R.id.logoutButton);
        ImageView profilePic = findViewById(R.id.profilePic);
        uploadProfilePic = findViewById(R.id.uploadImage);
        btnSave = findViewById(R.id.btn_save);
        btnSave.setEnabled(false);
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


        //Hvis pb findes på DB, set profilePic til det. Ellers hav knap hvor man kan hente det. Lige nu bare uploade.

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();


        //Burde nok fjerne det med profilbillede, da det ikke lige fungerer.
        uploadProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestImage();
            }
        });

        /*  gemmer billedet i firebase, når der bliver klikket på save.*/
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveInFirebase();
            }
        });


        homeName.setText(firebaseAuth.getCurrentUser().getDisplayName()); //Virker med user.getuser og med firebaseAuth.getCurrentUser().getEmail() viser username

        //Nedenstående skal tilføjes igen, fjernede for at teste.


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(user).child("BMI").exists()) {
                    homeName.setText("hello " + user + ", your BMI is " + dataSnapshot.child(user).child("BMI").getValue().toString());
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
                /*
                firebase.auth().signOut().then(function() {
                    // Sign-out successful.
                }).catch(function(error) {
                    // An error happened.
                });

                 */
                Intent logoutIntent = new Intent(Profile.this, MainActivity.class);
                startActivity(logoutIntent);
            }
        });


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            int competitionID;
            int userCompetitionID;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(user1.getUser()).child("CompetitionID").exists()) {
                    competitionID =  dataSnapshot.child(user1.getUser()).child("CompetitionID").getValue(Integer.class);
                    user1.setCompetitionID(competitionID);
                    userCompetitionID = Integer.parseInt(dataSnapshot.child(user1.getUser()).child("CompetitionID" + competitionID).child(user1.getUser() + "UserValue").getValue(String.class));
                    user1.setUserCompetitionID(userCompetitionID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefComp.addListenerForSingleValueEvent(new ValueEventListener() {
            int otherUserCompReps;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(String.valueOf(user1.getCompetitionID())).exists())//Skal tjekke i comp, skal lige finde ud af hvordan. Tror ikke man kan lave listener herinde)
                    //Det kan man ikke. Enten lave databasestruktur om eller lægge CompID i sharedPreferences og hente derfra.
                    // Kunne vel egentlig også bare være i brugerobjekt. Gøre det herinde, før man lægger op.
                    if(user1.getUserCompetitionID() == 1){ //Finder den anden brugers id baseret på ens egen, da der kun burde være 2 i konkurrencen.
                        int otherUserCompID = 2;
                        if(dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(otherUserCompID)).child("CompReps").exists()) {
                            otherUserCompReps = Integer.parseInt(dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(otherUserCompID)).child("CompReps").getValue(String.class));
                            int userCompReps = Integer.parseInt(dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(user1.getUserCompetitionID())).child("CompReps").getValue(String.class));
                            if(otherUserCompReps > userCompReps){
                                compStatus.setText("you are losing your competition, get to work " + user1.getUser());
                            } else {
                                compStatus.setText("You are winning your competition, "  + user1.getUser()  + " you absolute champion");
                            }
                        } else{
                            compStatus.setText("No one has joined your comp yet. Send the CompID to them, so they can join: " + user1.getCompetitionID());
                        }
                        }else if (user1.getUserCompetitionID() == 2){
                        int otherUserCompID = 1;
                        if(dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(otherUserCompID)).child("CompReps").exists()) {
                            otherUserCompReps = Integer.parseInt(dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(otherUserCompID)).child("CompReps").getValue(String.class));
                            int userCompReps = Integer.parseInt(dataSnapshot.child(String.valueOf(user1.getCompetitionID())).child(String.valueOf(user1.getUserCompetitionID())).child("CompReps").getValue(String.class));
                            if(otherUserCompReps > userCompReps){
                                compStatus.setText("you are losing your competition, get to work " + user1.getUser());
                            } else {
                                compStatus.setText("You are winning your competition, "  + user1.getUser()  + " you absolute champion");
                            }
                        } else{
                            compStatus.setText("No one has joined your comp yet. Send the CompID to them, so they can join: " + user1.getCompetitionID());
                        }
                        }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
//Burde nok slettes
    private void requestImage () {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //startActivityForResult(Intent.createChooser(intent,"select picture"), IMG_REQUEST_ID);
        startActivityForResult(intent, IMG_REQUEST_ID);
    }
/*
    private void saveInFirebase() {
        if (imgUri != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please Wait...");
            progressDialog.show();
            StorageReference reference = storageReference.child("picture/" + UUID.randomUUID().toString());

            try {
                reference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(Profile.this, "saved succesfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this, "error occurred" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double Progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("saved" + (int) Progress + "%");


                        uploadProfilePic.setEnabled(true);
                        btnSave.setEnabled(false);


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

 */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST_ID && resultCode == RESULT_OK/* && data != null && data.getData() != null*/) {
            imgUri = data.getData();
/*
            try {
                Bitmap bitmapImg = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);

                profilePic.setImageBitmap(bitmapImg);
                uploadProfilePic.setEnabled(false);
                btnSave.setEnabled(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
 */

Uri imageData = data.getData();

StorageReference imageName = storageReference.child("picture"+imageData.getLastPathSegment());

imageName.putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Toast.makeText(Profile.this, "uploaded", Toast.LENGTH_SHORT).show();
    }
});
        }
    }
}