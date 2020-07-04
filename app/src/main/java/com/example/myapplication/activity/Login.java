package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText edt_email, edt_pass;
    Button btn_login;
    Button btn_register;
    private FirebaseAuth mAuth;
    String email,password;
    private  final int REGISTER=100;
     SharedPreferences sharedPreferences;
     String trangThai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pw);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        sharedPreferences=getSharedPreferences("musicApp", MODE_PRIVATE);
        trangThai=sharedPreferences.getString("trang thai","");
        if(trangThai!=""){
            if(trangThai.equals("admin")) {
                Intent intent = new Intent();
                intent.putExtra("user2",trangThai);
                intent.setClass(Login.this, AdminHome.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent();
                intent.putExtra("user",trangThai);
                intent.setClass(Login.this, ClientHome.class);
                startActivity(intent);
            }

        }
        mAuth = FirebaseAuth.getInstance();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DangNhap();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivityForResult(intent,REGISTER);
            }
        });
    }
   private  void DangNhap() {
      email = edt_email.getText().toString();
     password=edt_pass.getText().toString();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (email.equals("admin@gmail.com")) {

                                Intent intent = new Intent();
                                intent.putExtra("user2","admin");
                                intent.setClass(Login.this, AdminHome.class);
                                startActivity(intent);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("trang thai","admin");
                                editor.commit();

                            } else {
                                String []user=email.split("@");
                                Intent intent = new Intent();
                                intent.putExtra("user",user[0]);

                                intent.setClass(Login.this, ClientHome.class);
                                startActivity(intent);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("trang thai",user[0]);
                                editor.commit();

                            }
                        } else {

                            Toast.makeText(Login.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();


                        }


                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REGISTER){
            edt_email.setText(data.getStringExtra("email"));
            edt_pass.setText(data.getStringExtra("password"));

        }
    }
}

