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

import java.util.Random;

public class Notifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        final FirebaseDatabase[] database = {FirebaseDatabase.getInstance()}; //Get instance of database
        final DatabaseReference myRefComp = database[0].getReference("Competition"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data. Også når jeg indsætter data.
        final DatabaseReference myRefUser = database[0].getReference("User"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data. Også når jeg indsætter data.

        final TextView joinCompTxt = findViewById(R.id.joinCompTxt);
        final EditText joinCompInput = findViewById(R.id.joinCompInput);
        final Button joinCompBtn = findViewById(R.id.joinCompBtn);

        final TextView createCompTxt = findViewById(R.id.createCompTxt);
        final Button createCompBtn = findViewById(R.id.createCompBtn);
        final TextView createCompNewInfo = findViewById(R.id.newCompInfo);


        final User user = User.getInstance(this);


        //onButtonClick: //Ved tryk på join competition knap
        //Skal have skrevet i tekstfelt. Hvis child med det id eksisterer, så skriv deres navn ind og sig "you have now joined". Ellers sig wrong number.
        joinCompBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!joinCompInput.getText().equals("")){ //Hvis den ikke er tom.
                    myRefComp.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //Burde måske tjekke om den nuværende bruger allerede er på databasen under denne comp, så man ikke kan konkurrerere med sig selv.
                            if(!dataSnapshot.child(joinCompInput.getText().toString()).child("2").exists()) {//myRefComp.child(joinCompInput.getText().toString()).child("2").setValue(user.getUser());
                            if(dataSnapshot.child(joinCompInput.getText().toString()).exists()) { //if competition with inputtet ID exists.
                                    //add user to competition
                                    myRefComp.child(joinCompInput.getText().toString()).child("2").setValue(user.getUser());
                                    myRefUser.child(user.getUser()).child("CompetitionID" + joinCompInput.getText().toString()).setValue(joinCompInput.getText().toString());
                                    myRefUser.child(user.getUser()).child("CompetitionID" + joinCompInput.getText().toString()).child(user.getUser() + "UserValue").setValue("2");
                                myRefUser.child(user.getUser()).child("CompetitionID").setValue(Integer.parseInt(joinCompInput.getText().toString()));
                                    joinCompTxt.setText("You have now joined the competition");
                                } else{
                                    joinCompTxt.setText("Competition does not exist, try inputting a different CompID");
                                }
                            }else {
                                joinCompTxt.setText("There are already 2 users in this Competition");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


        //nedenstående virker nu.
        createCompBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                final int randomCompNumber = random.nextInt(1000);
                createCompNewInfo.setText("Competition Number: " + randomCompNumber);
                myRefComp.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//Kune tjekke om man allerede er gang med en comp, for at man ikke kan være med i flere på en gang. Så kunne man også stoppe compen ved at fjerne det ID der er.
                        //Se om comp eksisterer, ellers lave den.
                        if(!dataSnapshot.child(String.valueOf(randomCompNumber)).exists()){ //Man skal ikke skrive child("Competition"), da det er fra myRefComp allerede
                            myRefComp.child(String.valueOf(randomCompNumber)).setValue(null); //Læg den nye comp op på database. Kan bare ikke finde ud af at gøre uden value. Men den må vel godt have value. Men kunne være smart bare at tilføje child. Tror det er uden value nu, da string er tom.
                            //Så inde i workoutDone sige hvis bruger har comp, sæt værdi derind og tilføj nuværende oveni hvis den findes.
                            myRefUser.child(user.getUser()).child("CompetitionID" + randomCompNumber).setValue(String.valueOf(randomCompNumber));
                            myRefUser.child(user.getUser()).child("CompetitionID" + randomCompNumber).child(user.getUser() + "UserValue").setValue("1");
                            myRefUser.child(user.getUser()).child("CompetitionID").setValue(randomCompNumber);
                            myRefComp.child(String.valueOf(randomCompNumber)).child("1").setValue(user.getUser());
                            createCompNewInfo.setText("You have now created a new competiton. Send this CompID: " + randomCompNumber + " to compete with them");
                        } else { //Else lav nyt nummer. fordi det allerede findes.
                            createCompNewInfo.setText("this CompID already exists, press the button again");
                        }
                    }
                    /*
                        if(Totalreps(på database).exists) { //Burde nok lave ny totalReps som vi kan gemme på hvis den findes i workoutDone, så der er all time totalReps og comp totalReps.
                            myRef.child(String.valueOf(randomCompNumber)).child(user.getUser).setValue(myRefUser.child(user.getUser).child(TotalReps));
                        }
                        setText("Exercise to win the Competition" +
                                "\n Send the code to a friend, for them to join the Competition");

                         */



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        }; //Så sådan ud før });
        //onButtonClick: //Ved tryk på start Competition knap
        /*
        Random random = new Random();
        int randomCompNumber = random.nextInt(1000);
        setText("Competition Number: " + randomCompNumber)
        if(Totalreps(på database).exists) {
            myRef.child(String.valueOf(randomCompNumber)).child(user.getUser).setValue(myRefUser.child(user.getUser).child(TotalReps));
        }
        setText("Exercise to win the Competition" +
                "\n Send the code to a friend, for them to join the Competition");
*/
        //Skal så bare gemme den nye totalReps under Comp. Så gemme datoen for upload der også. Se om det er mere end en uge siden, for så at slette hvis sandt.
        //For hver træning lægge en ny totalReps op på både egen bruger (all time), og comp, som slettes hvis der går over en uge. Kan måske også bare være fra hvornår man startede.
        //Hvis ens værdi så er lavere end modstanderens kan der stå i profil at man taber.
        //Man kan joine flere comps på samme tid.
        //Hvis vi har gemt det under brugeren kan det også vises på Profil med all time total Reps.

        });
    }
}