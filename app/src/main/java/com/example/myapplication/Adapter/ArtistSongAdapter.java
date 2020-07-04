package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.ArtistActivity;
import com.example.myapplication.model.Song;

import java.util.List;

public class ArtistSongAdapter  extends RecyclerView.Adapter<ArtistSongAdapter.MyViewHolder> {
    private Context mContext;
    private List<Song> artistImages;
    String user;
    public ArtistSongAdapter(Context mContext, List<Song> artistImages, String user) {
        this.mContext = mContext;
       this.artistImages=artistImages;
        this.user=user;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater=LayoutInflater.from(mContext);
        view=inflater.inflate(R.layout.card_view_item,parent,false);

        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Song artistImage = artistImages.get(position);
        String t= artistImage.getArtist();

        holder.tv_book_title.setText(artistImage.getArtist());
        byte[] image= Base64.decode(artistImage.getArtistImage(),Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(image,0,image.length);
        holder.img_book_thumail.setImageBitmap(bitmap);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ArtistActivity.class);
                intent.putExtra("user2",user);
                intent.putExtra("ArtistSong", artistImage.getArtist());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return artistImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_book_title;
        ImageView img_book_thumail;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_book_title=itemView.findViewById(R.id.book_title);
            img_book_thumail=itemView.findViewById(R.id.book_img_id);
            cardView=itemView.findViewById(R.id.card_view_id);
        }
    }
}
