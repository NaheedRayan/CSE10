package com.example.cse10.LoginActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.cse10.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class SignUp extends AppCompatActivity {

    //variable for loginmenu button
    Button callLogin;

    //for haptic feedback
    Vibrator vibrator;


    //shortform : write logt + enter
    private static final String TAG = "SplashScreen";

    //key name in the map
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE_NO = "phone_no";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_ID = "id";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_BATCH = "batch";


    //variables
    private TextInputLayout regName, regUsername, regEmail, regPhone, regAddress, regID, regBatch, regPassword;
    private Button regBtn;


    //database reference
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private DocumentReference noteRef = db.collection("StudentId").document("hello");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*for deleting status bar*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //hooks
        callLogin = findViewById(R.id.login_screen);
        //haptic feedback
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        //for going back to the login screen
        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(70);
                Intent intent = new Intent(SignUp.this, Login.class);
                finish();
                startActivity(intent);
                //finish();
                return;
            }
        });


        //hooks for the field
        regName = findViewById(R.id.name);
        regUsername = findViewById(R.id.username);
        regEmail = findViewById(R.id.email);
        regPhone = findViewById(R.id.phone_number);
        regID = findViewById(R.id.id_number);
        regBatch = findViewById(R.id.batch_number);
        regAddress = findViewById(R.id.address);
        regPassword = findViewById(R.id.password);
        regBtn = findViewById(R.id.registration);


        //when registration button is clicked
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                vibrator.vibrate(70);
                if (!validateEmail() | !validateID() | !validateBatchNo() | !validateName() | !validatePassword() | !validatePhoneNumber() | !validateUsername() | !validateAddress())
                    return;

                //get all the values
                String name = regName.getEditText().getText().toString();
                String username = regUsername.getEditText().getText().toString();
                String email = regEmail.getEditText().getText().toString();
                String phone_number = regPhone.getEditText().getText().toString();
                String address = regAddress.getEditText().getText().toString();
                final String id_number = regID.getEditText().getText().toString();
                final String batch_number = regBatch.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();

                //we have to pass data through map
                final Map<String, Object> note = new HashMap<>();
                note.put(KEY_FULL_NAME, name);
                note.put(KEY_USERNAME, username);
                note.put(KEY_EMAIL, email);
                note.put(KEY_PHONE_NO, phone_number);
                note.put(KEY_ADDRESS, address);
                note.put(KEY_ID, id_number);
                note.put(KEY_BATCH, batch_number);
                note.put(KEY_PASSWORD, password);

                //some extra fields
                note.put("imageUrl", "");//for profile pic url
                note.put("Social_Media_Profile", "");


                //if the document with the same id exists in the main StudentId Collection
                db.collection("StudentId").document(id_number).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(SignUp.this, "Error while loading...", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                            return;
                        } else if (documentSnapshot.exists()) {
                            //if the document with the same id exists
                            Toast.makeText(SignUp.this, "The id belongs to someone else", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            //if the document with the same id does not exists
                            //now we have to see if he is in the authorized list
                            db.collection("StudentIdAuth").document(batch_number.trim()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        Toast.makeText(SignUp.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, e.toString());
                                        return;
                                    }

                                    //getting the data for analyzing and splitting
                                    String string_date = documentSnapshot.getString("valid_Id");
                                    String[] arrOfStr = string_date.split("@", -2);

                                    int flag = 0;
                                    //now iterating over the data and finding if the id is present in authorization list
                                    for (String a : arrOfStr) {
                                        if (id_number.equals(a.trim())) {
                                            flag = 1;
                                            break;
                                        } else {
                                            flag = 0;
                                        }
                                        //System.out.println(a);
                                    }

                                    //if present
                                    if (flag == 1) {
                                        //setting the note
                                        db.collection("StudentId").document(id_number).set(note);   //we will set the document according to the id

                                        Toast.makeText(SignUp.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUp.this, Login.class);//calling the Login menu
                                        finish();
                                        startActivity(intent);
                                        //finish();
                                        //return;
                                    } else if (flag == 0) {
                                        //if not present
                                        Toast.makeText(SignUp.this, "Access Denied", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });


                        }
                    }

                });


            }
        });


    }

    private boolean validateName() {
        String val = regName.getEditText().getText().toString();


        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);//for shrinking back the design to its initial position
            return true;
        }
    }

    private boolean validateUsername() {
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{3,20}\\z";

        if (val.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            regUsername.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpace)) {
            regUsername.setError("White spaces are not allowed");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);//for shrinking back the design to its initial position
            return true;
        }
    }

    private boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            regEmail.setError("Invalid Email Address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);//for shrinking back the design to its initial position
            return true;
        }
    }

    private boolean validatePhoneNumber() {
        String val = regPhone.getEditText().getText().toString();

        if (val.isEmpty()) {
            regPhone.setError("Field cannot be empty");
            return false;
        } else {
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);//for shrinking back the design to its initial position
            return true;
        }
    }

    private boolean validateAddress() {
        String val = regAddress.getEditText().getText().toString();

        if (val.isEmpty()) {
            regAddress.setError("Field cannot be empty");
            return false;
        } else {
            regAddress.setError(null);
            regAddress.setErrorEnabled(false);//for shrinking back the design to its initial position
            return true;
        }
    }

    private boolean validateID() {
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
            regID.setErrorEnabled(false);//for shrinking back the design to its initial position
            return true;
        }
    }

    private boolean validateBatchNo() {
        String val = regBatch.getEditText().getText().toString();
        String batch_pattern = "[1]{1}[0-1]{1}"; //setting only for 10 , 11 batch

        if (val.isEmpty()) {
            regBatch.setError("Field cannot be empty");
            return false;

        } else if (!val.matches(batch_pattern)) {
            regBatch.setError("Invalid Batch no");
            return false;
        } else {
            regBatch.setError(null);
            regBatch.setErrorEnabled(false);//for shrinking back the design to its initial position
            return true;
        }
    }

    private boolean validatePassword() {
        String val = regPassword.getEditText().getText().toString();

        if (val.isEmpty()) {
            regPassword.setError("Field cannot be empty");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);//for shrinking back the design to its initial position
            return true;
        }
    }


}
