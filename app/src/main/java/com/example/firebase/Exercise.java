package com.example.firebase;

public abstract class Exercise {
    private int reps;

    public Exercise() {
        this.reps = 0;
    }

    public int getReps() {
        return reps;
    }

    public void addRep(){
        reps++;
    }
}
