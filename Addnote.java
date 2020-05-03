package com.example.firebasenoteapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Addnote extends AppCompatActivity {

    @BindView(R.id.toolbar_addnote)
    Toolbar toolbarAddnote;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.button)
    Button button;

    DatabaseReference databasenotes;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        ButterKnife.bind(this);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("saving notes");

        toolbarAddnote.setTitle("ADD NOTE");
        toolbarAddnote.setTitleTextColor(Color.WHITE);
        toolbarAddnote.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        sharedPreferences=getSharedPreferences("FIRENOTDATA", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();


        String uid=sharedPreferences.getString("UID","");
        databasenotes= FirebaseDatabase.getInstance().getReference("USERNOTES").child(uid);

        toolbarAddnote.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.button)
    public void onViewClicked() {

        String title=editText.getText().toString();
        String desc=editText2.getText().toString();

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd mmmm yyyy");
        Calendar calendar=Calendar.getInstance();
        String today=simpleDateFormat.format(calendar.getTime());

        if(!title.equalsIgnoreCase("")){
            if(!desc.equalsIgnoreCase("")) {
                progressDialog.show();

                String key=databasenotes.push().getKey();
                UserNotes userNotes=new UserNotes(title,desc,today,key);
                databasenotes.child(key).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            Toast.makeText(Addnote.this, "Note saved", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(Addnote.this, "Error saving notes", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
             }

                else{
                    Toast.makeText(this, "please enter description", Toast.LENGTH_LONG).show();
                }


        }else{
            Toast.makeText(this, "please enter title", Toast.LENGTH_LONG).show();

        }

    }
}
