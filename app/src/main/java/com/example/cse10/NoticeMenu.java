package com.example.cse10;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;



public class NoticeMenu extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mCollectionRef;

    private FirebaseStorage firebaseStorage;

    private NoticeAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<Notice> mNotice;

    //for progress bar circle
    private ProgressBar mProgressCircle;

    //for floating button
    FloatingActionButton button_add_notice;

    //for storing ID and Batch
    private String userId;
    private String userBatch;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_menu);

        //for changing the status bar color
        // Pass on the activity and color resourse
        Utils.darkenStatusBar(this, R.color.DarkGreen);

        //getting the id
        Intent intent = getIntent();
        userId = intent.getStringExtra("ID");
        userBatch = intent.getStringExtra("BATCH");
        //Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();


        settingRecyclerView();

        //hooks for floating button
        button_add_notice = findViewById(R.id.button_add_notice);

        //if the add button is clicked
        button_add_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoticeMenu.this, AddNotice.class);
                intent.putExtra("ID", userId);
                intent.putExtra("BATCH", userBatch);
                startActivity(intent);
            }
        });

    }

    private void settingRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(false);

        //some extra tweak for performance
        mRecyclerView.setItemViewCacheSize(25);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //for loading progressbar
        mProgressCircle = findViewById(R.id.progress_circle);

        mNotice = new ArrayList<>();
        firebaseStorage = FirebaseStorage.getInstance();


        String collection_name = "NoticeArchive" ;
        if(userBatch.trim().equals("11"))
            collection_name = "NoticeArchive11" ;

        mCollectionRef = FirebaseFirestore.getInstance().collection(collection_name);


        //by adding orderby function before snapshot listener enables the list to be sorted
        mCollectionRef.orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                //for refreshing
                mNotice.clear();
                //passing snapshots one by one for setting it to Notice class
                //then it will be passed to the adapter in the adapter list
                for (DocumentSnapshot postDocumentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    Notice notice = postDocumentSnapshot.toObject(Notice.class);
                    //setting the key(document name) for delete method
                    notice.setKey(notice.getTimestamp());


                    mNotice.add(notice);


                }

                //passing the context and list
                mAdapter = new NoticeAdapter(NoticeMenu.this, mNotice);

                mRecyclerView.setAdapter(mAdapter);

                //for the floating button to disappear when scrolling
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (dy > 0 && button_add_notice.getVisibility() == View.VISIBLE) {
                            button_add_notice.hide();
                        } else if (dy < 0 && button_add_notice.getVisibility() != View.VISIBLE) {
                            button_add_notice.show();
                        }
                    }
                });


                //adding listener with adapter
                mAdapter.setOnItemClickListener(new NoticeAdapter.onItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        //getting the text from the selected position
                        String text = mNotice.get(position).getUsername();
                        //Toast.makeText(NoticeMenu.this, text + " is selected in position " + position, Toast.LENGTH_SHORT).show();

                        //ImageView imageView = findViewById(R.id.image_view);
                        //PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
                        //attacher.update();

                    }

                    @Override
                    public void onEdit(int position) {
                        Toast.makeText(NoticeMenu.this, "This feature will soon be available", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDelete(int position) {
                        //Toast.makeText(NoticeMenu.this, "onDelete is selected", Toast.LENGTH_SHORT).show();
                        Notice selectedItem = mNotice.get(position);
                        String User = selectedItem.getId().trim();
                        final String key = selectedItem.getKey();
                        String tempUrl = selectedItem.getImageurl().trim();

                        //for administrative power
                        if(userId.equals("B180305041"))
                            Toast.makeText(NoticeMenu.this, "Administrative Power", Toast.LENGTH_SHORT).show();
                        //for giving access to valid user
                        else if (!userId.equals(User)) {
                            Toast.makeText(NoticeMenu.this, "Access Denied", Toast.LENGTH_SHORT).show();
                            return;
                        }



                        if (tempUrl.equals("")) {
                            //if it does not contain image
                            mCollectionRef.document(key).delete();
                            Toast.makeText(NoticeMenu.this, "Post deleted", Toast.LENGTH_SHORT).show();

                        } else {
                            //if it contains image then we will get reference from image
                            StorageReference storageReference = firebaseStorage.getReferenceFromUrl(selectedItem.getImageurl());
                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mCollectionRef.document(key).delete();
                                    Toast.makeText(NoticeMenu.this, "Post deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
                //for progress circle
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }


}
