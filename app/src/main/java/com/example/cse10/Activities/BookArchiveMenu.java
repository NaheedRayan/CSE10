package com.example.cse10.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cse10.ModelClass.BookArchive;
import com.example.cse10.Adapters.BookArchiveAdapter;
import com.example.cse10.R;
import com.example.cse10.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookArchiveMenu extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mCollectionRef;


    private BookArchiveAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<BookArchive> mBookArchive;

    //for progress bar circle
    private ProgressBar mProgressCircle;

    //for floating button
    FloatingActionButton button_add_book;


    //for storing ID and batch
    private String userId;
    private String userBatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_archive_menu);

        //for changing the status bar color
        // Pass on the activity and color resourse
        Utils.darkenStatusBar(this, R.color.LightGreen);

        //for action bar
        Toolbar toolbar = findViewById(R.id.custom_toolbar);
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        textView.setText("Book Archive");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //for the back arrow


        //getting the id
        final Intent intent = getIntent();
        userId = intent.getStringExtra("ID");
        userBatch = intent.getStringExtra("BATCH").trim();
        //Toast.makeText(this, userId, Toast.LENGTH_SHORT).show();

        settingRecyclerView();

        //hooks for floating button
        button_add_book = findViewById(R.id.button_add_book);

        //if the add button is clicked
        button_add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(BookArchiveMenu.this, "Add book is clicked", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(BookArchiveMenu.this, AddBook.class);
                intent1.putExtra("ID", userId);
                intent1.putExtra("BATCH", userBatch);
                startActivity(intent1);
            }
        });
    }

    private void settingRecyclerView() {
        mRecyclerView = findViewById(R.id.book_archive_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //for loading progressbar
        mProgressCircle = findViewById(R.id.book_archive_progress_circle);

        mBookArchive = new ArrayList<>();
        //firebaseStorage = FirebaseStorage.getInstance();


        //for different batches the collection name is different
        String collection_name = "BookArchive" ;
        if(userBatch.trim().equals("11"))
            collection_name = "BookArchive11" ;


        mCollectionRef = FirebaseFirestore.getInstance().collection(collection_name);


        //by adding orderby function before snapshot listener enables the list to be sorted
        mCollectionRef.orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                //for refreshing
                mBookArchive.clear();
                //passing snapshots one by one for setting it to Notice class
                //then it will be passed to the adapter in the adapter list
                for (DocumentSnapshot postDocumentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    BookArchive bookArchive = postDocumentSnapshot.toObject(BookArchive.class);

                    mBookArchive.add(bookArchive);


                }

                //passing the context and list
                mAdapter = new BookArchiveAdapter(BookArchiveMenu.this, mBookArchive);

                mRecyclerView.setAdapter(mAdapter);

                //for the floating button to disappear when scrolling
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (dy > 0 && button_add_book.getVisibility() == View.VISIBLE) {
                            button_add_book.hide();
                        } else if (dy < 0 && button_add_book.getVisibility() != View.VISIBLE) {
                            button_add_book.show();
                        }
                    }
                });

                //adding listener with adapter
                mAdapter.setOnItemClickListener(new BookArchiveAdapter.onItemClickListener() {
                    @Override
                    public void onDownloadClick(int position) {
                        //getting the text from the selected position
                        BookArchive selectedItem = mBookArchive.get(position);
                        String book_link = selectedItem.getBook_url().trim();

                        //for download manager
                        DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(book_link);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        Long reference = downloadManager.enqueue(request);

                        Toast.makeText(BookArchiveMenu.this, "Downloading", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onDeleteClick(int position) {
                        //Toast.makeText(BookArchiveMenu.this, "Delete is selected", Toast.LENGTH_SHORT).show();
                        BookArchive selectedItem = mBookArchive.get(position);
                        String User = selectedItem.getId().trim();
                        String key = selectedItem.getTimestamp();

                        //for administrative power
                        //if(userId.equals("B180305041"))
                        //    Toast.makeText(BookArchiveMenu.this, "Administrative Power", Toast.LENGTH_SHORT).show();
                        //for giving access to valid user
                        if (!userId.equals(User)) {
                            Toast.makeText(BookArchiveMenu.this, "Access Denied", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mCollectionRef.document(key).delete();
                        Toast.makeText(BookArchiveMenu.this, "Post deleted", Toast.LENGTH_SHORT).show();

                    }
                });


                //for progress circle
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }
}
