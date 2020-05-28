package com.example.cse10;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private Context mContext;
    private List<Notice> mUploads;
    //variable for onItemClickListener
    private onItemClickListener listener;

    public NoticeAdapter(Context context, List<Notice> uploads) {
        mContext = context;
        mUploads = uploads;
    }


    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.notice_item, parent, false);
        return new NoticeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Notice uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getUsername());
        holder.textViewID.setText(uploadCurrent.getId());
        holder.textViewTimestamp.setText(uploadCurrent.getTimestamp());
        holder.textViewText.setText(uploadCurrent.getTextfield());

        //if there is no image
        if (uploadCurrent.getImageurl().trim().equals("")) {
            //Toast.makeText(mContext, "gone", Toast.LENGTH_SHORT).show();
            holder.imageView.setVisibility(View.GONE);
        } else {
            Picasso.with(mContext)
                    .load(uploadCurrent.getImageurl())
                    .placeholder(R.drawable.happy)//for place holder image(dafault image)we have used an icon for loading speed
                    .fit()
                    .centerInside()
                    .into(holder.imageView);

            // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
            PhotoViewAttacher attacher = new PhotoViewAttacher(holder.imageView);
            //attacher.update();


            holder.imageView.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewName;
        public TextView textViewID;
        public TextView textViewTimestamp;
        public TextView textViewText;
        public ImageView imageView;


        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_username);
            textViewID = itemView.findViewById(R.id.text_view_id);
            textViewTimestamp = itemView.findViewById(R.id.text_view_timestamp);
            textViewText = itemView.findViewById(R.id.text_view_text);
            imageView = itemView.findViewById(R.id.image_view);


            //for linking
            itemView.setOnClickListener(this);
            //we have to connect item view with listener
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //we have to connect item view with listener
            //creating context menu item
            menu.setHeaderTitle("Choose an Action");
            MenuItem onEdit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "delete");

            //adding listener to the menu item
            onEdit.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    //listener.onItemClick(position);

                    switch (item.getItemId()) {
                        case 1:
                            listener.onEdit(position);
                            return true;

                        case 2:
                            listener.onDelete(position);
                            return true;

                    }
                }
            }
            return false;
        }
    }

    //interface----2
    public interface onItemClickListener {
        void onItemClick(int position);

        void onEdit(int position);

        void onDelete(int position);
    }

    //through this method we will call the listener-------1
    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;

    }


}

