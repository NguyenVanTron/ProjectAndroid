package com.firebase.woflfish.goodbox;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.firebase.woflfish.goodbox.DonationHistoryListRecyclerAdapter.RecyclerViewHolderActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by USER on 1/14/2017.
 */

public class DonationHistoryListRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerViewHolderActivity> {
    private List<Donations> mapDonations ;
    private Context parent;

    public DatabaseReference mDatabase;
    private List<Donor> user;
    private List<DonationActivities> activities;
    private List<String> idActi;
    private List<String> userid;
    public DonationHistoryListRecyclerAdapter(Context par){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        activities = new ArrayList<DonationActivities>();
        idActi = new ArrayList<String>();
        user = new ArrayList<Donor>();
        userid = new ArrayList<String>();
        ReadUsser();
        ReadActivity();
        this.mapDonations = new ArrayList<Donations>();
        this.parent = par;
    }

    public DonationHistoryListRecyclerAdapter(List<Donations> data) {
        this.mapDonations = new ArrayList<Donations>();
        this.mapDonations = data;
    }

    public void setAdapter(Donations data){

        this.mapDonations.add(data);
    }
    public DonationHistoryListRecyclerAdapter() {
        this.mapDonations = new ArrayList<Donations>();

    }

    public void updateList(List<Donations> data){
        this.mapDonations = data;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerViewHolderActivity onCreateViewHolder(ViewGroup viewGroup,
                                                 int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.donation_history_list_item, viewGroup, false);
        return new RecyclerViewHolderActivity(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolderActivity viewHolder, int position) {
        int status = this.mapDonations.get(position).getStatus();

        if (status == 1) {
            viewHolder.tvStatus.setText("Yêu cầu được chấp nhận");
        }
        if (status == 0) {
            viewHolder.tvStatus.setText("Yêu cầu đã gửi");
        }
        if (status == -1) {
            viewHolder.tvStatus.setText("Yêu cầu bị từ chối");
        }

        String donorName = "";
        String iddonor = this.mapDonations.get(position).getIdDonor();
        for(int i = 0;i<userid.size();i++){
            if(userid.get(i).equals(iddonor)){
                donorName = user.get(i).getFullName();
            }
        }
        String actiName = "";
        String idactivi = this.mapDonations.get(position).getIdActivity();
        for(int i = 0;i<activities.size();i++){
            if(idActi.get(i).equals(idactivi)){
                actiName = activities.get(i).getName();
            }
        }
        SharedPreferences sharedPreferences = parent.getSharedPreferences("History", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("namedonor", donorName);
        editor.putString("actiname",actiName);
        editor.apply();

        viewHolder.tvNameChar.setText(actiName);
        viewHolder.tvName.setText(donorName);

    }

    @Override
    public int getItemCount() {
        return this.mapDonations.size();

    }

    public void addItem(int position, Donations user) {
        this.mapDonations.add(user);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        this.mapDonations.remove(position);
        notifyItemRemoved(position);
    }
    private void ReadUsser(){
        mDatabase.child("Users").child("Donors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    String uid = postSnapshot.getKey();
                    Log.e("Key pass",uid);
                    userid.add(uid);
                    user.add(postSnapshot.getValue(Donor.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void ReadActivity(){
        mDatabase.child("CharityActivities").child("Activities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    String uid = postSnapshot.getKey();
                    Log.e("Key pass",uid);
                    idActi.add(uid);
                    activities.add(postSnapshot.getValue(DonationActivities.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child("CharityActivities").child("DonationActivities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    String uid = postSnapshot.getKey();
                    Log.e("Key pass",uid);
                    idActi.add(uid);
                    activities.add(postSnapshot.getValue(DonationActivities.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public int GetPoss(int pos){
        return pos;
    }
    /**
     * ViewHolder for item view of list
     * */

    public class RecyclerViewHolderActivity extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        public TextView tvName;
        public TextView tvNameChar;
        public TextView tvStatus;
        public CardView cardView;


        public RecyclerViewHolderActivity(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.txt_name_donor_item);
            tvStatus = (TextView) itemView.findViewById(R.id.txt_status_item);
            cardView = (CardView)itemView.findViewById(R.id.cv_history_item);
            tvNameChar =(TextView)itemView.findViewById(R.id.txt_name_charity_item) ;
            // set listener for button delete
            cardView.setOnClickListener(this);
        }


        // xem chi tiết tổ chức từ thiện
        @Override
        public void onClick(View v) {
            Log.e("Click card","Thanh công");
            final Context context = parent;
            int pos = getAdapterPosition();
            SharedPreferences sharedPreferences= parent.getSharedPreferences("PosActivity", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("positionB", pos);
            editor.apply();

            FragmentManager fm = ((Activity) context).getFragmentManager();
            Log.e("Packedname",parent.getClass().getName() );
            if(parent.getClass().getName().equals("com.firebase.woflfish.goodbox.HomeCharityOrganizationActivity")) {
                fm.beginTransaction()
                    .replace(R.id.content_frame_home_charity,new DetailDonationCharityActivity())
                    .commit();
            }
            else{
                fm.beginTransaction()
                        .replace(R.id.content_frame_home_donor,new DetailDonationDonorActivity())
                        .commit();
            }
        }
    }
}
