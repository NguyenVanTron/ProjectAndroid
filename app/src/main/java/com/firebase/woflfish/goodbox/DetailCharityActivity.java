package com.firebase.woflfish.goodbox;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 1/7/2017.
 */

public class DetailCharityActivity extends Fragment {
    View myView;
    private int position;
    private DatabaseReference mDatabase;
    private List<CharityOrganization> data;
    private List<String> uids;
    private TextView txtdescription;
    private ImageButton imageViewLoad;
    private Button btnViewActivity;
    private String fullDes;
    private ImageView imgAvatar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.detail_charity_activity, container, false);
        imageViewLoad = (ImageButton)myView.findViewById(R.id.imageLoad);
        txtdescription = (TextView)myView.findViewById(R.id.txtfulldescription) ;
        btnViewActivity = (Button)myView.findViewById(R.id.btnviewactivity);
        imgAvatar =(ImageView)myView.findViewById(R.id.imagev_avatar);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ReadCharityList();
        SharedPreferences sharedPreferences1 = myView.getContext().getSharedPreferences("gameSetting", Context.MODE_PRIVATE);
        if (sharedPreferences1 != null) {
            position = sharedPreferences1.getInt("position", 95);
        } else {
            position = 0;
        }

        data = new ArrayList<CharityOrganization>();
        uids = new ArrayList<String>();
        final String name;
        final String uidus;
        SharedPreferences sharedPreferences2 = myView.getContext().getSharedPreferences("IdCharity", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences4 = myView.getContext().getSharedPreferences("gameSetting", Context.MODE_PRIVATE);
        if (sharedPreferences2 != null) {
            name = sharedPreferences2.getString("name", "Name");
            uidus = sharedPreferences2.getString("Idchar","idchar");
        } else {
            name = "";
            uidus = "";
        }
        boolean check = false;
        if(sharedPreferences4!=null){
            check = sharedPreferences4.getBoolean("checkusser",false);
        }
        else
        {
            check =false;
        }
        if(check) {
            imageViewLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //data.clear();
                    //uids.clear();
                    //ReadCharityList();
                    fullDes = "Tên tổ chức: " + data.get(position).getFullName() + "\n" +
                            "Mô tả: " + data.get(position).getDescription() + "\n" +
                            "Số điện thoại: " + data.get(position).getPhoneNumber() + "\n" +
                            "Email: " + data.get(position).getEmail() + "\n" +
                            "Địa chỉ: " + data.get(position).getAdress();
                    txtdescription.setText(fullDes);
                    if(data.get(position).getUrlPhoto()!=null) {
                        new DownloadImageTask(imgAvatar)
                                .execute(data.get(position).getUrlPhoto());
                    }
                    SharedPreferences sharedPreferences = myView.getContext().getSharedPreferences("IdCharity", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", data.get(position).getFullName());
                    editor.putString("Idchar", uids.get(position));
                    editor.apply();
                    SharedPreferences sharedPreferences5= myView.getContext().getSharedPreferences("gameSetting", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor5 = sharedPreferences5.edit();
                    editor5.putBoolean("checkusser",false);
                    editor5.apply();
                }
            });

            btnViewActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (myView.getContext().getClass().getName().equals("com.firebase.woflfish.goodbox.HomeCharityOrganizationActivity")) {
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction()
                                .replace(R.id.content_frame_home_charity, new CharityListActivity())
                                .commit();
                    } else {
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction()
                                .replace(R.id.content_frame_home_donor, new DonorListActivity())
                                .commit();
                    }

                }
            });
        }
        else{

            imageViewLoad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = checkIDChar(uidus);
                    if (pos != -1) {fullDes = "Tên tổ chức: " + name + "\n" +
                            "Mô tả: " + data.get(pos).getDescription() + "\n" +
                            "Số điện thoại: " + data.get(pos).getPhoneNumber() + "\n" +
                            "Email: " + data.get(pos).getEmail() + "\n" +
                            "Địa chỉ: " + data.get(pos).getAdress();
                        txtdescription.setText(fullDes);
                    } else
                        txtdescription.setText("Không có dữ liệu");
                }
                });
            btnViewActivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction()
                                .replace(R.id.content_frame_home_charity, new CharityListActivity())
                                .commit();
                }
            });
        }
        return  myView;
    }
    private int checkIDChar(String uid){
        for(int i = 0;i<uids.size();i++){
            if(uids.get(i).equals(uid))
                return i;
        }
        return -1;
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
                //Log.e("Error", e.getMessage());
                //e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    private void ReadCharityList(){
        mDatabase.child("Users").child("CharityOrganizations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String uid = postSnapshot.getKey();
                    CharityOrganization dataUser = postSnapshot.getValue(CharityOrganization.class);
                    Log.e("Debug bobobo: ", uid);
                    uids.add(uid);
                    data.add(dataUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}