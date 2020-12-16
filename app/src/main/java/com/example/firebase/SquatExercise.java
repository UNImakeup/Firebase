package com.example.firebase;

public class SquatExercise extends Exercise {
    public SquatExercise() {

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
