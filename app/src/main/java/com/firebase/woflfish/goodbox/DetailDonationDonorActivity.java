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

public class DetailDonationDonorActivity extends Fragment{
    View myView;
    private List<AtDropOffLocation> dataoLocation;
    private List<PickedUpFromHome> dataoHone;
    private List<String> uiddrop;
    private List<String> uidhome;
    private DatabaseReference mDatabase;
    private TextView txtDescription;
    private int position;
    private ImageButton imabtnLoad;
    private ImageView imgActivity1;
    private ImageView imgActivity2;
    private ImageView imgActivity3;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.detail_donation_donor_activity, container, false);

        txtDescription = (TextView)myView.findViewById(R.id.tv_des_donation_donor) ;
        imabtnLoad = (ImageButton)myView.findViewById(R.id.imabt_load);
        dataoLocation = new ArrayList<AtDropOffLocation>();
        uiddrop = new ArrayList<String>();
        dataoHone = new ArrayList<PickedUpFromHome>();
        uidhome = new ArrayList<String>();
        imgActivity1 = (ImageView)myView.findViewById(R.id.imagev_activity_donation_donor1);
        imgActivity2 = (ImageView)myView.findViewById(R.id.imagev_activity_donation_donor2);
        imgActivity3 = (ImageView)myView.findViewById(R.id.imagev_activity_donation_donor3);
        mDatabase = FirebaseDatabase.getInstance().getReference();
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

                    String description = "Tên hoạt động: " + actiName + "\n"+
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
                }else{
                    position = position - uiddrop.size();
                    String description = "Tên hoạt động: " + actiName +"\n"+
                            "Mô tả: " + dataoHone.get(position).getDescription()+"\n";
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
