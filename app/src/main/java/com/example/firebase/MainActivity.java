package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.spark.submitbutton.SubmitButton;


public class MainActivity extends AppCompatActivity {
    private EditText emailEt,passwordEt;
    private SubmitButton SignInButton;
    private TextView SignUpTv;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                Intent intent=new Intent(MainActivity.this, Signupactivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
    }


    private void Login(){
        String email=emailEt.getText().toString();
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
                    Toast.makeText(MainActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(MainActivity.this,HomeNavigation.class);
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
