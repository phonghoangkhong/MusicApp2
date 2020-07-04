package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;

public class ListenSongAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private final String[] title;
    private final Integer[] imageId;
    private  Integer[][] img;

    public ListenSongAdapter(Context context,
                      String[] title, Integer[] imageId, Integer[][] img) {
        super(context, R.layout.listenmusicrow, title);
        this.mContext = context;
        this.title = title;
        this.imageId = imageId;
        this.img = img;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView= inflater.inflate(R.layout.listenmusicrow, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.tv1);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ic1);
        txtTitle.setText(title[position]);
        imageView.setImageResource(imageId[position]);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);

        imageView = (ImageView) rowView.findViewById(R.id.img1);
        imageView.setImageResource(img[position][0]);
        imageView = (ImageView) rowView.findViewById(R.id.img2);
        imageView.setImageResource(img[position][1]);
        imageView = (ImageView) rowView.findViewById(R.id.img3);
        imageView.setImageResource(img[position][2]);


        return rowView;
    }
}
