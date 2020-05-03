package com.example.firebasenoteapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login)
    EditText login;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.loginbtn)
    Button loginbtn;
    @BindView(R.id.forgotpass)
    TextView forgotpass;
    @BindView(R.id.signup)
    TextView signup;
    @BindView(R.id.otplogin)
    TextView otplogin;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sharedPreferences=getSharedPreferences("FIRENOTDATA", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
    }


    @OnClick({R.id.loginbtn, R.id.signup,R.id.forgotpass,R.id.otplogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loginbtn:

                String email=login.getText().toString();
                String pass=password.getText().toString();

                if(!email.equalsIgnoreCase("")){

                    if (!pass.equalsIgnoreCase("")){

                        loginuser(email,pass);
                    }else {
                        Toast.makeText(this, "enter the password", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "enter the email", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.signup:
                Intent intentSignup=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intentSignup);
                break;

            case R.id.forgotpass:

                String mail=login.getText().toString();
                if(!mail.equalsIgnoreCase("")){
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "reset password email sent", Toast.LENGTH_SHORT).show();
                            }else {
                                String error=task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(this, "enter email id", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.otplogin:

                Intent intent=new Intent(LoginActivity.this,OTPActivity.class);
                startActivity(intent);
                finish();

        }
    }

    public void loginuser(String email,String password){

        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    editor.putBoolean("LOGINSTATUS",true);
                    editor.commit();
                    Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    editor.putBoolean("LOGINSTATUS",false);
                    editor.commit();
                    String error=task.getException().getMessage();

                    Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}

