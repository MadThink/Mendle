package com.example.kayda.mendle.Areas.Admin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.kayda.mendle.Areas.Admin.Models.Users;
import com.example.kayda.mendle.Areas.Profiles.Activity.ProfileActivity;
import com.example.kayda.mendle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Kayda on 3/17/2018.
 */

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder>{

    public List<Users> usersList;
    public Context context;
    private DatabaseReference mUsersDatabase;
    private ViewGroup viewParent;

    public UsersListAdapter(Context context, List<Users>usersList){
        this.usersList=usersList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_layout,parent,false);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference();
        viewParent=parent;
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
   // holder.mNameText.setText(usersList.get(position).getName());
    final String userId=usersList.get(position).userId;

       mUsersDatabase.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               Users user = dataSnapshot.getValue(Users.class);
               String name = user.name;
               String image = user.image;

               holder.setUsersData(name, image);

               holder.mView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent = new Intent(viewParent.getContext(), ProfileActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                       intent.putExtra("user_id", userId);

                       viewParent.getContext().startActivity(intent);
                       //viewParent.getContext().startActivity(new Intent(context, ProfileActivity.class));
                   }
               });
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {
               
           }
       });
    }

    @Override
    public int getItemCount() {
        return  usersList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        View mView;
        public TextView mNameText;
        public CircleImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //mNameText = (TextView) mView.findViewById(R.id.user_single_name);
            //mImage = (ImageView) mView.findViewById(R.id.user_single_image);


        }

        public void setUsersData(String name, String image) {

            mNameText = mView.findViewById(R.id.user_single_name);
            mImage = mView.findViewById(R.id.user_single_image);

            mNameText.setText(name);

            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.ic_account_circle_black_24dp);

            Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(image).into(mImage);

        }
    }
}
