package com.example.myapplication.DAO;

import androidx.annotation.NonNull;

import com.example.myapplication.model.Constants;
import com.example.myapplication.model.Song;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoveSongDAO {
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    public ArrayList<String> getLove(String t){
        final ArrayList<String> list=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_ARTIST).child(t);
        valueEventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dss:dataSnapshot.getChildren()){
                    Song uploadSong=dss.getValue(Song.class);
                     list.add(dss.getKey());
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }



        });

                   return list;
    }
}
