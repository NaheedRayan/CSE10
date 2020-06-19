package com.example.cse10.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cse10.R;
import com.example.cse10.Utils;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;

import javax.annotation.Nullable;

public class ClassRoutine extends AppCompatActivity {

    private String DAY;

    TextView sub8;
    TextView sub10;
    TextView sub11;
    TextView sub12;
    TextView sub01;
    TextView sub02;

    TextView room8;
    TextView room10;
    TextView room11;
    TextView room12;
    TextView room01;
    TextView room02;


    TextView Teacher8;
    TextView Teacher10;
    TextView Teacher11;
    TextView Teacher12;
    TextView Teacher01;
    TextView Teacher02;

    Button button_full_routine;

    private String userId, userBatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_routine);

        //for changing the status bar color
        // Pass on the activity and color resourse
        Utils.darkenStatusBar(this, R.color.LightGreen);

        //getting the id
        Intent intent = getIntent();
        userId = intent.getStringExtra("ID");
        userBatch = intent.getStringExtra("BATCH");
        //Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();


        //hooks
        sub8 = findViewById(R.id.sub8);
        sub10 = findViewById(R.id.sub10);
        sub11 = findViewById(R.id.sub11);
        sub12 = findViewById(R.id.sub12);
        sub01 = findViewById(R.id.sub01);
        sub02 = findViewById(R.id.sub02);

        room8 = findViewById(R.id.room8);
        room10 = findViewById(R.id.room10);
        room11 = findViewById(R.id.room11);
        room12 = findViewById(R.id.room12);
        room01 = findViewById(R.id.room01);
        room02 = findViewById(R.id.room02);

        Teacher8 = findViewById(R.id.Teacher8);
        Teacher10 = findViewById(R.id.Teacher10);
        Teacher11 = findViewById(R.id.Teacher11);
        Teacher12 = findViewById(R.id.Teacher12);
        Teacher01 = findViewById(R.id.Teacher01);
        Teacher02 = findViewById(R.id.Teacher02);
        button_full_routine = findViewById(R.id.button);


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        //getting the day ;
        switch (day) {
            case Calendar.SUNDAY:
                DAY = "Sunday";
                break;
            case Calendar.MONDAY:
                DAY = "Monday";
                break;
            case Calendar.TUESDAY:
                DAY = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                DAY = "Wednesday";
                break;
            case Calendar.THURSDAY:
                DAY = "Thursday";
                break;
            case Calendar.FRIDAY:
                DAY = "Friday";
                break;
            case Calendar.SATURDAY:
                DAY = "Saturday";
                break;
        }

        String collection_name = "ClassRoutine";
        if (userBatch.trim().equals("11"))
            collection_name = "ClassRoutine11";

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection(collection_name).document(DAY);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                //getting the data for splitting
                String data8 = documentSnapshot.getString("08:30-10:00");
                String data10 = documentSnapshot.getString("10:00-11:00");
                String data11 = documentSnapshot.getString("11:00-12:00");
                String data12 = documentSnapshot.getString("12:00-01:00");
                String data01 = documentSnapshot.getString("01:30-02:30");
                String data02 = documentSnapshot.getString("02:30-03:30");


                //splitting the data into 3 parts using split
                String[] arrOfStr8 = data8.split("@", 3);
                String[] arrOfStr10 = data10.split("@", 3);
                String[] arrOfStr11 = data11.split("@", 3);
                String[] arrOfStr12 = data12.split("@", 3);
                String[] arrOfStr01 = data01.split("@", 3);
                String[] arrOfStr02 = data02.split("@", 3);


                //setting the data

                sub8.setText(arrOfStr8[0].trim());
                sub10.setText(arrOfStr10[0].trim());
                sub11.setText(arrOfStr11[0].trim());
                sub12.setText(arrOfStr12[0].trim());
                sub01.setText(arrOfStr01[0].trim());
                sub02.setText(arrOfStr02[0].trim());

                Teacher8.setText(arrOfStr8[1].trim());
                Teacher10.setText(arrOfStr10[1].trim());
                Teacher11.setText(arrOfStr11[1].trim());
                Teacher12.setText(arrOfStr12[1].trim());
                Teacher01.setText(arrOfStr01[1].trim());
                Teacher02.setText(arrOfStr02[1].trim());


                room8.setText(arrOfStr8[2].trim());
                room10.setText(arrOfStr10[2].trim());
                room11.setText(arrOfStr11[2].trim());
                room12.setText(arrOfStr12[2].trim());
                room01.setText(arrOfStr01[2].trim());
                room02.setText(arrOfStr02[2].trim());


            }
        });

        //when full routine button is clicked
        button_full_routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassRoutine.this, FullRoutine.class);
                intent.putExtra("ID", userId);
                intent.putExtra("BATCH", userBatch);
                startActivity(intent);
            }
        });


    }
}
