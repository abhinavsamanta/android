package com.example.firebasenoteapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OTPActivity extends AppCompatActivity {

    String number = "";

    PhoneAuthProvider.OnVerificationStateChangedCallbacks onVerificationStateChangedCallbacks;
    FirebaseAuth firebaseAuth;

    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.opt)
    Button opt;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        onVerificationStateChangedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signinwithmobile(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };


    }

    @OnClick(R.id.opt)
    public void onViewClicked() {
        number="+91"+ mobile.getText().toString();

        if(!number.equalsIgnoreCase("")){

            verifynumber(number);


        }else {
            Toast.makeText(this, "enter valid mobile number", Toast.LENGTH_SHORT).show();
        }


    }


    public void verifynumber(String mobilenumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobilenumber, 60, TimeUnit.SECONDS, this, onVerificationStateChangedCallbacks);
    }

    public void signinwithmobile(AuthCredential phoneAuthProvider) {
        progressDialog.setMessage("Verifying number...");
        progressDialog.show();


        firebaseAuth.signInWithCredential(phoneAuthProvider).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(OTPActivity.this, "verified successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OTPActivity.this, SignUpActivity.class);
                    intent.putExtra("MOBILE", number);
                    FirebaseUser user = task.getResult().getUser();
                    String uid = user.getUid();
                    intent.putExtra("UID", uid);

                    startActivity(intent);

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(OTPActivity.this, "error with otp", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
