package com.example.myapplication.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.Song;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AdminHome extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextView textViewImage;
    ProgressBar progressBar;
    Uri androidUri;
    StorageReference mStorageref;
    DatabaseReference referenceSongs;
    StorageTask mUploadsTask;
    String songsCategory;
    MediaMetadataRetriever metadataRetriever;
    byte[]art;
    String title1,artist1,album_art1="",durations1;
    TextView title,artist,album,duration,dataa;
    ImageView album_art;
    Button listenmusic;
    String artistImage;
    SharedPreferences sharedPreferences;
    ImageView img_logout;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewImage=findViewById(R.id.textViewSongFileSelect);
        progressBar=findViewById(R.id.processbar);
        listenmusic=findViewById(R.id.listenmusic);
        title=findViewById(R.id.title);
        artist=findViewById(R.id.artist);
        duration=findViewById(R.id.duration);
        album=findViewById(R.id.album);
        dataa=findViewById(R.id.dataa);
        album_art=findViewById(R.id.imageView_main);
        metadataRetriever=new MediaMetadataRetriever();
        referenceSongs=FirebaseDatabase.getInstance().getReference().child("songs");
         mStorageref=FirebaseStorage.getInstance().getReference().child("songs");
         firebaseFirestore=FirebaseFirestore.getInstance();
        Spinner spinner=findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> categories=new ArrayList<>();
        categories.add("Love Songs");
        categories.add("Sad Songs");
        categories.add("Party Songs");
        categories.add("Birthday Songs");
        img_logout = findViewById(R.id.btn_logout_admin);
        sharedPreferences=getSharedPreferences("musicApp", Context.MODE_PRIVATE);

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.remove("trang thai");
                editor.commit();
                Intent intent = new Intent(AdminHome.this, Login.class);
                startActivity(intent);

            }
        });
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
         listenmusic.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent();

                 intent.putExtra("user","admin");
                 intent.setClass(AdminHome.this, ClientHome.class);
                 startActivity(intent);
             }
         });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       songsCategory=parent.getItemAtPosition(position).toString();
        Toast.makeText(this,"Selected: "+songsCategory,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void openAudioFiles(View v){
        Intent i=new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        startActivityForResult(i,101
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==101&& data.getData()!=null){

            Uri a=data.getData();
          androidUri=a;

           String fileNames=getFileName(androidUri);
           metadataRetriever.setDataSource(this,androidUri);
           textViewImage.setText(fileNames);
           art=metadataRetriever.getEmbeddedPicture();

             artistImage= Base64.encodeToString(art,Base64.DEFAULT);


           Bitmap bitmap= BitmapFactory.decodeByteArray(art,0,art.length);

            album_art.setImageBitmap(bitmap);
            album.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            artist.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
            dataa.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
            duration.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

            title.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
            artist1=metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            title1=metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            durations1=metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);



        }

    }
    private String getFileName(Uri uri){
        String result=null;
        if(uri.getScheme().equals("content")){
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                }
            }finally {
                cursor.close();
            }
        }
        if(result==null){
            result=uri.getPath();
            int cut=result.lastIndexOf('/');
            if(cut!=-1){
                return result.substring(cut+1);

            }
        }
        return result;
    }
    public void uploadFileTofirebase(View v){
        if(textViewImage.equals("No file Selected")){
            Toast.makeText(this,"please select as image",Toast.LENGTH_LONG).show();

        }else{
            if(mUploadsTask!=null&&mUploadsTask.isInProgress()){
                Toast.makeText(this,"song uploads is allready progress",Toast.LENGTH_LONG).show();

            }else{
                uploadFiles();
            }

        }
    }
    public void uploadFiles(){
             if(androidUri!=null){
                 Toast.makeText(this,"upload please wait",Toast.LENGTH_LONG).show();
                 progressBar.setVisibility(View.VISIBLE);
                 final StorageReference storageReference=mStorageref.child(System.currentTimeMillis()+getfileextension(androidUri));
                  mUploadsTask=storageReference.putFile(androidUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                              @Override
                              public void onSuccess(Uri uri) {

                                  Song uploadSong=new Song(songsCategory,title1,artist1,album_art1,durations1,uri.toString(),artistImage);
                                  String uploadId=String.valueOf(title1.hashCode());
                                  referenceSongs.child(uploadId).setValue(uploadSong);

                              }
                          });
                      }


                  }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                          double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                          progressBar.setProgress((int )progress);
                      }
                  });
             }else{
                 Toast.makeText(this,"No file Selected to uploads",Toast.LENGTH_LONG).show();
             }
    }
    public String getfileextension(Uri androidUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(androidUri));
    }
    public void uploadImageAlbum(View v){
          Intent in=new Intent(AdminHome.this, UploadAlbumActivity.class);
          startActivity(in);
    }

}
