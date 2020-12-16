package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spark.submitbutton.SubmitButton;

public class MainActivity extends AppCompatActivity {
    private EditText emailEt,passwordEt;
    private SubmitButton SignInButton;
    private TextView SignUpTv;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    User user1; //Altid her den laves først, så altid den context der bruges til at instantiere objektet.




    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer lyd = MediaPlayer.create(this, R.raw.yes); //Create sound

        final FirebaseDatabase[] database = {FirebaseDatabase.getInstance()}; //Get instance of database
        final DatabaseReference myRef = database[0].getReference("User"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data. Også når jeg indsætter data.

        user1 = User.getInstance(this); //Altid her den laves først, så altid den context der bruges til at instantiere objektet.


        //Hvis brugeren ikke er logget ind.
        if(user1.getUser().isEmpty()){ //Har gjort det med brugerobjektet og det virker.
            System.out.println("user not logged in");
        } else { //Hvis brugeren er logget ind, kommer vi til hjemmeskærmen.
            Intent loggedIn = new Intent(MainActivity.this, HomeNavigation.class);
            startActivity(loggedIn);
        }


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout1);

        final ImageView profileImage = findViewById(R.id.imageLogin);
        profileImage.setImageResource(R.drawable.zlogo);

        final TextView display = findViewById(R.id.textView);

        firebaseAuth=FirebaseAuth.getInstance();
        emailEt=findViewById(R.id.email);
        passwordEt=findViewById(R.id.password);
        SignInButton=findViewById(R.id.login);
        progressDialog=new ProgressDialog(this);
        SignUpTv=findViewById(R.id.signUpTv);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        SignUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
    }

    private void Login(){
        final String email=emailEt.getText().toString();
        String password=passwordEt.getText().toString();
        //Tjekker om parameter er null, hvis true, dukker en besked op med at der skal indtastes en email brr brr
        if(TextUtils.isEmpty(email)){
            emailEt.setError("Enter your email");
            return;
        }
        //Tjekker om parameter er null, hvis true, dukker en besked op med at der skal indtastes en kode brr brr

        else if(TextUtils.isEmpty(password)){
            passwordEt.setError("Enter your password");
            return;
        }
        //Loading besked efter der er logget in aiwa
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        // Når brugerens forsøg på login er succesfuld, vil det føre dem til en tom side som indeholder en knap som returner dem login siden.
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    user1.setUser(firebaseAuth.getUid()); //Prøver at sætte herned, da brugeren skal være logget ind. måske rykke logget ind, herind. Så den nye bruger er logget ind.
                    Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(MainActivity.this, HomeNavigation.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this,"Sign In fail!",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}