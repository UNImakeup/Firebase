package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WorkoutDone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_done);
        TextView workoutSummary = findViewById(R.id.workoutResult);
        Button workoutDoneBtn = findViewById(R.id.workoutDoneBtn);
        ExerciseData exerciseData = ExerciseData.getInstance();

        //Spille anime wow lyd


        //vise tekst. Måske bruge metode i ExerciseData, print og sådan. Vise getSum til sidst.
        workoutSummary.setText("Pushups: " + exerciseData.getExercises().get(0).getReps() +
        "\n Squats: " + exerciseData.getExercises().get(1).getReps() +
                "\n Situps: " + exerciseData.getExercises().get(2).getReps() +
                "\n Backbends: " + (exerciseData.getExercises().get(3).getReps() - 1)
        );

        //Knap til homeNavigation
        workoutDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHome = new Intent(WorkoutDone.this, HomeNavigation.class);
                startActivity(goHome);
            }
        });

    }
}