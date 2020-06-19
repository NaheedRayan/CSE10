package com.example.cse10.LoginActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.cse10.Activities.MainMenu;
import com.example.cse10.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class Login extends AppCompatActivity {

    //shortform : write logt + enter
    private static final String TAG = "Login";


    //variables
    private TextInputLayout regID, regPassword;
    private CheckBox checkBox;

    //for haptic feedback
    Vibrator vibrator ;

    Button callSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*for deleting status bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //hooks
        callSignUp = findViewById(R.id.sign_up_screen);
        regID = findViewById(R.id.id);
        regPassword = findViewById(R.id.password);
        checkBox = findViewById(R.id.checkbox);

        //haptic feedback
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //starting of remember me data
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String Checkbox = sharedPreferences.getString("remember", "");
        String userid = sharedPreferences.getString("userid", "");
        String userpass = sharedPreferences.getString("userpass", "");
        if (Checkbox.equals("true")) {

            regID.getEditText().setText(userid.trim());
            regPassword.getEditText().setText(userpass.trim());
            //passing the data to is user for validation
            isUser();

        } else if (Checkbox.equals("false")) {
            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
        }

        //when the checkbox is clicked
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.putString("userid", regID.getEditText().getText().toString().trim());
                    editor.putString("userpass", regPassword.getEditText().getText().toString().trim());
                    editor.apply();
                    Toast.makeText(Login.this, "Checked", Toast.LENGTH_SHORT).show();

                } else if (!buttonView.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(Login.this, "UnChecked", Toast.LENGTH_SHORT).show();


                }
            }
        });


        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(70);
                finish();
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                //finish();
                //return;
            }
        });


    }

    //for validating username
    private boolean validateUserID() {
        String val = regID.getEditText().getText().toString();
        String ID_pattern = "[B]{1}[0-9]{9}";

        if (val.isEmpty()) {
            regID.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(ID_pattern)) {
            regID.setError("Invalid ID Format");
            return false;
        } else {
            regID.setError(null);
            regID.setErrorEnabled(false);
            return true;
        }

    }

    //for validating user password
    private boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }


    }


    //for clicking the login button
    public void loginUser(View view) {

        vibrator.vibrate(70);
        //validate login info
        if (!validatePassword() | !validateUserID())
            return;
        else {
            isUser();

        }

    }

    private void isUser() {
        final String userEnteredID = regID.getEditText().getText().toString().trim();
        final String userEnteredPassword = regPassword.getEditText().getText().toString().trim();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("StudentId").document(userEnteredID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //for handling any exception
                if (e != null) {
                    Toast.makeText(Login.this, "Error while loading", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    return;
                }


                if (documentSnapshot.exists()) {
                    String val = documentSnapshot.getString("password");//getting the password field
                    String batch = documentSnapshot.getString("batch");//getting the batch field

                    if (val.equals(userEnteredPassword)) {
                        Toast.makeText(Login.this, "Welcome", Toast.LENGTH_SHORT).show();
                        //going to the next screen
                        Intent intent = new Intent(Login.this, MainMenu.class);//calling the Login menu
                        intent.putExtra("ID", userEnteredID);//also passing the item to other activity
                        intent.putExtra("BATCH", batch);//also passing the item to other activity
                        finish();
                        startActivity(intent);
                        //finish();
                        //return;
                    } else {
                        Toast.makeText(Login.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                        //return;
                    }

                } else {
                    Toast.makeText(Login.this, "Sorry Invalid ID", Toast.LENGTH_SHORT).show();
                    //return;
                }


            }


        });


    }
}
