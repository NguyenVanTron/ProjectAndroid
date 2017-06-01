package com.firebase.woflfish.goodbox;

import android.app.Fragment;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 1/18/2017.
 */

public class DetailDonationCharityActivity extends Fragment{

    View myView;
    private List<AtDropOffLocation> dataoLocation;
    private List<PickedUpFromHome> dataoHone;
    private List<String> uiddrop;
    private List<String> uidhome;
    private DatabaseReference mDatabase;
    private ImageButton imabtnLoad;
    private TextView txtDescription;
    private Button btnAccept;
    private Button btnDenined;
    private int position;
    private  boolean flag;
    private ImageView imgActivity1;
    private ImageView imgActivity2;
    private ImageView imgActivity3;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.detail_donation_charity_activity, container, false);

        txtDescription = (TextView)myView.findViewById(R.id.tv_des_donation) ;
        imabtnLoad = (ImageButton)myView.findViewById(R.id.imabt_load_charity);
        dataoLocation = new ArrayList<AtDropOffLocation>();
        uiddrop = new ArrayList<String>();
        dataoHone = new ArrayList<PickedUpFromHome>();
        uidhome = new ArrayList<String>();
        imgActivity1 = (ImageView)myView.findViewById(R.id.imagev_activity_donation1);
        imgActivity2 = (ImageView)myView.findViewById(R.id.imagev_activity_donation2);
        imgActivity3 = (ImageView)myView.findViewById(R.id.imagev_activity_donation3);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        flag =false;
        btnAccept = (Button)myView.findViewById(R.id.btn_accept);
        btnDenined = (Button)myView.findViewById(R.id.btn_denied);
        SharedPreferences sharedPreferences = myView.getContext().getSharedPreferences("PosActivity", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            position = sharedPreferences.getInt("positionB", 95);
        } else {
            position = 0;
        }
        ReadDataLocationList();
        ReadDataFromHomeList();
        imabtnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = myView.getContext().getSharedPreferences("History", Context.MODE_PRIVATE);
                String donorName;
                String actiName;
                if (sharedPreferences != null) {
                    actiName = sharedPreferences.getString("actiname", "Name");
                    donorName = sharedPreferences.getString("namedonor", "Name");
                } else {
                    actiName = "";
                    donorName = "";
                }

                if(position<uiddrop.size()){
                    flag = true;
                    String description = "Tên hoạt động: " + actiName +"\n"+
                            "Mô tả: " + dataoLocation.get(position).getDescription()+"\n";
                    if(dataoLocation.get(position).getCategories().size()>0){
                        description = description + "Loại đồ quyên góp\n";
                        for(int i =0; i<dataoLocation.get(position).getCategories().size();i++) {
                            description = description + String.valueOf(i)+ ") "+ dataoLocation.get(position).getCategories().get("Category" + String.valueOf(i)) +"\n";
                        }
                    }
                    if(dataoLocation.get(position).getAdress().size()>0){
                        description = description + "Địa chỉ gửi quyên góp\n";
                        for(int i =0; i<dataoLocation.get(position).getAdress().size();i++) {
                            description = description + String.valueOf(i)+ ") "+ dataoLocation.get(position).getAdress().get("Adress" + String.valueOf(i)) +"\n";
                        }
                    }
                    if(dataoLocation.get(position).getStatus()==1){
                        description += "Tình trạng: Yêu cầu được chấp nhân"+"\n";
                    }
                    if(dataoLocation.get(position).getStatus()==0){
                        description += "Tình trạng: Yêu cầu đã gửi"+"\n";
                    }
                    if(dataoLocation.get(position).getStatus()==-1){
                        description += "Tình trạng: Yêu cầu bị từ chối"+"\n";
                    }
                    txtDescription.setText(description);
                    if(dataoLocation.get(position).getUrlImages().size()>0) {
                        for(int i = 0;i<dataoLocation.get(position).getUrlImages().size();i++) {
                            String url = dataoLocation.get(position).getUrlImages().get("Url" + String.valueOf(i));
                            if (!url.equals("") && (i ==0)) {
                                new DownloadImageTask(imgActivity1)
                                        .execute(url);
                            }
                            if (!url.equals("") && (i ==1)) {
                                new DownloadImageTask(imgActivity2)
                                        .execute(url);
                            }
                            if (!url.equals("") && (i ==2)) {
                                new DownloadImageTask(imgActivity3)
                                        .execute(url);
                            }
                        }

                    }

                }else{
                    position = position - uiddrop.size();
                    String description = "Tên hoạt động: " + actiName + "\n" +
                            "Mô tả: " + dataoHone.get(position).getDescription() +"\n";
                    if(dataoHone.get(position).getCategories().size()>0){
                        description = description + "Loại đồ quyên góp\n";
                        for(int i =0; i<dataoHone.get(position).getCategories().size();i++) {
                            description = description + String.valueOf(i)+ ") "+ dataoHone.get(position).getCategories().get("Category" + String.valueOf(i)) +"\n";
                        }
                    }
                    description = description + "Tên người gửi: ";
                    description = description + dataoHone.get(position).getName() +"\n";
                    description = description + "Số điện thoại: ";
                    description = description + dataoHone.get(position).getPhoneNumber() +"\n";
                    description = description + "Địa chỉ gửi quyên góp tại nhà\n";
                    description = description + dataoHone.get(position).getAdress() +"\n";
                    if(dataoHone.get(position).getStatus()==1){
                        description += "Tình trạng: Yêu cầu được chấp nhân"+"\n";
                    }
                    if(dataoHone.get(position).getStatus()==0){
                        description += "Tình trạng: Yêu cầu đã gửi"+"\n";
                    }
                    if(dataoHone.get(position).getStatus()==-1){
                        description += "Tình trạng: Yêu cầu bị từ chối"+"\n";
                    }
                    txtDescription.setText(description);
                    if(dataoHone.get(position).getUrlImages().size()>0) {
                        for(int i = 0;i<dataoHone.get(position).getUrlImages().size();i++) {
                            String url = dataoHone.get(position).getUrlImages().get("Url" + String.valueOf(i));
                            if (!url.equals("") && (i ==0)) {
                                new DownloadImageTask(imgActivity1)
                                        .execute(url);
                            }
                            if (!url.equals("") && (i ==1)) {
                                new DownloadImageTask(imgActivity2)
                                        .execute(url);
                            }
                            if (!url.equals("") && (i ==2)) {
                                new DownloadImageTask(imgActivity3)
                                        .execute(url);
                            }
                        }

                    }
                }
            }
        });

        btnDenined.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag) {
                    mDatabase.child("Donations").child("AtDropOffLocation").child(uiddrop.get(position)).child("status").setValue(-1);
                }else{
                    mDatabase.child("Donations").child("PickefUpFromHome").child(uidhome.get(position)).child("status").setValue(-1);
                }
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag) {
                    mDatabase.child("Donations").child("AtDropOffLocation").child(uiddrop.get(position)).child("status").setValue(1);
                }else{
                    mDatabase.child("Donations").child("PickefUpFromHome").child(uidhome.get(position)).child("status").setValue(1);
                }
            }
        });
        return myView;
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
    private void ReadDataLocationList() {

        mDatabase.child("Donations").child("AtDropOffLocation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String uid = postSnapshot.getKey();
                    AtDropOffLocation dataUser = postSnapshot.getValue(AtDropOffLocation.class);
                    Log.e("Debug bobobo: ", uid);
                    uiddrop.add(uid);
                    dataoLocation.add(dataUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void ReadDataFromHomeList() {

        mDatabase.child("Donations").child("PickedUpFromHome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String uid = postSnapshot.getKey();
                    PickedUpFromHome dataUser = postSnapshot.getValue(PickedUpFromHome.class);
                    Log.e("Debug bobobo: ", uid);
                    uidhome.add(uid);
                    dataoHone.add(dataUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
