package com.example.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class User {
    private String user;
    private Context contextOfActivity; //https://stackoverflow.com/questions/7491287/android-how-to-use-sharedpreferences-in-non-activity-class
    private final SharedPreferences gemmeobjekt;

    private static User instance;

    private User(Context context) {
        this.contextOfActivity = context;
        this.gemmeobjekt = PreferenceManager.getDefaultSharedPreferences(contextOfActivity);
        //Denne klasse burde være singleton, da man lige nu henter fra gemmeobjekt hver gang man går ind i aktiviteten.
        // Hvis det var singleton/samme objekt skulle vi kun hente det en gang. Så ville vi kunne redigere det når man logger ind og ud, men ellers bruge det samme herfra.
        this.user = gemmeobjekt.getString("username", "");
    }

    public static User getInstance(Context context) {
        if(instance == null){
            instance = new User(context);
        }
        return instance;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) { //Kan bruges når man laver bruger, eller når man logger ind. Skal vi bruge i databaseklassen, for at, når man logger ind, at den også gemmer brugeren på harddisken. og her i objektet.
        gemmeobjekt.edit().putString("username", user).apply(); //Hvorfor vi bruger apply(), hurtigere end commit  https://stackoverflow.com/questions/5960678/whats-the-difference-between-commit-and-apply-in-sharedpreferences
        this.user = user;
    }

    //Login ville så kræve database og skulle måske derind. Eller hvad.
    // ihvertfald kunne man i den tjekke om input er lig en bruger på databasen.

    public void logOut(){
        gemmeobjekt.edit().remove("username").apply();
        this.user = gemmeobjekt.getString("username", "");
    }
}
