package com.example.firebase;

public abstract class Exercise {
    private int reps; //Kunne have liste med exercises. polymorphi
    private int difficulty;

    /*
    private static Exercise instance;

    public static Exercise getInstance(int difficulty) {
        if(instance == null) {
            instance = new Exercise(difficulty);
        }
        return instance;
    }
    */

    public Exercise(int difficulty) {
        this.reps = 0;
        this.difficulty = difficulty;
    }



    public int getReps() {
        return reps;
    }

    //Har nok ikke brug for setter, da den starter på nul og man tilføjer.
/*
    public void setReps(int reps) {
        this.reps = reps;
    }
 */

    public void addRep(){
        reps++;
    }
}
