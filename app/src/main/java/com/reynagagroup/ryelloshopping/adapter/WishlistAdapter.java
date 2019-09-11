package com.reynagagroup.ryelloshopping.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.WishlistModel;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<WishlistModel> wishlistModelList;
    private Boolean wishlist;
    private int lastPosition = -1;

    public WishlistAdapter(List<WishlistModel> wishlistModelList,Boolean wishlist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist = wishlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlist_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String productId = wishlistModelList.get(position).getProductID();
        String resource = wishlistModelList.get(position).getProductImage();
        String title = wishlistModelList.get(position).getProductTitle();
        long freeCoupon = wishlistModelList.get(position).getFreeCoupons();
        String ratting = wishlistModelList.get(position).getRatting();
        long totalRattings = wishlistModelList.get(position).getTotalRattings();
        String productPrice = wishlistModelList.get(position).getProductPrice();
        String cuttedPrice = wishlistModelList.get(position).getCuttedPrice();
        Boolean COD = wishlistModelList.get(position).getCOD();
        viewHolder.SetData(productId,resource,freeCoupon,title,ratting,totalRattings,productPrice,cuttedPrice,COD,position);


        if (lastPosition <position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private TextView productTitle;
        private TextView freeCoupon;
        private ImageView couponIcon;
        private TextView ratting;
        private View priceCut;
        private  TextView totalRattings;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView paymentMethod;
        private ImageButton deleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCoupon = itemView.findViewById(R.id.free_coupon);
            couponIcon = itemView.findViewById(R.id.coupon_icon);
            ratting = itemView.findViewById(R.id.tv_product_ratting_miniview);
            totalRattings = itemView.findViewById(R.id.total_ratting);
            priceCut = itemView.findViewById(R.id.price_cut);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            paymentMethod = itemView.findViewById(R.id.payment_method);
            deleteBtn = itemView.findViewById(R.id.delete_btn);

        }

        private void SetData(final String productId, String resource, long freeCouponNo, String title, String avarageRate, long totalRattingNo, String price, String cuttedPriceValue, Boolean COD, final int index){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.icon2_round)).into(productImage);
            productTitle.setText(title);
            if(freeCouponNo !=0){
                couponIcon.setVisibility(View.VISIBLE);
                if (freeCouponNo ==1){
                    freeCoupon.setText("free "+ freeCouponNo + " coupon");
                }else {
                    freeCoupon.setText("free "+freeCouponNo+" coupon");
                }

            }else {
                couponIcon.setVisibility(View.INVISIBLE);
                freeCoupon.setVisibility(View.INVISIBLE);
            }
            ratting.setText(avarageRate);
            totalRattings.setText("("+totalRattingNo+")rattings");
            productPrice.setText("Rp."+price+"/-");
            cuttedPrice.setText("Rp."+cuttedPriceValue+"/-");
            if(COD){
                paymentMethod.setVisibility(View.VISIBLE);
            }else {
                paymentMethod.setVisibility(View.GONE);
            }

            if (wishlist){
                deleteBtn.setVisibility(View.VISIBLE);
            }else {
                deleteBtn.setVisibility(View.GONE);
            }
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ProductDetailActivity.running_wishlist_query) {
                        ProductDetailActivity.running_wishlist_query = true;
                        DBqueries.removeFromWishlist(index, itemView.getContext());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                    productDetailsIntent.putExtra("productID",productId);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });
        }

    }

}
