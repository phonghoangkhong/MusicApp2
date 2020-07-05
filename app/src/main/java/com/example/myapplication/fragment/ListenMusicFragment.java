package com.example.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.Adapter.ListenSongAdapter;
import com.example.myapplication.R;
import com.example.myapplication.activity.Login;


public class ListenMusicFragment extends Fragment {
    ImageView img_logout;
    String[] title = {"Bài hát",
            "Thể loại", "Ca sĩ"};
    Integer[][] img = {
            {R.drawable.img9, R.drawable.img5, R.drawable.img10},
            {R.drawable.img6, R.drawable.img0, R.drawable.img7},
            {R.drawable.img3, R.drawable.img2, R.drawable.img4}
    };
    Integer[] imageId = {
            R.drawable.ic1,
            R.drawable.playlist,
            R.drawable.ic_singer
    };
ListView listView;
String user;
SharedPreferences sharedPreferences;

    public ListenMusicFragment(String user) {
        // Required empty public constructor
        this.user=user;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listen_music, container, false);
        img_logout = view.findViewById(R.id.btn_logout);
        sharedPreferences=getActivity().getSharedPreferences("musicApp", Context.MODE_PRIVATE);
        Toast.makeText(getContext(),"Xin Chào : "+user,Toast.LENGTH_LONG).show();
        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.remove("trang thai");
                editor.commit();
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);

            }
        });
        ListenSongAdapter listAdapter =new ListenSongAdapter(this.getContext(),title, imageId, img);
        listView =  view.findViewById(R.id.list);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {//i la position
                    case 1://click vao danh sach bai hat
//                        ArrayList<SongPath> songList = AppDatabase.getInstance(getContext()).getSongList();

                        CategoryFragment categoryFragment = new CategoryFragment(user);
                        FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.replaceFragement);
                        frameLayout.setVisibility(View.GONE);
                        frameLayout = (FrameLayout) getActivity().findViewById(R.id.replaceFragement2);
                        frameLayout.setVisibility(View.VISIBLE);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.replaceFragement2, categoryFragment)
                                .addToBackStack(null)
                                .commit();
                        break;

                    case 0://click vao bai hat
                        AllSongFragment allSongFragment= new AllSongFragment(user);
                        FrameLayout frameLayout2 = (FrameLayout) getActivity().findViewById(R.id.replaceFragement);
                        frameLayout2.setVisibility(View.GONE);
                        frameLayout2 = (FrameLayout) getActivity().findViewById(R.id.replaceFragement2);
                        frameLayout2.setVisibility(View.VISIBLE);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.replaceFragement2, allSongFragment)
                                .addToBackStack(null)
                                .commit();
                        break;


                    case 2://click vao danh sach tac gia
                       ArtistFragment artistFragment = new ArtistFragment(user);
                        FrameLayout frameLayout1 = (FrameLayout) getActivity().findViewById(R.id.replaceFragement);
                        frameLayout1.setVisibility(View.GONE);
                        frameLayout1 = (FrameLayout) getActivity().findViewById(R.id.replaceFragement2);
                        frameLayout1.setVisibility(View.VISIBLE);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.replaceFragement2, artistFragment)

                                .addToBackStack(null)
                                .commit();
                }
            }
        });
        return view;
    }

}
