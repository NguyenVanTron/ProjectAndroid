package com.firebase.woflfish.goodbox;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private Button bSignupWithhMail;
    private EditText edEMail;
    private EditText edFullName;
    private EditText edPassword;
    final static String TAG = "SignUpActivity";
    private EditText edReTypePassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
// ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        bSignupWithhMail = (Button)findViewById(R.id.btn_signup_signup);
        edEMail = (EditText)findViewById(R.id.edit_email_signup);
        edFullName = (EditText)findViewById(R.id.edit_fullname_signup);
        edPassword  =(EditText)findViewById(R.id.edit_password_signup);
        edReTypePassword = (EditText)findViewById(R.id.edit_retype_password_signup);

        bSignupWithhMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edFullName.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this,"Bạn chưa nhập tên",Toast.LENGTH_LONG).show();
                    return;
                }
                if(edEMail.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this,"Bạn chưa nhập email",Toast.LENGTH_LONG).show();
                    return;
                }
                if(edPassword.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this,"Bạn chưa nhập mật khẩu lần 1",Toast.LENGTH_LONG).show();
                    return;
                }
                if(edReTypePassword.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this,"Bạn chưa nhập mật khẩu lần 2",Toast.LENGTH_LONG).show();
                    return;
                }
                if(!edPassword.getText().toString().equals(edReTypePassword.getText().toString())){
                    Toast.makeText(SignUpActivity.this,"Mật khẩu không khớp",Toast.LENGTH_LONG).show();
                    return;
                }
                CreateUserAccount(edEMail.getText().toString(),edPassword.getText().toString());
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(SignUpActivity.this,"Đăng nhập thành công",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUpActivity.this, HomeDonorActivity.class));
                    finish();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
                // ...
            }
        };


    }
    private void CreateUserAccount(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            FirebaseUser user = mAuth.getCurrentUser();
                            String id = user.getUid();
                            String fullnameuser = edFullName.getText().toString();
                            String memail = edEMail.getText().toString();
                            UpdateProfile(user,fullnameuser,"");
                            writeNewUser(id,fullnameuser,memail);
                            startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                            finish();
                        }
                    }
                });

    }
    private void writeNewUser(String idAcount,String fullName,String email) {
        User us = new User(fullName,email);

        mDatabase.child("Users").child("Donors").child(idAcount).setValue(us, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError == null){
                    Toast.makeText(SignUpActivity.this,"Đăng kí thành công",Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(SignUpActivity.this,"Đăng kí thất bại",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void UpdateProfile(FirebaseUser user,String name,String ulrphoto){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(Uri.parse(ulrphoto))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            Toast.makeText(SignUpActivity.this,"Cập nhật thông tin thành công",Toast.LENGTH_LONG).show();

                        }
                        else
                            Toast.makeText(SignUpActivity.this,"Cập nhật thông tin thất bại",Toast.LENGTH_LONG).show();
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
