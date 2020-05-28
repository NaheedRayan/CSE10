package com.example.cse10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookArchiveAdapter extends RecyclerView.Adapter<BookArchiveAdapter.BookArchiveViewHolder> {

    private Context mContext ;
    private List<BookArchive> mUploads;
    //variable for onItemClickListener
    private onItemClickListener listener ;


    public BookArchiveAdapter(Context context , List<BookArchive>uploads){
        mContext = context ;
        mUploads = uploads ;
    }

    @NonNull
    @Override
    public BookArchiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.book_archive_item , parent ,false);
        return new BookArchiveViewHolder(v) ;
    }

    @Override
    public void onBindViewHolder(@NonNull BookArchiveViewHolder holder, int position) {
        BookArchive uploadCurrent = mUploads.get(position) ;
        holder.textViewBookName.setText(uploadCurrent.getBook_name());
        holder.textViewTimestamp.setText(uploadCurrent.getTimestamp());
        holder.textViewUsername.setText(uploadCurrent.getUsername());

    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class BookArchiveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewBookName ;
        public TextView textViewUsername ;
        public TextView textViewTimestamp ;
        public ImageView imageView_download;
        public ImageView imageView_delete ;

        public BookArchiveViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTimestamp = itemView.findViewById(R.id.book_archive_timestamp) ;
            textViewBookName = itemView.findViewById(R.id.book_name) ;
            textViewUsername = itemView.findViewById(R.id.contributor_name) ;
            imageView_download = itemView.findViewById(R.id.download_button) ;
            imageView_delete = itemView.findViewById(R.id.delete_button) ;

            //for linking the images to be clicked---3
            imageView_download.setOnClickListener(this);
            imageView_delete.setOnClickListener(this);

        }

        //this method will be auto generated when implements View.OnClickListener is written---4
        @Override
        public void onClick(View v) {

            //if the download image is clicked
            if(v.getId() == R.id.download_button)
            {
                if(listener!=null)
                {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        listener.onDownloadClick(position);
                    }
                }
            }
            //if the delete image is clicked
            else
            {
                if(listener!=null)
                {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        listener.onDeleteClick(position);
                    }
                }
            }

        }
    }


    //interface----2
    public interface onItemClickListener{
        void onDownloadClick(int position) ;
        void onDeleteClick(int position) ;

    }

    //through this method we will call the listener-------1
    public void setOnItemClickListener(onItemClickListener listener)
    {
        this.listener = listener ;
    }
}
