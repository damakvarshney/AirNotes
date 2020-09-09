package com.application.airnotes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.application.airnotes.Data.UserNotes;
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
    ArrayList<UserNotes> arrayList = new ArrayList<>();
    
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
        final String user_date = userNotes.getNoteDate();

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(user_note_id,user_title,user_desc,user_date);


            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(user_note_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Your Note has been deleted.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context,main_screen.class);
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

    }

    private void showDialog(final String user_note_id, String user_title, String user_desc, String user_date) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_add_note_screen);
        final SpotsDialog progressDialog=new SpotsDialog(context, R.style.custom_progressDialog);

        final EditText mEditTextTitle = dialog.findViewById(R.id.editTextTitle);
        mEditTextTitle.setText(user_title);
        final EditText mEditTextDescription = dialog.findViewById(R.id.editTextDescription);
        mEditTextDescription.setText(user_desc);
        Button mRegisterBtn = dialog.findViewById(R.id.register_btn);

        dialog.show();

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String context_title = mEditTextTitle.getText().toString();
                String context_desc = mEditTextDescription.getText().toString();


                if(!context_title.equalsIgnoreCase("")){
                    if(!context_desc.equalsIgnoreCase("")){
                        update_notes_to_database(user_note_id,context_title,context_desc);

                    }else {
                        Toast.makeText(dialog.getContext(), "Enter the Description", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(dialog.getContext(), "Enter the Title", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void update_notes_to_database( final String user_note_id, final String context_title, final String context_desc) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar calendar = Calendar.getInstance();
        final String todays_date = simpleDateFormat.format(calendar.getTime());


        databaseReference.child(user_note_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    UserNotes userNotes = new UserNotes(user_note_id,todays_date,context_title,context_desc);
                    databaseReference.child(user_note_id).setValue(userNotes).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(context, "Your Notes has been Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context,main_screen.class);
                                context.startActivity(intent);
                                ((Activity)context).finish();

                            }
                            else {

                                Intent intent = new Intent(context,main_screen.class);
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }
                        }
                    });
                }
                else {

                    Intent intent = new Intent(context,main_screen.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class myholder extends ViewHolder {

        TextView title, desc, date;
        ImageView edit, delete;

        public myholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_received);
            desc = itemView.findViewById(R.id.desc_received);
            date = itemView.findViewById(R.id.date_received);
            edit = itemView.findViewById(R.id.edit_image);
            delete = itemView.findViewById(R.id.delete_image);
        }
    }


   




}
