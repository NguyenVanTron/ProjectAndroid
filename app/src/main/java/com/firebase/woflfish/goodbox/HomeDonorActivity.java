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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.io.InputStream;

public class HomeDonorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    public static final String ANONYMOUS = "anonymous";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mPhotoUrl;
    private GoogleApiClient mGoogleApiClient;
    private ImageView imageView;
    private TextView txtName;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    private Uri imageCaptureUri;
    private  String idDonor;
    private DatabaseReference mDatabase;
    public ExpandableRelativeLayout expdrop,expfromhome;
    public void checkBoxExpDrop(View view) {
        expdrop = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutdrop);
        expdrop.toggle();
    }
    public void CheckBoxExpFromeHome(View view) {
        expfromhome = (ExpandableRelativeLayout) findViewById(R.id.expandableLayoutfromhome);
        expfromhome.toggle();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_donor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_donor);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_donor);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_donor);
        View hView =  navigationView.inflateHeaderView(R.layout.nav_header_home_donor);
        imageView = (ImageView)hView.findViewById(R.id.imageViewAvatar_donor);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetAvatarDonor();
            }
        });
        txtName = (TextView)hView.findViewById(R.id.txtdisplayname) ;
        mUsername = ANONYMOUS;
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        //imageView = (ImageView)findViewById(R.id.imagePhoto);
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(HomeDonorActivity.this, LoginActivity.class));
            finish();
        } else {
            idDonor = mFirebaseUser.getUid();
            mUsername = mFirebaseUser.getDisplayName();
            mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            if(!mPhotoUrl.equals("")) {
                new DownloadImageTask(imageView)
                        .execute(mPhotoUrl);
            }
            txtName.setText(mUsername);
        }
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    mUsername = user.getDisplayName();
                    idDonor = user.getUid();
                    //mPhotoUrl = user.getPhotoUrl().toString();
                    //txtName.setText(mUsername);
                    //new DownloadImageTask(imagev1)
                      //      .execute(mPhotoUrl);

                } else {

                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Toast.makeText(HomeDonorActivity.this,"Đăng xuất thành công",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(HomeDonorActivity.this, LoginActivity.class));
                    finish();

                }
                // ...
            }
        };

        navigationView.setNavigationItemSelectedListener(this);
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    //Set avatar
    public void SetAvatarDonor(){
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
        imageView.setImageBitmap(bitmap);

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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_donor);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_donor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_donor) {
            return true;
        }

        else if (id == R.id.action_signout_donor) {
            if (mFirebaseAuth!=null)
                mFirebaseAuth.signOut();
            if(mGoogleApiClient!=null)
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mFirebaseUser = null;
            mUsername = ANONYMOUS;
            mPhotoUrl = null;
            if (mAuthListener != null) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
            startActivity(new Intent(HomeDonorActivity.this,LoginActivity.class));
            finish();
            Toast.makeText(HomeDonorActivity.this,"Đăng xuất thành công", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager frangmentManager = getFragmentManager();
        if (id == R.id.nav_donor_home) {

            // Handle the camera action
            frangmentManager.beginTransaction()
                    .replace(R.id.content_frame_home_donor,new DonorHomeActivity())
                    .commit();
        } else if (id == R.id.nav_donor_list_activity) {
            frangmentManager.beginTransaction()
                    .replace(R.id.content_frame_home_donor,new DonorListActivity())
                    .commit();

        } else if (id == R.id.nav_history_donation) {
            SharedPreferences sharedPreferences= getSharedPreferences("IdDonor", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("idDon", idDonor );
            editor.apply();
            frangmentManager.beginTransaction()
                    .replace(R.id.content_frame_home_donor,new DonorHistoryDonationActivity())
                    .commit();

        } else if (id == R.id.nav_listcharityorg_activity_donor) {
            SharedPreferences sharedPreferences= getSharedPreferences("IdDonor", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("idDon", idDonor );
            editor.apply();
            frangmentManager.beginTransaction()
                    .replace(R.id.content_frame_home_donor,new DonorListCharityOrganizationActivity())
                    .commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_donor);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        //mFirebaseAuth.signOut();
        //Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        mFirebaseUser = null;
        mUsername = ANONYMOUS;
        mPhotoUrl = null;
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
