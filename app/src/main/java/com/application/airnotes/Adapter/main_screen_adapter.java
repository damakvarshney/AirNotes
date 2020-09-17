package com.application.airnotes.Adapter;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.application.airnotes.Data.UserNotes;
import com.application.airnotes.R;
import com.application.airnotes.main_screen;
import com.application.airnotes.addNote_screen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class main_screen_adapter extends RecyclerView.Adapter<main_screen_adapter.myholder> {
    Context context;
    ArrayList<UserNotes> arrayList;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid = currentUser.getUid();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid).child("user_notes");

    public main_screen_adapter(Context context, ArrayList<UserNotes> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.adapter_for_recyclerview, parent, false);
        myholder holder = new myholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        final UserNotes userNotes = arrayList.get(position);
        holder.title.setText(userNotes.getNoteTitle());
        holder.desc.setText(userNotes.getNoteDesc());
        holder.date.setText(userNotes.getNoteDate());

        final String user_note_id = userNotes.getNoteId();
        final String user_title = userNotes.getNoteTitle();
        final String user_desc = userNotes.getNoteDesc();

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,addNote_screen.class);
                intent.putExtra("TITLE",user_title);
                intent.putExtra("DESCRIPTION",user_desc);
                intent.putExtra("NOTE_ID",user_note_id);
                context.startActivity(intent);


            }
        });


        holder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Delete this Note");
                builder.setMessage("Are you sure?");
                builder.setCancelable(true);
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        databaseReference.child(user_note_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Your Note has been deleted.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, main_screen.class);
                                    context.startActivity(intent);
                                    ((Activity)context).finish();
                                } else {
                                    Toast.makeText(context, "Deletion Unsuccessful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context,main_screen.class);
                                    context.startActivity(intent);
                                    ((Activity)context).finish();
                                }
                            }
                        });

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });

    }




    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class myholder extends ViewHolder {

        TextView title, desc, date;
        View root;


        public myholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_received);
            desc = itemView.findViewById(R.id.desc_received);
            date = itemView.findViewById(R.id.date_received);

            root = itemView.findViewById(R.id.root);
        }
    }


   




}
