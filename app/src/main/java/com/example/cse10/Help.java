package com.example.cse10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Help extends AppCompatActivity {

    private String userId ;
    private String timestamp ;

    private TextInputLayout feedback ;
    private Button button ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        //getting the id
        Intent intent = getIntent();
        userId = intent.getStringExtra("ID");
        //Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();

        //for action bar
        Toolbar toolbar = findViewById(R.id.help_toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        textView.setText("Help");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //for the back arrow


        //for changing the status bar color
        // Pass on the activity and color resourse
        Utils.darkenStatusBar(this, R.color.LightGreen);

        //for getting timestamp
        timestamp = (DateFormat.format("yyyy-MM-dd  HH:mm:ss", new java.util.Date()).toString());

        button = findViewById(R.id.send_feedback) ;
        feedback = findViewById(R.id.feedback) ;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //we have to pass data through map
                final Map<String, Object> note = new HashMap<>();
                note.put("userID", userId);
                note.put("feedback", feedback.getEditText().getText().toString().trim());
                note.put("timestamp",timestamp);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Feedback").document(timestamp).set(note);
                Toast.makeText(Help.this, "Feedback sent💛🧡💚", Toast.LENGTH_SHORT).show();


            }
        });


    }
}
