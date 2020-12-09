package com.example.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signupactivity extends AppCompatActivity {
    private EditText emailEt,passwordEt1,passwordEt2;
    private Button SignUpButton;
    private TextView SignInTv;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        firebaseAuth=FirebaseAuth.getInstance();
        emailEt=findViewById(R.id.email);
        passwordEt1=findViewById(R.id.password1);
        passwordEt2=findViewById(R.id.password2);
        SignUpButton=findViewById(R.id.register);
        progressDialog=new ProgressDialog(this);
        SignInTv=findViewById(R.id.signInTv);
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        });

        SignInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signupactivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //
    private void Register(){
        String email=emailEt.getText().toString();
        String password1=passwordEt1.getText().toString();
        String password2=passwordEt2.getText().toString();

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

        //Besked som kommer når brugeren har oprettet konto hala
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        //Når brugerens forsøg på login er succesfuld, vil det føre dem til en tom side som indeholder en knap som returner dem login siden.
        firebaseAuth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(signupactivity.this,"Successfully registered",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(signupactivity.this,HomeNavigation.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(signupactivity.this,"Sign up fail!",Toast.LENGTH_LONG).show();
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
