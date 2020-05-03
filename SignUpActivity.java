package com.example.firebasenoteapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.Name)
    EditText Name;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.signpassword)
    EditText signpassword;
    @BindView(R.id.signupbtn)
    Button signupbtn;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseUsers;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        sharedPreferences=getSharedPreferences("FIRENOTDATA", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        databaseUsers= FirebaseDatabase.getInstance().getReference("USERS");
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        if(getIntent().hasExtra("MOBILE")){
            signpassword.setVisibility(View.GONE);
        }else{

            signpassword.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.signupbtn)
    public void onViewClicked() {

        String name=Name.getText().toString();
        String Email=email.getText().toString();
        String password="";

        if(getIntent().hasExtra("MOBILE")){
            if(!name.equalsIgnoreCase("")){
                if(!Email.equalsIgnoreCase("")){

                    String mobile=getIntent().getExtras().getString("MOBILE");
                    String uid=getIntent().getExtras().getString("UID");

                    UserInfo userInfo=new UserInfo() {
                        @NonNull
                        @Override
                        public String getUid() {
                            return uid;
                        }

                        @NonNull
                        @Override
                        public String getProviderId() {
                            return null;
                        }

                        @Nullable
                        @Override
                        public String getDisplayName() {
                            return name;
                        }

                        @Nullable
                        @Override
                        public Uri getPhotoUrl() {
                            return null;
                        }

                        @Nullable
                        @Override
                        public String getEmail() {
                            return Email;
                        }

                        @Nullable
                        @Override
                        public String getPhoneNumber() {
                            return mobile;
                        }

                        @Override
                        public boolean isEmailVerified() {
                            return false;
                        }
                    };

                    databaseUsers.child(uid).setValue(userInfo).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                editor.putString("UID",uid);
                                editor.commit();
                                Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(SignUpActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                            }
                            
                        }
                    });

                }
            }

        }else {
            password=signpassword.getText().toString();

            registerUser(name,password,Email);
        }


    }

    public void registerUser(String name,String password,String email){

        if(!name.equalsIgnoreCase("")){
            if(!email.equalsIgnoreCase("")){
                if(!password.equalsIgnoreCase("")){

                    progressDialog.setMessage("registering");
                    progressDialog.show();

                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){

                                FirebaseUser user=firebaseAuth.getCurrentUser();

                                String uid=user.getUid();

                                UserInfo userInfo=new UserInfo() {
                                    @NonNull
                                    @Override
                                    public String getUid() {
                                        return uid;
                                    }

                                    @NonNull
                                    @Override
                                    public String getProviderId() {
                                        return null;
                                    }

                                    @Nullable
                                    @Override
                                    public String getDisplayName() {
                                        return name;
                                    }

                                    @Nullable
                                    @Override
                                    public Uri getPhotoUrl() {
                                        return null;
                                    }

                                    @Nullable
                                    @Override
                                    public String getEmail() {
                                        return email;
                                    }

                                    @Nullable
                                    @Override
                                    public String getPhoneNumber() {
                                        return null;
                                    }

                                    @Override
                                    public boolean isEmailVerified() {
                                        return false;
                                    }
                                };


                                databaseUsers.child(uid).setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            editor.putString("UID",uid);
                                            editor.commit();
                                            Toast.makeText(SignUpActivity.this, "User successfully registered", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                    }
                                });


                            }else {
                                Toast.makeText(SignUpActivity.this, "Error registered user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(this,"please enter password",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this,"please enter email",Toast.LENGTH_SHORT).show();

            }
        }else {
            Toast.makeText(this,"please enter name",Toast.LENGTH_SHORT).show();
        }


    }
}
