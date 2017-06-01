package com.firebase.woflfish.goodbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private SignInButton mSignInButton;
    private EditText editEmail;
    private EditText editPassWord;
    private TextInputLayout txt_error_email;
    private TextInputLayout txt_error_password;
    private Button btnSignIn;
    private Button btnSignUp;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private SharedPreferences mSharedPreferences;
    private boolean ischecked = false;
    private boolean checkSignIn = false;

    private List<String> charuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        charuser = new ArrayList<String>();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        // Assign fields
        mSignInButton = (SignInButton) findViewById(R.id.btn_signin_withg);
        editEmail = (EditText)findViewById(R.id.edit_email_signin);
        editPassWord = (EditText)findViewById(R.id.edit_password_signin);
        txt_error_email = (TextInputLayout)findViewById(R.id.txt_error_email);
        txt_error_password = (TextInputLayout)findViewById(R.id.txt_error_password);
        btnSignIn = (Button)findViewById(R.id.btn_signin_signin);
        btnSignUp = (Button)findViewById(R.id.btn_signup_signin);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ReadUsser();
        // Detect focus so as to show/hide error when typing email and password
        editEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    txt_error_email.setError("");
                    txt_error_password.setError("");
                }
            }
        });
        editPassWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    txt_error_email.setError("");
                    txt_error_password.setError("");
                }
            }
        });


                // Change content of Sign in with Gmail button into Vietnamese
        setGooglePlusButtonText(mSignInButton,getString(R.string.sign_in_with_gmail));


        //Click button Sign in
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //StartActivity
                checkSignIn = true;
                SignInWithMailAndPassWord();
            }
        });
        //Click button Sign up
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
            }
        });

        // Set click listeners
        mSignInButton.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();


            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        // User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                        if (isCheckUser()) {
                            startActivity(new Intent(LoginActivity.this, HomeCharityOrganizationActivity.class));
                            finish();
                        } else {
                            if(checkSignIn) {
                                startActivity(new Intent(LoginActivity.this, HomeDonorActivity.class));
                                finish();
                            }
                        }
                    } else {
                        // User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                    // ...
                }
            };


    }
    private void SignInWithMailAndPassWord(){
        String email = editEmail.getText().toString();
        String password = editPassWord.getText().toString();

        if(email.equals("")) {
            txt_error_email.setError(getString(R.string.error_field_required));
            return;
        }
        if(password.equals("")) {
            txt_error_password.setError(getString(R.string.error_field_required));
            return;
        }
        if(!isValidEmail(email)) {
            txt_error_email.setError(getString(R.string.error_invalid_email));
            return;
        }

        if(!email.equals("") && !password.equals("")) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    txt_error_email.setError(getString(R.string.error_invalid_email));
                                } catch (FirebaseAuthUserCollisionException e) {
                                    txt_error_email.setError(getString(R.string.error_user_exists));
                                } catch (Exception e) {
                                    txt_error_password.setError(getString(R.string.error_incorrect_password));
                                    Log.e(TAG, e.getMessage());
                                }
                            } else {
                                //Kiểm tra tài khoản thuộc người dùng nào ?
                                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                if (isCheckUser()) {
                                    startActivity(new Intent(LoginActivity.this, HomeCharityOrganizationActivity.class));
                                    finish();
                                }
                                else {
                                    startActivity(new Intent(LoginActivity.this, HomeDonorActivity.class));
                                    finish();
                                }

                            }

                            // ...
                        }
                    });
        }
    }
    private void ReadUsser(){
        mDatabase.child("Users").child("CharityOrganizations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    String uid = postSnapshot.getKey();
                    Log.e("Key pass",uid);
                    charuser.add(uid);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private boolean isCheckUser(){
        FirebaseUser fuser = mAuth.getCurrentUser();
        Log.e("Check: ",fuser.getUid());
        if(charuser.size()>0) {
            for(int i =0;i<charuser.size();i++) {
                if (fuser.getUid().equals(charuser.get(i))) {
                    ischecked = true;
                    return ischecked;
                }
            }
        }
        Log.e("Status ",  String.valueOf(ischecked));
        return ischecked;
    }
    private void handleFirebaseAuthResult(AuthResult authResult) {
        if (authResult != null) {
            // Welcome the user
            FirebaseUser user = authResult.getUser();
            Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
            // Go back to the main activity
            if(isCheckUser()) {
                startActivity(new Intent(LoginActivity.this, HomeCharityOrganizationActivity.class));
                finish();
            }
            else{
                startActivity(new Intent(LoginActivity.this, HomeDonorActivity.class));
                finish();
            }
            //startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signin_withg:
                signIn();
                break;
            //case R.id.btn_sign_in:
            // SignInWithMailAndPassWord();
            //break;
            default:
                return;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        final String Id =acct.getId();
        final String fullName = acct.getDisplayName();
        final String email = acct.getEmail();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser usser = mFirebaseAuth.getCurrentUser();
                            String uid = usser.getUid();
                            writeNewUser(uid,fullName,email);
                            startActivity(new Intent(LoginActivity.this, HomeDonorActivity.class));
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
                    Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(LoginActivity.this,"Đăng nhập thấ bại",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
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

    protected void setGooglePlusButtonText(SignInButton signInButton,
                                           String buttonText) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setTextSize(15);
                tv.setTypeface(null, Typeface.NORMAL);
                tv.setText(buttonText);
                return;
            }
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}

