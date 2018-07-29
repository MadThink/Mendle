package com.example.kayda.mendle.Areas.Requests.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.kayda.mendle.App_Start.MainActivity;
import com.example.kayda.mendle.Areas.Admin.Models.Users;
import com.example.kayda.mendle.Areas.Requests.Models.Request;
import com.example.kayda.mendle.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.ViewHolder> {

    public List<Request> RequestsList;
    public Context context;
    public Request mRequest;
    public FirebaseUser mCurrentUser;
    private DatabaseReference mUserDatabase;

    public RequestsListAdapter(Context context, List<Request> RequestsList) {
        this.RequestsList = RequestsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.request_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        mRequest = RequestsList.get(position);

        final String name = mRequest.UserRequestName;
        final String image = mRequest.UserRequestImage;

        holder.setUsersData(name, image);

        final Button mAcceptRequestButton = holder.mView.findViewById(R.id.accept_request_button);

        mAcceptRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                mUserDatabase = FirebaseDatabase.getInstance().getReference();

                mUserDatabase.child("Users").child(mCurrentUser.getUid()).child("Friends").child(mRequest.UserRequestId).setValue(true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mUserDatabase.child("Users").child(mRequest.UserRequestId).child("Friends").child(mCurrentUser.getUid()).setValue(true)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        RequestsList.remove(mRequest);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Your friend has been added!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                mUserDatabase.child("Requests").child(mCurrentUser.getUid()).child(mRequest.UserRequestId)
                        .child("RequestStatus").setValue("accepted");

                mUserDatabase.child("Users").child(mCurrentUser.getUid()).child("Requests").child(mRequest.UserRequestId).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mUserDatabase.child("Users").child(mRequest.UserRequestId).child("Requests").child(mCurrentUser.getUid())
                                .removeValue();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return RequestsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public TextView requestName;
        public ImageView requestImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setUsersData(String name, String image) {

            requestName = mView.findViewById(R.id.request_name);
            requestImage = mView.findViewById(R.id.request_image);

            requestName.setText(name);

            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.ic_account_circle_black_24dp);

            Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(image).into(requestImage);

        }
    }
}
