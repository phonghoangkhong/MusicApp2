package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText user,matKhau;
    Button btnDK;
   private  final int REGISTER_SUCESS=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
         btnDK=findViewById(R.id.btn_register2);
        user=findViewById(R.id.edt_email2);
         matKhau=findViewById(R.id.edt_pw2);
        mAuth = FirebaseAuth.getInstance();
        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangKy();
            }
        });

    }

    private void DangKy(){
        final String email=user.getText().toString();
        final String password=matKhau.getText().toString();
        mAuth.createUserWithEmailAndPassword( email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){

                           Toast.makeText(Register.this,"Đăng ký thành công",Toast.LENGTH_LONG).show();
                           Intent intent=new Intent();
                           intent.putExtra("email",email);
                           intent.putExtra("password",password);
                           setResult(REGISTER_SUCESS,intent);
                           finish();
                       }else{
//                           Exception e=task.getException();
//                           e.printStackTrace();
                           Toast.makeText(Register.this,"Nhập đúng định dạng email, mật khẩu có ít nhất 6 ký tự",Toast.LENGTH_LONG).show();
                       }

                        // ...
                    }
                });

    }
}
