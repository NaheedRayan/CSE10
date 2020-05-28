package com.example.cse10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddNotice extends Activity {

    //it will extend activity to work

    private static final int PICK_IMAGE_REQUEST = 1;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("NoticeArchive");
    private CollectionReference mCollectionRef = db.collection("NoticeArchive");


    //for the id
    private String userID;
    private String userBatch;
    private String username;
    private String timestamp;


    //variable
    private Button button_choose_image;
    private Button post_button;
    private ImageView imageView;
    private EditText text_field;
    private ProgressBar mProgressBar;

    //for haptic feedback
    private Vibrator vibrator ;

    //setting variable for not clicking a button multiple times
    StorageTask uploadTask;


    //for image ;
    private Uri mImageUri;
    private Bitmap bitmap ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);


        //for changing the status bar color
        // Pass on the activity and color resourse
        Utils.darkenStatusBar(this, R.color.DarkGreen);

        //getting the id
        Intent intent = getIntent();
        userID = intent.getStringExtra("ID");
        userBatch = intent.getStringExtra("BATCH");
        //Toast.makeText(this, userID, Toast.LENGTH_SHORT).show();


        //for getting timestamp
        timestamp = (DateFormat.format("yyyy-MM-dd  HH:mm:ss", new java.util.Date()).toString());
        //for getting timestamp for document name because it does not support '/' character.

        //for haptic feedback
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);



        //hooks
        button_choose_image = findViewById(R.id.button_choose_image);
        imageView = findViewById(R.id.image_view);
        text_field = findViewById(R.id.text_input_field);
        post_button = findViewById(R.id.post_button);
        mProgressBar = findViewById(R.id.progress_bar);


        db.collection("StudentId").document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                username = documentSnapshot.getString("username").trim();
            }
        });


        //when the choose image button is clicked
        button_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for haptic feedback
                vibrator.vibrate(70);

                openFileChooser();
            }
        });

        //when post button is clicked;
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //for haptic feedback
                vibrator.vibrate(70);

                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(AddNotice.this, "Uploading in progress", Toast.LENGTH_SHORT).show();
                } else
                    uploadFile();

            }
        });

    }

    //for opening file chooser
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //for identifying the request from openFileChooser
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();//setting the image uri for later upload
            //for converting it into bitmap for compression
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Picasso.with(this)
                    .load(mImageUri)
                    .fit()
                    .centerInside()
                    .into(imageView);//for loading the image in image view
        }

    }

    //this will give the extension(eg jpeg --> jpg)
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadFile() {

        //for different batches the collection name is different
        String collection_name = "NoticeArchive" ;
        if(userBatch.trim().equals("11"))
            collection_name = "NoticeArchive11" ;

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(collection_name);
        final CollectionReference mCollectionRef = db.collection(collection_name);

        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            //for compressing
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //here you can choose quality factor in third parameter(ex. i choosen 25)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] fileInBytes = baos.toByteArray();


            Toast.makeText(this, "Your file is Uploading ,Please Wait", Toast.LENGTH_LONG).show();
            uploadTask = fileReference.putBytes(fileInBytes)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //for delaying the progress bar
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 4000);



                            //for getting the url
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(AddNotice.this, "Upload Succesfull", Toast.LENGTH_SHORT).show();
                                    Uri downloadUrl = uri;

                                    //we will pass the data in notice and save there and upload it
                                    Notice notice = new Notice(username, userID, text_field.getText().toString().trim(), downloadUrl.toString().trim(), timestamp.trim());

                                    //naming the document according to timestamp
                                    mCollectionRef.document(timestamp).set(notice);
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(AddNotice.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double prgress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) prgress);
                        }
                    });
        } else {
            //if the image url is empty
            if(text_field.getText().toString().equals(""))
            {
                Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            //we will pass the data in notice and save there and upload it
            Notice notice = new Notice(username, userID, text_field.getText().toString().trim(), "", timestamp.trim());
            mCollectionRef.document(timestamp).set(notice);
            Toast.makeText(this, "Upload successful", Toast.LENGTH_SHORT).show();
        }

    }


}
