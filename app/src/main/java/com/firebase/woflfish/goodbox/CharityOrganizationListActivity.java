package com.firebase.woflfish.goodbox;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharityOrganizationListActivity extends Fragment {
// TODO: Xem link http://www.nguyenvanquan7826.com/2015/05/31/android-recyclerview-trong-android/ phần code Java
    // TODO: CardView là charity_organization_list_item.xml; Recycle View là activity_charity_orgnization_list.xml
    // TODO: Tạo thêm 1 class RecyclerAdapter nha! Class đó để truyền dữ liệu vào RecyclerView. Thay List item trong Recycler(link trên) thành CardView nha
    View myView;
    private List<CharityOrganization> data;
    private List<String> uids;
    private CharityOrganizationRecyclerAdapter charityOrganizationRecyclerAdapter;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_charity_organization_list, container, false);
        data = new ArrayList<CharityOrganization>();
        uids = new ArrayList<String>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //RecyclerView
        recyclerView = (RecyclerView) myView.findViewById(R.id.charity_organization_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(myView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        ReadCharityList();
        //setting adater
        charityOrganizationRecyclerAdapter = new CharityOrganizationRecyclerAdapter(this.getActivity());
        recyclerView.setAdapter(charityOrganizationRecyclerAdapter);
        return myView;
    }
    private void ReadCharityList(){

        mDatabase.child("Users").child("CharityOrganizations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String uid = postSnapshot.getKey();
                    CharityOrganization dataUser = postSnapshot.getValue(CharityOrganization.class);
                    Log.e("Debug bobobo: ",uid);
                    uids.add(uid);
                    data.add(dataUser);
                }
                if(data.size()>0) {
                    for(int i =0;i<data.size();i++)
                        charityOrganizationRecyclerAdapter.setAdapter(data.get(i));
                }
                recyclerView.setAdapter(charityOrganizationRecyclerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

