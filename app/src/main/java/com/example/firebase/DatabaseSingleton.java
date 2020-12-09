package com.example.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DatabaseSingleton {
    final FirebaseDatabase[] database = {FirebaseDatabase.getInstance()}; //Get instance of database
    final DatabaseReference myRefUser = database[0].getReference("User"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data. Også når jeg indsætter data.
    final DatabaseReference myRefComp = database[0].getReference("Competition");
    private static DatabaseSingleton instance;

    private DatabaseSingleton() {
    }

    public static DatabaseSingleton getInstance(){
        if(instance == null){
            instance = new DatabaseSingleton();
        }
        return instance;
    }

    //Skal så bare kalde metoden i mainactivity, hvor jeg sætter
    public void saveNewUser(String username, String password){ //Skal bare fjerne password når auth kommer.
        myRefUser.child(username).child("password").setValue(password);
    }

    public void saveBMI(String user, float BMI){
        myRefUser.child(user).child("BMI").setValue(BMI);
    }

    public void saveCompReps(int competititonID, int userCompetitionID, int compReps){
        myRefComp.child(String.valueOf(competititonID)).child(String.valueOf(userCompetitionID)).child("CompReps").setValue(String.valueOf(compReps));
    }

    public void createComp(String user, int randomCompNumber){
        myRefComp.child(String.valueOf(randomCompNumber)).setValue(null); //Læg den nye comp op på database. Kan bare ikke finde ud af at gøre uden value. Men den må vel godt have value. Men kunne være smart bare at tilføje child. Tror det er uden value nu, da string er tom.
        //Så inde i workoutDone sige hvis bruger har comp, sæt værdi derind og tilføj nuværende oveni hvis den findes.
        myRefUser.child(user).child("CompetitionID" + randomCompNumber).setValue(String.valueOf(randomCompNumber));
        myRefUser.child(user).child("CompetitionID" + randomCompNumber).child(user + "UserValue").setValue("1");
        myRefUser.child(user).child("CompetitionID").setValue(randomCompNumber);
        myRefComp.child(String.valueOf(randomCompNumber)).child("1").setValue(user);
    }

    public void joinComp(String user, String joinCompInput){
        myRefComp.child(joinCompInput).child("2").setValue(user);
        myRefUser.child(user).child("CompetitionID" + joinCompInput).setValue(joinCompInput);
        myRefUser.child(user).child("CompetitionID" + joinCompInput).child(user + "UserValue").setValue("2");
        myRefUser.child(user).child("CompetitionID").setValue(Integer.parseInt(joinCompInput));
    }






}
