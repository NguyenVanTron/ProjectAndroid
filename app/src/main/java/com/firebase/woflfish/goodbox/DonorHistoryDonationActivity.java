package com.firebase.woflfish.goodbox;

import android.app.Fragment;
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

public class DonorHistoryDonationActivity extends Fragment {
    View myView;
    private List<AtDropOffLocation> dataoLocation;
    private List<PickedUpFromHome> dataoHone;
    private List<String> uiddrop;
    private List<String> uidhome;
    private DonationHistoryListRecyclerAdapter donationHistoryListRecyclerAdapter;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.charity_view_history_donation_activity, container, false);
        dataoLocation = new ArrayList<AtDropOffLocation>();
        uiddrop = new ArrayList<String>();
        dataoHone = new ArrayList<PickedUpFromHome>();
        uidhome = new ArrayList<String>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //RecyclerView
        recyclerView = (RecyclerView) myView.findViewById(R.id.donation_history_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(myView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //setting adater
        ReadDataLocationList();
        ReadDataFromHomeList();
        donationHistoryListRecyclerAdapter = new DonationHistoryListRecyclerAdapter(this.getActivity());
        recyclerView.setAdapter(donationHistoryListRecyclerAdapter);
        return myView;
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
                SharedPreferences sharedPreferences= myView.getContext().getSharedPreferences("IdDonor", Context.MODE_PRIVATE);
                String iddonor;
                if(sharedPreferences!=null){
                    iddonor = sharedPreferences.getString("idDon","Donor");
                }else
                    iddonor = "";
                if (dataoLocation.size() > 0) {
                    for (int i = 0; i < dataoLocation.size(); i++)
                        if(dataoLocation.get(i).getIdDonor().equals(iddonor))
                            donationHistoryListRecyclerAdapter.setAdapter(dataoLocation.get(i));
                }
                recyclerView.setAdapter(donationHistoryListRecyclerAdapter);
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
                SharedPreferences sharedPreferences= myView.getContext().getSharedPreferences("IdDonor", Context.MODE_PRIVATE);
                String iddonor;
                if(sharedPreferences!=null){
                    iddonor = sharedPreferences.getString("idDon","Donor");
                }else
                    iddonor = "";
                if (dataoHone.size() > 0) {
                    for (int i = 0; i < dataoHone.size(); i++)
                        if(dataoHone.get(i).getIdDonor().equals(iddonor))
                            donationHistoryListRecyclerAdapter.setAdapter(dataoHone.get(i));
                }
                recyclerView.setAdapter(donationHistoryListRecyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
