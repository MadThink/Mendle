package com.example.kayda.mendle.Areas.Requests.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kayda.mendle.Areas.Requests.Adapters.RequestsListAdapter;
import com.example.kayda.mendle.Areas.Requests.Models.Request;
import com.example.kayda.mendle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private FirebaseUser mCurrent_user;
    private DatabaseReference mUsersDatabase;
    private RecyclerView mRecyclerView;
    private RequestsListAdapter mRequestsListAdapter;
    private List<Request> mRequestsList;
    private RecyclerView.LayoutManager mLayoutManager;

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference();

        mLayoutManager = new LinearLayoutManager(container.getContext());

        mRequestsList = new ArrayList<Request>();
        mRequestsListAdapter = new RequestsListAdapter(container.getContext(), mRequestsList);

        mRecyclerView = view.findViewById(R.id.fragment_requests_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mRequestsListAdapter);

        mUsersDatabase.child("Users").child(mCurrent_user.getUid()).child("Requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Request request = postSnapshot.getValue(Request.class);
                    if(request.RequestStatus == "received") {
                        mRequestsList.add(request);
                        mRequestsListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return view;
    }

}
