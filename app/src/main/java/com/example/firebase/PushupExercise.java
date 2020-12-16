package com.example.firebase;

public class PushupExercise extends Exercise{
    //Ved ikke om reps skal herind. Har alle en reps eller er der bare en i superklassen.
    //Alle har fra superklassen

    //private static PushupExercise instance;

    public PushupExercise() { //Difficulty vil så kunne sætte hvor man reps man skal lave. Man kan sætte reps ud fra hvad folk vælger i skærmen før øvelsen.

    }

    @Override
    public int getReps() { //For at vise reps
        return super.getReps();
    }

    /*
    //Fjerner da den starter på nul når man laver objektet og den ikke er singleton.
    @Override
    public void setReps(int reps) { //For at sætte reps lig nul, når øvelsen begynder.
        super.setReps(reps);
    }
     */

    @Override
    public void addRep() { //Lægge til reps.
        super.addRep();
    }
}
