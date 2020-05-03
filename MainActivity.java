package com.example.firebasenoteapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements UdpateNotes{

    @BindView(R.id.toolbar3)
    Toolbar toolbar3;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.floatingActionButton3)
    FloatingActionButton floatingActionButton3;
    @BindView(R.id.addallnotesrecycler)
    RecyclerView addallnotesrecycler;


    ArrayList<UserNotes> allnotes = new ArrayList<>();

    DatabaseReference databasenotes;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    LinearLayoutManager linearLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar3.setTitle("FireNotes");
        toolbar3.setTitleTextColor(Color.WHITE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Reading notes");

        sharedPreferences = getSharedPreferences("FIRENOTDATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        String uid = sharedPreferences.getString("UID", "");
        databasenotes = FirebaseDatabase.getInstance().getReference("USERNOTES").child(uid);

        linearLayoutManager = new LinearLayoutManager(this);
        addallnotesrecycler.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        readnotes();
    }

    @OnClick(R.id.floatingActionButton3)
    public void onViewClicked() {

        Intent intent = new Intent(MainActivity.this, Addnote.class);
        startActivity(intent);

    }

    public void readnotes() {
        allnotes.clear();
        progressDialog.show();

        databasenotes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    UserNotes userNotes = snapshot.getValue(UserNotes.class);
                    allnotes.add(userNotes);
                }
                progressDialog.dismiss();
                Notesadapter notesadapter=new Notesadapter(MainActivity.this,allnotes);
                addallnotesrecycler.setAdapter(notesadapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void updatenotes(UserNotes userNotes) {
        databasenotes.child(userNotes.getNoteid()).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    readnotes();
                }
            }
        });
    }

    @Override
    public void delete(UserNotes userNotes) {
        databasenotes.child(userNotes.getNoteid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "note deleted successfully", Toast.LENGTH_SHORT).show();
                    readnotes();
                }
            }
        });
    }
}
