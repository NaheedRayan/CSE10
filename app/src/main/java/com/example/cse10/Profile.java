package com.example.cse10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class Profile extends AppCompatActivity {

    //shortform : write logt + enter
    private static final String TAG = "Profile";

    //user id and Batch
    private String userId;
    private String userBatch;

    //variables
    private TextInputEditText fullname , username , email, password ,phone_number ,address , social_media,batch;


    Button update ;

    TextView randomUser , randomUserId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //for changing the status bar color
        // Pass on the activity and color resourse
        Utils.darkenStatusBar(this, R.color.LightGreen);

        //for getting the user id
        Intent intent = getIntent();
        userId = intent.getStringExtra("ID").trim() ;
        //Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();


        //hooks
        randomUser = findViewById(R.id.RandomUser);
        randomUserId = findViewById(R.id.RandomUserId);

        //hooks for textInputEditText layout
        fullname = findViewById(R.id.pfull_name);
        username = findViewById(R.id.pusername);
        email = findViewById(R.id.pemail);
        phone_number = findViewById(R.id.pphone_number);
        password = findViewById(R.id.ppassword);
        address = findViewById(R.id.pAddress);
        social_media = findViewById(R.id.pSocialAddress);
        batch = findViewById(R.id.pbatch_number);

        update = findViewById(R.id.update) ;

        //setting the profile
        settingProfile();





        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Profile.this, "Update complete", Toast.LENGTH_SHORT).show();

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                String temp_fullname = fullname.getText().toString();
                String temp_username = username.getText().toString();
                String temp_email = email.getText().toString();
                String temp_address = address.getText().toString();
                String temp_social_media = social_media.getText().toString();
                String temp_phone_number = phone_number.getText().toString();
                String temp_password = password.getText().toString();
                String temp_batch = batch.getText().toString();



                //we have to pass data through map
                Map<String, Object> note = new HashMap<>();
                note.put("full_name", temp_fullname);
                note.put("username", temp_username);
                note.put("email", temp_email);
                note.put("address", temp_address);
                note.put("Social_Media_Profile", temp_social_media);
                note.put("password", temp_password);
                note.put("phone_no", temp_phone_number);
                note.put("batch", temp_batch);


                db.collection("StudentId").document(userId).update(note);
                finish();





            }
        });


    }



    private void settingProfile(){


        //for reference
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("StudentId").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //for handling any exception
                if(e != null)
                {
                    Toast.makeText(Profile.this, "Error while loading", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    return;
                }
                if(documentSnapshot.exists())
                {
                    //setting the values
                    fullname.setText(documentSnapshot.getString("full_name"));
                    username.setText(documentSnapshot.getString("username"));
                    email.setText(documentSnapshot.getString("email"));
                    phone_number.setText(documentSnapshot.getString("phone_no"));
                    address.setText(documentSnapshot.getString("address"));
                    social_media.setText(documentSnapshot.getString("Social_Media_Profile"));
                    password.setText(documentSnapshot.getString("password"));
                    batch.setText(documentSnapshot.getString("batch"));

                    randomUser.setText(documentSnapshot.getString("username"));
                    randomUserId.setText(documentSnapshot.getString("id"));


                }




            }
        });

    }





}
