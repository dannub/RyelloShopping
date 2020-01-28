package com.reynagagroup.ryelloshopping.adapter;

import android.app.Notification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.reynagagroup.ryelloshopping.Activity.NotificationActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.NotificationModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private List<NotificationModel> notifiationModelList;

    public NotificationAdapter(List<NotificationModel> notifiationModelList) {
        this.notifiationModelList = notifiationModelList;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        String image = notifiationModelList.get(position).getImage();
        String body = notifiationModelList.get(position).getBody();
        boolean readed = notifiationModelList.get(position).isReaded();

        holder.setData(image,body,readed);

    }

    @Override
    public int getItemCount() {
        return notifiationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);
        }

        private void setData (String image,String body,boolean readed){
            if (image.equals("")) {
                Glide.with(itemView.getContext()).load(image).apply(new RequestOptions().placeholder(R.drawable.rewards)).into(imageView);
            }else {
                imageView.setImageDrawable(itemView.getResources().getDrawable(R.drawable.rewards));
            }
            if (readed){
                textView.setAlpha(0.5f);
            }else {
                textView.setAlpha(1f);
            }
            textView.setText(body);
        }
    }
}
