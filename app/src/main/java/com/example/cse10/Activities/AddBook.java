package com.example.cse10.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse10.ModelClass.BookArchive;
import com.example.cse10.R;
import com.example.cse10.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;


public class AddBook extends AppCompatActivity {

    //for userid
    private String userId;
    private String userBatch;
    private String username;
    private String timestamp;
    //private String link;
    //private String bookName;
    //variables
    private TextInputLayout book_name, book_link;
    private Button button ;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        //getting the id
        Intent intent = getIntent();
        userId = intent.getStringExtra("ID");
        userBatch = intent.getStringExtra("BATCH");
        //Toast.makeText(this, userBatch, Toast.LENGTH_SHORT).show();

        //for action bar
        Toolbar toolbar = findViewById(R.id.add_book_toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        textView.setText("Add books Link");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //for the back arrow


        //for changing the status bar color
        // Pass on the activity and color resourse
        Utils.darkenStatusBar(this, R.color.LightGreen);

        //for getting timestamp
        timestamp = (DateFormat.format("yyyy-MM-dd  HH:mm:ss", new java.util.Date()).toString());


        //hooks
        book_name = findViewById(R.id.textView_book_name);
        book_link = findViewById(R.id.textView_Google_drive_link);
        button = findViewById(R.id.share_book_link);

        //regID.getEditText().getText().toString().trim()



        //for getting the id
        db.collection("StudentId").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                username = documentSnapshot.getString("username").trim();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(book_name.getEditText().getText().toString().trim().equals("")&&book_name.getEditText().getText().toString().trim().equals(""))
                {
                    Toast.makeText(AddBook.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    //for converting the link into google download compatable
                    String link1 = book_link.getEditText().getText().toString().trim();
                    String link2 ;
                    int p1 = link1.indexOf("id=");//link from pc
                    int p2 = link1.indexOf("/d/");//link from within android
                    if(p1 == -1 && p2 == -1){
                        Toast.makeText(AddBook.this, "Invalid link", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        if(p1 != -1)
                            link2 = "https://drive.google.com/uc?export=download&"+link1.substring(p1) ;
                        else
                        {
                            //we have to cut the id and paste it
                            //and making it a downloadable link
                            int pend = link1.indexOf("/view?");
                            link2 = "https://drive.google.com/uc?export=download&id="+link1.substring(p2+3,pend) ;
                        }

                    }

                    //for different batches the collection name is different
                    String collection_name = "BookArchive" ;
                    if(userBatch.trim().equals("11"))
                        collection_name = "BookArchive11" ;


                    BookArchive bookArchive = new BookArchive(username,userId,book_name.getEditText().getText().toString().trim(),link2,timestamp) ;
                    db.collection(collection_name).document(timestamp).set(bookArchive) ;
                    Toast.makeText(AddBook.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                }
            }
        });





    }
}
