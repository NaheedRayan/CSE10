package com.example.cse10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //variable for changing duration
    private static  int SPLASH_SCREEN = 2300;

    //variables for animation
    Animation topAnim , bottomAnim ;

    //Other variables
    ImageView image ;
    TextView logo ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*for deleting status bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.activity_main);

        //for hooking animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //hooks
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView) ;

        //for setting the animation for splash screen
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);


        //for going to the next screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,Login.class);
                startActivity(intent);
                finish();//finishing it because splash screen should be shown only once

            }
        },SPLASH_SCREEN);//for timer

    }
}
