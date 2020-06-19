package com.example.cse10.LoginActivities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse10.Activities.MainMenu;
import com.example.cse10.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class SplashScreen extends AppCompatActivity {

    //variable for changing duration
    private static int SPLASH_SCREEN = 2300;

    //variables for animation
    Animation topAnim, bottomAnim;

    //Other variables
    ImageView image;
    TextView logo;

    private String userid;
    private String userpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*for deleting status bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //for hooking animation
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //hooks
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);

        //for setting the animation for splash screen
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);


        //for going to the next screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //starting of remember me data
                SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                String Checkbox = sharedPreferences.getString("remember", "");
                userid = sharedPreferences.getString("userid", "");
                userpass = sharedPreferences.getString("userpass", "");
                if (Checkbox.equals("true")) {
                    //passing the data to is user for validation
                    isUser();

                } else if (Checkbox.equals("false")) {
                    //Toast.makeText(SplashScreen.this, "Please sign in", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                    finish();//finishing it because splash screen should be shown only once
                }
            }
        }, SPLASH_SCREEN);//for timer

    }

    private void isUser() {
        final String userEnteredID = userid;
        final String userEnteredPassword = userpass;

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("StudentId").document(userEnteredID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //for handling any exception
                if (e != null) {
                    Toast.makeText(SplashScreen.this, "Error while loading", Toast.LENGTH_SHORT).show();
                    //Log.d(TAG, e.toString());
                    return;
                }


                if (documentSnapshot.exists()) {
                    String val = documentSnapshot.getString("password");//getting the password field
                    String batch = documentSnapshot.getString("batch");//getting the batch field

                    if (val.equals(userEnteredPassword)) {
                        Toast.makeText(SplashScreen.this, "Welcome", Toast.LENGTH_SHORT).show();
                        //going to the next screen
                        Intent intent = new Intent(SplashScreen.this, MainMenu.class);//calling the Login menu
                        intent.putExtra("ID", userEnteredID);//also passing the item to other activity
                        intent.putExtra("BATCH", batch);//also passing the item to other activity

                        //for deleting the flash during transition
                        getWindow().setExitTransition(null);

                        finish();
                        startActivity(intent);
                        //finish();
                        //return;
                    } else {
                        Toast.makeText(SplashScreen.this, "Auto login Unsuccessful \n Invalid Password", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashScreen.this, Login.class);//calling the Login menu
                        finish();
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(SplashScreen.this, "Auto login Unsuccessful\n Sorry Invalid ID", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SplashScreen.this, Login.class);//calling the Login menu
                    finish();
                    startActivity(intent);
                    //return;
                }


            }


        });


    }
}
