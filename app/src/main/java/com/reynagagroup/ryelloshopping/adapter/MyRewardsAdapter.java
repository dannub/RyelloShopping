package com.reynagagroup.ryelloshopping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reynagagroup.ryelloshopping.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.RewardModel;

import java.util.List;

public class MyRewardsAdapter extends RecyclerView.Adapter<MyRewardsAdapter.Viewholder> {

    private List<RewardModel> rewardModelList;
    private Boolean userMiniLayout = false;

    public MyRewardsAdapter(List<RewardModel> rewardModelList, Boolean userMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.userMiniLayout = userMiniLayout;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (userMiniLayout){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mini_rewards_item_layout,viewGroup,false);
        }else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rewards_item_layout,viewGroup,false);
        }
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {
        String title = rewardModelList.get(position).getTitle();
        String date = rewardModelList.get(position).getExpiryDate();
        String body1 = rewardModelList.get(position).getCouponBody1();
        String body2 = rewardModelList.get(position).getCouponBody2();
        String discount = rewardModelList.get(position).getDiscount();

        viewholder.SetData(title,date,discount,body1,body2);

    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView couponTitle;
        private TextView couponDiscount;
        private TextView couponExpiryDate;
        private TextView couponBody1;
        private TextView couponBody2;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            couponTitle = itemView.findViewById(R.id.reward_title);
            couponDiscount = itemView.findViewById(R.id.discount_reward);
            couponExpiryDate = itemView.findViewById(R.id.reward_till_date);
            couponBody1 = itemView.findViewById(R.id.reward_body1);
            couponBody2 = itemView.findViewById(R.id.reward_body2);

        }

        private void SetData(final String title, final String date, final String discount, final String body1, final String body2){
            couponTitle.setText(title);
            couponExpiryDate.setText(date);
            couponDiscount.setText(discount);
            couponBody1.setText(body1);
            couponBody2.setText(body2);

            if (userMiniLayout){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductDetailActivity.coupontitle.setText(title);
                        ProductDetailActivity.couponexpiryDate.setText(date);
                        ProductDetailActivity.couponCouponBody1.setText(body1);
                        ProductDetailActivity.couponCouponBody2.setText(body2);
                        ProductDetailActivity.coupondiscount.setText(discount);
                        ProductDetailActivity.showDialogRecyclerView();
                    }
                });
            }
        }
    }
}
