package com.example.firebasenoteapp;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Notesadapter extends RecyclerView.Adapter<Notesadapter.Notesholder> {

    Context context;
    ArrayList<UserNotes> arrayList = new ArrayList<>();

    String noteid="",notedate="";

    UdpateNotes udpateNotes;


    public Notesadapter(Context con, ArrayList<UserNotes> arr) {
        context = con;
        arrayList = arr;

        udpateNotes=(UdpateNotes)context;
    }


    @NonNull
    @Override
    public Notesholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_recycler, parent, false);
        Notesholder notesholder = new Notesholder(view);

        return notesholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Notesholder holder, int position) {
        UserNotes userNotes=arrayList.get(position);

        String title=userNotes.getNotetitile();
        String desc=userNotes.getNotedesc();
        String date=userNotes.getNotedate();

        holder.textTitle.setText(title);
        holder.textDesc.setText(desc);
        holder.textDate.setText(date);

        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNotes userNotes1=arrayList.get(position);
                updatenote(userNotes1);
            }
        });

        holder.imageView2Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNotes userNotes1=arrayList.get(position);
                deletenote(userNotes1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Notesholder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title)
        TextView textTitle;
        @BindView(R.id.text_desc)
        TextView textDesc;
        @BindView(R.id.text_date)
        TextView textDate;
        @BindView(R.id.imageView_edit)
        ImageView imageViewEdit;
        @BindView(R.id.imageView2_delete)
        ImageView imageView2Delete;

        public Notesholder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void updatenote(UserNotes noteob){

        Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_dialog);
        dialog.show();

        EditText etitle=(EditText)dialog.findViewById(R.id.updatetitle);
        EditText edec=(EditText)dialog.findViewById(R.id.updatedesc);

        Button ebtn=(Button) dialog.findViewById(R.id.updatenotte);

            etitle.setText(noteob.notetitile);
            edec.setText(noteob.notedesc);

            noteid=noteob.getNoteid();

        ebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd mmmm yyyy");
                Calendar calendar=Calendar.getInstance();
                notedate=simpleDateFormat.format(calendar.getTime());

                String title=etitle.getText().toString();
                String desc=edec.getText().toString();

                UserNotes userNotes=new UserNotes(title,desc,notedate,noteid);
                udpateNotes.updatenotes(userNotes);
            }
        });
    }

    public void deletenote(final UserNotes userNotes){
            final Dialog dialog=new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.delete_note);
            dialog.show();

            TextView textyes=(TextView)dialog.findViewById(R.id.tv_yes);
            TextView textno=(TextView)dialog.findViewById(R.id.tv_no);

            textno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            textyes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    udpateNotes.delete(userNotes);
                }
            });

    }
}
