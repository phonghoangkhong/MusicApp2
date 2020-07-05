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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    EditText edt_email, edt_pass;
    Button btn_login;
    Button btn_register;
        private FirebaseAuth mAuth;
    String email,password;
    private  final int REGISTER=100;
     SharedPreferences sharedPreferences;
     String trangThai;
    private Button signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "MainActivity";
    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        edt_email = findViewById(R.id.edt_email);
        edt_pass = findViewById(R.id.edt_pw);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        signInButton = findViewById(R.id.sign_in_button);

        sharedPreferences=getSharedPreferences("musicApp", MODE_PRIVATE);
        trangThai=sharedPreferences.getString("trang thai","");
        if(trangThai!=""){
            if(trangThai.equals("admin")) {
                Intent intent = new Intent();
                intent.putExtra("user2",trangThai);
                intent.setClass(LoginActivity.this, AdminHomeActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent();
                intent.putExtra("user",trangThai);
                intent.setClass(LoginActivity.this, ClientHomeActivity.class);
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
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent,REGISTER);
            }
        });


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
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
                                intent.setClass(LoginActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("trang thai","admin");
                                editor.commit();

                            } else {
                                String []user=email.split("@");
                                Intent intent = new Intent();
                                intent.putExtra("user",user[0]);

                                intent.setClass(LoginActivity.this, ClientHomeActivity.class);
                                startActivity(intent);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("trang thai",user[0]);
                                editor.commit();

                            }
                        } else {

                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_LONG).show();


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

        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //Google sign in
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void FirebaseGoogleAuth(final GoogleSignInAccount acc) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
//                    updateUI(user);
                    String personEmail = acc.getEmail();

                    Intent intent=new Intent(LoginActivity.this, ClientHomeActivity.class);
                    String []userName=personEmail.split("@");
                    intent.putExtra("user", userName[0]);
//                    intent.putExtra(mGoogleSignInClient, obj);
                    startActivity(intent);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("trang thai",userName[0]);
                    editor.commit();

                } else {
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                    updateUI(null);
                }
            }
        });
    }

}

