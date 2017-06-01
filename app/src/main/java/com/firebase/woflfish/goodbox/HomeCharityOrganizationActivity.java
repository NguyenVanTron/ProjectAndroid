package com.firebase.woflfish.goodbox;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;

public class HomeCharityOrganizationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {
    private static final String TAG = "SignInActivity";
    public static final String ANONYMOUS = "anonymous";
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    private Uri imageCaptureUri;
    private static final int RC_SIGN_IN = 9001;
    public CharityOrganization us;
    private String mUsername;
    private String mPhotoUrl;
    private FirebaseAuth mAuth;
    private ImageView imagev1;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public ExpandableRelativeLayout expandableLayout1,expandableLayout2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_charity_organization);
        imagev1 = (ImageView)findViewById(R.id.imageViewAvatar);
        us = new CharityOrganization();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

        mUsername = ANONYMOUS;
        // Initialize Firebase Auth
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    mUsername = user.getDisplayName();
                    us.setFullName(mUsername);
                    us.setEmail(user.getEmail());
                    us.setAdress(user.getUid());
                    //mPhotoUrl = user.getPhotoUrl().toString();

                } else {

                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(HomeCharityOrganizationActivity.this,"Đăng xuất thành công",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(HomeCharityOrganizationActivity.this, LoginActivity.class));
                    finish();

                }
                // ...
            }
        };
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    //Set avatar
    public void SetAvatar(){
        final String[] items = new String[]{"Chụp hình","Chọn từ bộ sưu tập"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i ==0){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(),"tmp_avatar"+String.valueOf(System.currentTimeMillis())+".jpg");
                    imageCaptureUri = Uri.fromFile(file);
                    try{
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageCaptureUri);
                        intent.putExtra("return data",true);
                        startActivityForResult(intent,PICK_FROM_CAMERA);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                    dialogInterface.cancel();
                }
                else{
                    Log.e("Vao dc else","Debug");
                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //intent.setType("image/*");
                    //intent.setAction(Intent.ACTION_GET_CONTENT);Intent.createChooser(intent,"Complete action using")
                    startActivityForResult(intent,PICK_FROM_FILE);
                }
            }
        });
        builder.setTitle("Thêm ảnh bạn muốn !");
        final AlertDialog dialog = builder.create();
        dialog.show();
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
                            Toast.makeText(HomeCharityOrganizationActivity.this,"Cập nhật thông tin thành công",Toast.LENGTH_LONG).show();

                        }
                        else
                            Toast.makeText(HomeCharityOrganizationActivity.this,"Cập nhật thông tin thất bại",Toast.LENGTH_LONG).show();
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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void expandablecheckbox1(View view) {
        expandableLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout1);
        expandableLayout1.toggle();
    }
    public void expandablecheckbox2(View view) {
        expandableLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);
        expandableLayout2.toggle();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_charity_organization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_signout){
            mAuth.signOut();
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
            mUsername = ANONYMOUS;
            startActivity(new Intent(HomeCharityOrganizationActivity.this,LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Vao dc result","Debug");
        if(resultCode != RESULT_OK){
            return;
        }
        Bitmap bitmap = null;
        String path = "";
        if(requestCode == PICK_FROM_FILE){
            imageCaptureUri = data.getData();
            path = getRealPathFromUri(imageCaptureUri);
            if(path==null){
                path = imageCaptureUri.getPath();
            }
            if(path!=null){
                //Uri file = Uri.fromFile(new File(path));

                Log.e("Path khac null","Debug");
                bitmap = BitmapFactory.decodeFile(path);
            }
        }
        else{
            //Uri file = Uri.fromFile(new File(path));

            path = imageCaptureUri.getPath();
            bitmap = BitmapFactory.decodeFile(path);
        }
        imagev1.setImageBitmap(bitmap);

    }
    private String getRealPathFromUri(Uri contentUri) {
        String [] proj = {MediaStore.Images.Media.DATA};
        Cursor cusor = managedQuery(contentUri,proj,null,null,null);
        if(cusor == null) return null;
        int column_index = cusor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cusor.moveToFirst();
        Log.e("Cusor khac null","Debug");
        return cusor.getString(column_index);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager frangmentManager = getFragmentManager();
        if (id == R.id.nav_charity_home) {
            frangmentManager.beginTransaction()
                    .replace(R.id.content_frame_home_charity,new CharityHomeActivity())
                    .commit();
            // Handle the camera action
        } else if (id == R.id.nav_charity_list_activity) {
            frangmentManager.beginTransaction()
                    .replace(R.id.content_frame_home_charity,new CharityListActivity())
                    .commit();

        } else if (id == R.id.nav_charity_create_activity) {
            frangmentManager.beginTransaction()
                    .replace(R.id.content_frame_home_charity,new CharityCreateActivity())
                    .commit();

        } else if (id == R.id.nav_list_charityorg_activity) {
            SharedPreferences sharedPreferences= getSharedPreferences("IdCharity", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", us.getFullName() );
            editor.putString("Idchar",us.getAdress());
            editor.apply();
            frangmentManager.beginTransaction()
                    .replace(R.id.content_frame_home_charity,new DetailCharityActivity())
                    .commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
