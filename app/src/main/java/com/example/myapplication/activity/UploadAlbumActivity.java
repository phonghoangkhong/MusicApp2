package com.example.myapplication.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.CategoryImage;
import com.example.myapplication.model.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadAlbumActivity extends AppCompatActivity implements View.OnClickListener{
    Button buttonChoose;
    Button buttonUpload;
    EditText editTextName;
    ImageView imageView;
    String songCategory;
     private static final int PICK_IMAGE_REQUEST=234;
     private Uri filepath;
     StorageReference storageReference;
     DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_album);
       buttonChoose=findViewById(R.id.buttonChoose);
       buttonUpload=findViewById(R.id.buttonUpload2);
       editTextName=findViewById(R.id.edittext);
       imageView=findViewById(R.id.imageView);
       storageReference= FirebaseStorage.getInstance().getReference();
       mDatabaseReference= FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADIMAGECATEGORY);
        Spinner spinner=findViewById(R.id.spinner_uploadalbum);
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        List<String> categories=new ArrayList<>();
        categories.add("Love Songs");
        categories.add("Sad Songs");
        categories.add("Party Songs");
        categories.add("Birthday Songs");

        ArrayAdapter<String> dataAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                songCategory=parent.getItemAtPosition(position).toString();
                Toast.makeText(UploadAlbumActivity.this,"Select :"+songCategory, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v==buttonChoose){
            showFileChoose();
        }
        else {
            if (v == buttonUpload) {
               // Toast.makeText(UploadAlbumActivity.this, "upload", Toast.LENGTH_LONG).show();
                uploadFile();
            }
        }
    }
    private void uploadFile(){
            if(filepath!=null){
                final ProgressDialog progressDialog=new ProgressDialog(this);
                progressDialog.setTitle("uploading");
                progressDialog.show();
                final StorageReference sRef =storageReference.child(Constants.STORANGE_PATH_UPLOADIMAGECATEGORY+System.currentTimeMillis());
                sRef.putFile(filepath).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                              String url=uri.toString();
                              CategoryImage categoryImage =new CategoryImage(editTextName.getText().toString().trim(),url,songCategory);
                              String uploadId=String.valueOf(songCategory.hashCode());
                              mDatabaseReference.child(uploadId).setValue(categoryImage);
                              progressDialog.dismiss();
                              Toast.makeText(UploadAlbumActivity.this,"File Uploaded",Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UploadAlbumActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress=(100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("uploaded"+(int)progress+"%......");

                    }
                });
            }
    }
    private void showFileChoose(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST&& resultCode==RESULT_OK&&data !=null&&data.getData()!=null ){
            filepath=data.getData();
            Bitmap bitmap=null;
            try{
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
