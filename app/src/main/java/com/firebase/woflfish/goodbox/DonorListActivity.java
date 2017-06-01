package com.firebase.woflfish.goodbox;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 1/11/2017.
 */

public class DonorListActivity extends Fragment{
    View myView;
    private List<DonationActivities> data;
    private List<String> uids;
    private ActivityListRecyclerAdapter activityListRecyclerAdapter;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.donor_list_activity, container, false);
        data = new ArrayList<DonationActivities>();
        uids = new ArrayList<String>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //RecyclerView
        recyclerView = (RecyclerView) myView.findViewById(R.id.donor_list_activity);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(myView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //setting adater
        ReadActivityList();
        ReadActivityDonationList();
        activityListRecyclerAdapter = new ActivityListRecyclerAdapter(this.getActivity());
        recyclerView.setAdapter(activityListRecyclerAdapter);


        return myView;
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
                    //if(datActivity.getIdCharity().equals(Idchar)) {
                        uids.add(uid);
                        data.add(datActivity);
                   // }
                }
                if(data.size()>0) {
                    for(int i =0;i<data.size();i++) {
                            activityListRecyclerAdapter.setAdapter(data.get(i));
                    }
                }
                recyclerView.setAdapter(activityListRecyclerAdapter);
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
                if(data.size()> size) {

                    for(int i = size;i<data.size();i++) {
                            activityListRecyclerAdapter.setAdapter(data.get(i));
                    }
                }
                recyclerView.setAdapter(activityListRecyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}