package com.example.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.myapplication.Adapter.JcSongsAdapter;
import com.example.myapplication.R;
import com.example.myapplication.model.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AllSongFragment extends Fragment {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Boolean checkin=false;
    List<Song> mupload;
    JcSongsAdapter adapter;
    DatabaseReference databaseReference;

    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios=new ArrayList<>();
    private int currentIndex;
     ImageView back;
     String user;
     EditText timkiem;
     ImageView search;
    public AllSongFragment(String user) {
        // Required empty public constructor
          this.user=user;

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_song, container, false);
        progressBar = view.findViewById(R.id.processbarshowsong_allsong);
        jcPlayerView = view.findViewById(R.id.jcplayer_allsong);
        recyclerView = view.findViewById(R.id.recycleview_id_allsong);
        back = view.findViewById(R.id.btn_back_allSong);
        search=view.findViewById(R.id.btn_search_allsong);
        timkiem=view.findViewById(R.id.edt_allsong);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListenMusicFragment listenMusicFragment = new ListenMusicFragment(user);
                FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.replaceFragement);
                frameLayout.setVisibility(View.GONE);
                frameLayout = (FrameLayout) getActivity().findViewById(R.id.replaceFragement2);
                frameLayout.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.replaceFragement2, listenMusicFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mupload = new ArrayList<>();
        recyclerView.setAdapter(adapter);
        adapter = new JcSongsAdapter(getContext(), mupload, user, new JcSongsAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(Song uploadSong, int position) {
                changeSelectedSong(position);
                jcPlayerView.playAudio(jcAudios.get(position));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification();
            }

        });
         databaseReference= FirebaseDatabase.getInstance().getReference("songs");
         databaseReference.addListenerForSingleValueEvent(valueEventListener);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timkiem.getText().toString().equals("")) {
                    Query query = FirebaseDatabase.getInstance().getReference("songs").orderByChild("songtitle")
                            .startAt(timkiem.getText().toString())
                            .endAt(timkiem.getText().toString()+"\uf8ff");
                    query.addListenerForSingleValueEvent(valueEventListener);

                }else{
                    databaseReference.addListenerForSingleValueEvent(valueEventListener);
                }
            }
        });
        return view;

    }
    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mupload.clear();
            for(DataSnapshot dss:dataSnapshot.getChildren()){
                Song uploadSong=dss.getValue(Song.class);
                uploadSong.setmKey(dss.getKey());
                currentIndex=0;


                mupload.add(uploadSong);
                checkin=true;
                jcAudios.add(JcAudio.createFromURL(uploadSong.getSongtitle(),uploadSong.getSongLink()));


            }
            adapter.setSelectPosition(0);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            if(checkin){
                jcPlayerView.initPlaylist(jcAudios,null);

            }else{
                Toast.makeText(getContext(),"there is songs!",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            progressBar.setVisibility(View.GONE);
        }
    };
    public void changeSelectedSong(int index){
        adapter.notifyItemChanged(adapter.getSelectPosition());
        currentIndex=index;
        adapter.setSelectPosition(currentIndex);
        adapter.notifyItemChanged(currentIndex);

    }
}
