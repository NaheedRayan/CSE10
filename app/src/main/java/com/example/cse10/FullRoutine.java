package com.example.cse10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import uk.co.senab.photoview.PhotoViewAttacher;

public class FullRoutine extends AppCompatActivity {

    private ImageView imageView ;
    private String userId,userBatch ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_routine);

        //for changing the status bar color
        // Pass on the activity and color resourse
        Utils.darkenStatusBar(this, R.color.LightGreen);

        //getting the id
        Intent intent = getIntent();
        userId = intent.getStringExtra("ID");
        userBatch = intent.getStringExtra("BATCH");
        //Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();

        imageView = findViewById(R.id.full_routine) ;


        String document_name = "CSE10" ;
        if(userBatch.trim().equals("11"))
            document_name = "CSE11" ;


        //progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Toast.makeText(this, "Please wait while the image is being loaded", Toast.LENGTH_SHORT).show();
        db.collection("ClassRoutineImages").document(document_name).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String url = documentSnapshot.getString("imageurl");
                //Toast.makeText(FullRoutine.this, "Please wait while the image is loading...", Toast.LENGTH_SHORT).show();
                Picasso.with(FullRoutine.this)
                        .load(url)
                        .fit()
                        .centerInside()
                        .into(imageView);

                // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
                PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);

            }
        });

    }
}
