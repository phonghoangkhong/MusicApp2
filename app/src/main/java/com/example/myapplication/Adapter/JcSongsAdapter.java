package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DAO.LoveSongDAO;
import com.example.myapplication.R;
import com.example.myapplication.model.Constants;
import com.example.myapplication.model.Song;
import com.example.myapplication.model.Utility;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class JcSongsAdapter extends RecyclerView.Adapter<JcSongsAdapter.SongsAdapterViewHolder> {
    private int selectPosition;
    Context context;
    List<Song> arrayListSongs;
    private RecyclerItemClickListener listener;
  DatabaseReference databaseReference;
    ArrayList<String> listLove;
    String user;
    public JcSongsAdapter(Context context, List<Song> arrayListSongs, String user, RecyclerItemClickListener listener) {
        this.context = context;
        this.arrayListSongs = arrayListSongs;
        this.listener = listener;
         this.user=user;
        listLove=new ArrayList<>();
        listLove=new LoveSongDAO().getLove(user);

    }

    @NonNull
    @Override
    public JcSongsAdapter.SongsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.songs_row,parent,false);
       return new  SongsAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull JcSongsAdapter.SongsAdapterViewHolder holder, int position) {

        Song uploadSong=arrayListSongs.get(position);
        if(uploadSong!=null){
            if(selectPosition==position){
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));
                holder.tv_play_active.setVisibility(View.VISIBLE);

            }else{
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context,R.color.mp3));
                holder.tv_play_active.setVisibility(View.VISIBLE);

            }
        }
        holder.tv_title.setText(uploadSong.getSongtitle());
        holder.tv_artist.setText(uploadSong.getArtist());
           String duration= Utility.convertDuration(Long.parseLong(uploadSong.getSongDuration()));
           holder.tv_duration.setText(duration);
           holder.bind(uploadSong,listener);
    }

    @Override
    public int getItemCount() {
        return arrayListSongs.size();
    }

    public class SongsAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_artist, tv_duration;
        ImageView tv_play_active;
        ImageView loveSong;

        public SongsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_artist = itemView.findViewById(R.id.tv_artist);
            tv_duration = itemView.findViewById(R.id.tv_duration);
            tv_play_active = itemView.findViewById(R.id.tv_play_active);
            loveSong=itemView.findViewById(R.id.iv_artwork);


        }

        public void bind(final Song uploadSong, final RecyclerItemClickListener listener) {
             uploadSong.setState(0);
            if(listLove.contains(uploadSong.getmKey())){
                loveSong.setImageResource(R.drawable.love2);
                uploadSong.setState(1);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickListener(uploadSong,getAdapterPosition());
                }
            });
           loveSong.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    uploadSong.setState(1-uploadSong.getState());
                    if(uploadSong.getState()==0) {
                        loveSong.setImageResource(R.drawable.love);
                           removeLove(uploadSong);
                    }else {
                        loveSong.setImageResource(R.drawable.love2);
                           setLove(uploadSong);

                    }

               }
           });

        }
        public void setLove(Song uploadSong){

             databaseReference= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_USER).child(user);
            String key=uploadSong.getmKey();
            databaseReference.child(key).setValue(uploadSong);
        }
        public void removeLove(Song uploadSong){

            databaseReference= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_USER).child(user);
            String key=uploadSong.getmKey();
            databaseReference.child(key).removeValue();
        }


    }


    public interface RecyclerItemClickListener {
               void onClickListener(Song uploadSong, int position);
    }
    public int getSelectPosition(){
        return selectPosition;
    }
    public void setSelectPosition(int selectPosition){
        this.selectPosition=selectPosition;
    }
}
