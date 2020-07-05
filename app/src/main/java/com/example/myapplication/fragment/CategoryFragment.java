package com.example.myapplication.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CategorySongAdapter;
import com.example.myapplication.R;
import com.example.myapplication.model.CategoryImage;
import com.example.myapplication.model.Constants;
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
public class CategoryFragment extends Fragment {
    RecyclerView recyclerView;
    CategorySongAdapter adapter;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    private List<CategoryImage> categoryImages;
    ImageView imageView;

     String user;
    public CategoryFragment(String user) {
        // Required empty public constructor
        this.user=user;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view;
       view=inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView=view.findViewById(R.id.recycleview_id3);
        imageView = view.findViewById(R.id.btn_back);


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
        categoryImages =new ArrayList<>();
        progressDialog.setMessage("please wait.....");
        progressDialog.show();
        mDatabase= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADIMAGECATEGORY);
        mDatabase.addListenerForSingleValueEvent(valueEventListener);


        return view;
    }
    ValueEventListener valueEventListener=  new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            progressDialog.dismiss();
            for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                CategoryImage categoryImage = postsnapshot.getValue(CategoryImage.class);
                categoryImages.add(categoryImage);
                categoryImages.size();
            }
            adapter = new CategorySongAdapter(getContext(), categoryImages, user);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            progressDialog.dismiss();
        }
    }
    ;
}
