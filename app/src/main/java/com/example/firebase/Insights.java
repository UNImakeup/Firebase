package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Insights extends AppCompatActivity {

    EditText Height, Weight;
    TextView answer;
    Button calculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);

        //implementation of BMI calculator

        Height=(EditText) findViewById(R.id.height);
        Weight=(EditText) findViewById(R.id.weight);
        answer=(TextView) findViewById(R.id.answer);
        calculate=(Button) findViewById(R.id.calculate);
        final FirebaseDatabase[] database = {FirebaseDatabase.getInstance()}; //Get instance of database
        final DatabaseReference myRef = database[0].getReference("User"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data. Også når jeg indsætter data.

        /*
        final SharedPreferences gemmeobjekt = PreferenceManager.getDefaultSharedPreferences(this);
        final String user = gemmeobjekt.getString("username", "");
         */
        //Burde være gemt i bruger objekt, så vi kunne hente det derfra. Kunne lave brugerobjekt der kun indeholder det faktisk.
        final User user1 = User.getInstance(this); //Bruger context fra MainActivity så det er i orden.

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weight = Weight.getText().toString();
                String height = Height.getText().toString();

                BMIModel bmiModel = new BMIModel(height, weight);

                myRef.child(user1.getUser().toString()).child("BMI").setValue(bmiModel.calculateBMI()); //Også inde i databaseSingleton


                answer.setText(bmiModel.displayBMI(bmiModel.calculateBMI()));
                //Error message if nothing typed

                if(TextUtils.isEmpty(weight)){
                    Weight.setError("You need to enter your weight in order to calculate your BMI");
                    Weight.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(height)){
                    Height.setError("You need to enter your height in order to calculate your BMI");
                    Height.requestFocus();
                    return;
                }
            }
        });
    }
}