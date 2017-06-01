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

public class DetailActivityDonorActivity extends Fragment {
    View myView;
    private int position;
    private DatabaseReference mDatabase;
    private List<DonationActivities> data;
    private List<String> uids;
    private TextView txtdescription;
    private ImageButton imageViewLoad;
    private Button btnCreateDonation;
    private String fullDes;
    private ImageView imgActivity1;
    private ImageView imgActivity2;
    private ImageView imgActivity3;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.detail_charity_activity_donor_activity, container, false);
        imageViewLoad = (ImageButton)myView.findViewById(R.id.imageLoad1);
        txtdescription = (TextView)myView.findViewById(R.id.txtfulldescription_detailactivity) ;
        btnCreateDonation = (Button)myView.findViewById(R.id.btncreate_donation);
        imgActivity1 = (ImageView)myView.findViewById(R.id.imagev_activity_donor1);
        imgActivity2 = (ImageView)myView.findViewById(R.id.imagev_activity_donor2);
        imgActivity3 = (ImageView)myView.findViewById(R.id.imagev_activity_donor3);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        SharedPreferences sharedPreferences = myView.getContext().getSharedPreferences("PosActivity", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            position = sharedPreferences.getInt("positionA", 95);
        } else {
            position = 0;
        }
        ReadActivityList();
        ReadActivityDonationList();
        data = new ArrayList<DonationActivities>();
        uids = new ArrayList<String>();
        imageViewLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = myView.getContext().getSharedPreferences("IdCharity", Context.MODE_PRIVATE);
                String Name;
                if (sharedPreferences != null) {
                    Name = sharedPreferences.getString("name", "IdName");
                } else {
                    Name = "";
                }
                fullDes = "Tên tổ chức: " + Name +"/n"+
                        "Tên hoạt động: " + data.get(position).getName() + "\n"+
                        "Ngày bắt đầu: " + data.get(position).getStartDate() + "\n"+
                        "Ngày kết thúc: " + data.get(position).getFinishDate()+"\n"+
                        "Mô tả: "+ data.get(position).getDescription()+"\n";
                if(data.get(position).getCategories().size()>0){
                    fullDes = fullDes + "Loại đồ quyên góp\n";
                    for(int i =0; i<data.get(position).getCategories().size();i++) {
                        fullDes = fullDes + String.valueOf(i)+ ") "+ data.get(position).getCategories().get("Category" + String.valueOf(i)) +"\n";
                    }
                }
                if(data.get(position).getAdress().size()>0){
                    fullDes = fullDes + "Địa chỉ nhận quyên góp\n";
                    for(int i =0; i<data.get(position).getAdress().size();i++) {
                        fullDes = fullDes + String.valueOf(i)+ ") "+ data.get(position).getAdress().get("Adress" + String.valueOf(i)) +"\n";
                    }
                }
                if(data.get(position).getUrlImages().size()>0) {
                    for(int i = 0;i<data.get(position).getUrlImages().size();i++) {
                        String url = data.get(position).getUrlImages().get("Url" + String.valueOf(i));
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
                txtdescription.setText(fullDes);

                SharedPreferences sharedPreferences1= myView.getContext().getSharedPreferences("IdActivity", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString("IdActi", uids.get(position));
                editor.apply();

            }
        });

        btnCreateDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data.get(position).getCategories().size()>0) {
                    btnCreateDonation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FragmentManager fm = getFragmentManager();
                            fm.beginTransaction()
                                    .replace(R.id.content_frame_home_donor, new DonorCreateDonationActivity())
                                    .commit();

                        }
                    });
                }else
                {
                    //Họa động không cần quyên góp
                    btnCreateDonation.setText("Hoạt động không cần quyên góp");
                }
            }
        });
        return  myView;
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
    public void ReadActivityList(){
        mDatabase.child("CharityActivities").child("Activities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SharedPreferences sharedPreferences = myView.getContext().getSharedPreferences("IdCharity", Context.MODE_PRIVATE);
                String Idchar;
                if (sharedPreferences != null) {
                    Idchar = sharedPreferences.getString("Idchar", "Ichar");
                } else {
                    Idchar = "";
                }
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String uid = postSnapshot.getKey();
                    DonationActivities datActivity = postSnapshot.getValue(DonationActivities.class);
                    Log.e("Debug bobobo: ",uid);
                    if(datActivity.getIdCharity().equals(Idchar)) {
                        uids.add(uid);
                        data.add(datActivity);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void ReadActivityDonationList(){
        mDatabase.child("CharityActivities").child("DonationActivities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int size = data.size();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    SharedPreferences sharedPreferences = myView.getContext().getSharedPreferences("IdCharity", Context.MODE_PRIVATE);
                    String Idchar;
                    if (sharedPreferences != null) {
                        Idchar = sharedPreferences.getString("Idchar", "Ichar");
                    } else {
                        Idchar = "";
                    }
                    String uid = postSnapshot.getKey();
                    DonationActivities datActivity = postSnapshot.getValue(DonationActivities.class);
                    Log.e("Debug bobobo: ",uid);
                    if(datActivity.getIdCharity().equals(Idchar)) {
                        uids.add(uid);
                        data.add(datActivity);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}