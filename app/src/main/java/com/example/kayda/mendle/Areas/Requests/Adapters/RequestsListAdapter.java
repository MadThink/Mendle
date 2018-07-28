package com.example.kayda.mendle.Areas.Requests.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.kayda.mendle.Areas.Requests.Models.Request;
import com.example.kayda.mendle.R;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.ViewHolder> {

    public List<Request> RequestsList;
    public Context context;
    public Request mRequest;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mRequest = RequestsList.get(position);
        final String name = mRequest.UserRequestName;
        final String image = mRequest.UserRequestImage;

        holder.setUsersData(name, image);
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
