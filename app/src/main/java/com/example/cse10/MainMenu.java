package com.example.cse10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    //variable for cardview buttons
    CardView notice_button;
    CardView class_routine_button;
    CardView aboutus_button;
    CardView phonebook_button;
    CardView book_archive_button;
    CardView cgpa_button;
    CardView attendance_button;
    CardView help_button;

    //user id and batch
    private String userId;
    private String userBatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //---------------------hooks----------------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        //---------------------toolbar------------------
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CSE 10");

        //-------------------navigation drawer menu-------------------
        navigationView.bringToFront();//to resolve the issue
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //to make the menu clickable
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);//for selecting the home

        //for getting user id
        Intent intent = getIntent();
        userId = intent.getStringExtra("ID");
        userBatch = intent.getStringExtra("BATCH");
        //Toast.makeText(this, userBatch.trim(), Toast.LENGTH_SHORT).show();


        //------------hooks for menu buttons--------------
        notice_button = findViewById(R.id.notice_button);
        class_routine_button = findViewById(R.id.class_routine_button);
        aboutus_button = findViewById(R.id.aboutus_button);
        phonebook_button = findViewById(R.id.phonebook_button);
        book_archive_button = findViewById(R.id.book_archive_button);
        attendance_button = findViewById(R.id.attendance_button);
        cgpa_button = findViewById(R.id.CGPA_button);
        help_button = findViewById(R.id.help_button);


        //when notice button is clicked
        notice_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainMenu.this, "Notice button is clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainMenu.this, NoticeMenu.class);
                intent.putExtra("ID", userId);
                intent.putExtra("BATCH", userBatch);
                startActivity(intent);

            }
        });
        //when class routine button is clicked
        class_routine_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, ClassRoutine.class);
                intent.putExtra("ID", userId);
                intent.putExtra("BATCH", userBatch);
                startActivity(intent);

            }
        });

        //when aboutus is clicked
        aboutus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, AboutUs.class);
                startActivity(intent);

            }
        });

        //when phonebook button is clicked
        phonebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainMenu.this, "The feature will be available soon", Toast.LENGTH_SHORT).show();
            }
        });

        //when book archive button is clicked
        book_archive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainMenu.this, "The feature will be available soon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainMenu.this, BookArchiveMenu.class);
                intent.putExtra("ID", userId);
                intent.putExtra("BATCH", userBatch);
                startActivity(intent);

            }
        });
        //when attendance button is clicked
        attendance_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainMenu.this, "The feature will be available soon", Toast.LENGTH_SHORT).show();
            }
        });
        //when cgpa button program button is clicked
        cgpa_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainMenu.this, "The feature will be available soon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainMenu.this , CGPA_Calculator.class);
                startActivity(intent);
            }
        });
        //when help button is clicked
        help_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainMenu.this, "The feature will be available soon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainMenu.this, Help.class);
                intent.putExtra("ID", userId);
                intent.putExtra("BATCH", userBatch);
                startActivity(intent);
            }
        });

    }

    //to avoid closing the application on back pressed(nav menu)
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }


    //for selecting the navigation menu item
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                break;
            case R.id.take_note:
                Toast.makeText(this, "Take note selected", Toast.LENGTH_SHORT).show();
                break;
            //  case R.id.login:
            //  Toast.makeText(this, "login selected", Toast.LENGTH_SHORT).show();
            //  break;
            case R.id.profile:
                Toast.makeText(this, "profile selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainMenu.this, Profile.class);
                intent.putExtra("ID", userId);
                intent.putExtra("BATCH", userBatch);
                startActivity(intent);

                break;
            case R.id.logout:

                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.apply();
                Toast.makeText(MainMenu.this, "Log Out Successful", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(MainMenu.this, Login.class);
                startActivity(intent1);
                finish();

                break;
            case R.id.share:
                Toast.makeText(this, "share selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.aboutus:
                //Toast.makeText(this, "aboutus selected", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MainMenu.this, AboutUs.class);
                startActivity(intent2);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
