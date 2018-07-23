package com.example.kayda.mendle.Areas.Requests.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kayda.mendle.Areas.Requests.Models.Request;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.ViewHolder> {

    public List<Request> RequestsList;
    public Context context;

    public void Requests(Context context, List<Request> RequestsList) {
        this.RequestsList = RequestsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public TextView mNameText;
        public CircleImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            //mNameText = (TextView) mView.findViewById(R.id.user_single_name);
            //mImage = (ImageView) mView.findViewById(R.id.user_single_image);


        }
    }
}
