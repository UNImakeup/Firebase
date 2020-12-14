package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity2 extends AppCompatActivity {
    private EditText emailEt,passwordEt1,passwordEt2, username;
    private Button SignUpButton;
    private TextView SignInTv;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    final User user1 = User.getInstance(this); //Henter bare den samme bruger.

    final FirebaseDatabase[] database = {FirebaseDatabase.getInstance()}; //Get instance of database
    final DatabaseReference myRefUser = database[0].getReference("User"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data. Også når jeg indsætter data.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        firebaseAuth=FirebaseAuth.getInstance();

        emailEt=findViewById(R.id.email);
        passwordEt1=findViewById(R.id.password1);
        passwordEt2=findViewById(R.id.password2);
        username = findViewById(R.id.userName);
        SignUpButton=findViewById(R.id.register);
        final ImageView profileImage = findViewById(R.id.imageView);
        profileImage.setImageResource(R.drawable.zlogo);

        progressDialog=new ProgressDialog(this);
        SignInTv=findViewById(R.id.signInTv);

        //Det vil virke, da man stadig henter fra gemmeobjekt, når man laver user objektet. Så her ser vi bare om der er hentet/om brugeren er logget ind.
        if(!user1.getUser().isEmpty()){ //Hvis der er en bruger logget ind. Burde nok gøre det på den første side. Så kan man enten lave en bruger eller logge ind, hvis man ikke er det.
            Intent homeIntent = new Intent(MainActivity2.this, Home.class);
            startActivity(homeIntent);
        }

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

        SignInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity2.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left); //Ikke hokuspokus
                finish();
            }
        });
    }
    private void Register(){
        final String email=emailEt.getText().toString();
        String password1=passwordEt1.getText().toString();
        String password2=passwordEt2.getText().toString();
        final String username1 = username.getText().toString();

        //Tjekker om der er indtastet/ om er null, hvis true, bliver der returned en besked op med at der skal skrives email brr brr
        if(TextUtils.isEmpty(email)){
            emailEt.setError("Enter your email");
            return;
        }
        //Tjekker om der er indtastet/ om parameter er null, hvis true bliver der returned en besked op med at der skal skrives kode brr brr

        else if(TextUtils.isEmpty(password1)){
            passwordEt1.setError("Enter your password");
            return;
        }
        //Tjekker om parameter er null, hvis true, dukker en besked op med at der skal skrives kode ro ro ro din

        else if(TextUtils.isEmpty(password2)){
            passwordEt2.setError("Confirm your password");
            return;
        }
        //Tjekker om parameter er null, hvis true, dukker en besked op med at der indtastet to forskellige koder brr brr

        else if(!password1.equals(password2)){
            passwordEt2.setError("Different password");
            return;
        }

        //Længden af kodeord skal være mindst 6 karakter, pga firebase - the more you know
        else if(password1.length()<6){
            passwordEt1.setError("Length should be at least 6 characters");
            return;
        }
        //besked dukker op hvis der ikke bliver indtastet en gyldig mail som indeholder gmail@.com etc. wallah
        else if(!isVallidEmail(email)){
            emailEt.setError("invalid email");
            return;
        }

        else if(TextUtils.isEmpty(username1)){ //Hvis man ikke har brugernavn
            username.setError("input a username, my brother");
            return;
        }

        //Besked som kommer når brugeren har oprettet konto halla
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        //Når brugerens forsøg på login er succesfuld, vil det føre dem til hjemme siden.
        firebaseAuth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(username1)
                            .build();
                    firebaseAuth.getCurrentUser().updateProfile(profileUpdates);
                    user1.setUser(firebaseAuth.getUid()); //Prøver at sætte herned, da brugeren skal være logget ind. måske rykke logget ind, herind. Så den nye bruger er logget ind.
                    myRefUser.child(firebaseAuth.getUid()).setValue(""); //Send data to database //Er kommet i databaseSingleton, så kan bare kalde den med det objekt.
                    Toast.makeText(MainActivity2.this,"Successfully registered",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(MainActivity2.this, HomeNavigation.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity2.this,"Sign up fail!",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });


    }
    // Tjekker om det en gyldig mail som indeholder gmail@.com etc. skrr
    private Boolean isVallidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}