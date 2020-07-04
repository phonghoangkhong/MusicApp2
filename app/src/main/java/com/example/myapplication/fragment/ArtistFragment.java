package com.example.myapplication.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ArtistSongAdapter;
import com.example.myapplication.R;
import com.example.myapplication.model.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistFragment extends Fragment {
    RecyclerView recyclerView;
    ArtistSongAdapter adapter;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    private List<Song> songs;
    ImageView imageView;
    String user;
    public ArtistFragment(String user) {
        // Required empty public constructor
        this.user=user;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_artist_fragmnet, container, false);
        recyclerView=view.findViewById(R.id.recycleview_id_artistfragment);
        imageView = view.findViewById(R.id.btn_back_artistfragment);
        imageView.setOnClickListener(new View.OnClickListener() {
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
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        progressDialog=new ProgressDialog(getContext());
        songs =new ArrayList<>();
        progressDialog.setMessage("please wait.....");
        progressDialog.show();
        mDatabase= FirebaseDatabase.getInstance().getReference("songs");
        mDatabase.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                progressDialog.dismiss();
                                                List<String> listArtist = new ArrayList<>();
                                                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                                                    Song song = postsnapshot.getValue(Song.class);
                                                    if (!listArtist.contains(song.getArtist())){
                                                        listArtist.add(song.getArtist());
                                                        songs.add(song);
                                                         songs.size();
                                                }
                                            }

                                                adapter = new ArtistSongAdapter(getContext(), songs, user);
                                                recyclerView.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                progressDialog.dismiss();
                                            }
                                        }
        );


        return view;
    }
}
