package com.reynagagroup.ryelloshopping.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.WishlistModel;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private boolean fromSearch;
    private List<WishlistModel> wishlistModelList;
    private Boolean wishlist;
    private int lastPosition = -1;

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public boolean isFromSearch() {
        return fromSearch;
    }

    public WishlistAdapter(List<WishlistModel> wishlistModelList, Boolean wishlist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist = wishlist;
    }

    public List<WishlistModel> getWishlistModelList() {
        return wishlistModelList;
    }

    public void setWishlistModelList(List<WishlistModel> wishlistModelList) {
        this.wishlistModelList = wishlistModelList;
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
        String oriPrice = wishlistModelList.get(position).getOriPrice();
        Boolean COD = wishlistModelList.get(position).getCOD();
        Boolean inStock = wishlistModelList.get(position).getInStock();
        long offersApplied = wishlistModelList.get(position).getOffersApplied();
        String satuan = wishlistModelList.get(position).getSatuan();

        viewHolder.SetData(productId,resource,freeCoupon,title,ratting,totalRattings,productPrice,oriPrice,COD,position,inStock,offersApplied,satuan);


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
        private TextView oriPrice;
        private ImageButton deleteBtn;
        private TextView satuan;


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
            oriPrice = itemView.findViewById(R.id.ori_price);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
            satuan = itemView.findViewById(R.id.satuan);
        }

        private void SetData(final String productId, String resource, long freeCouponNo, String title, String avarageRate, long totalRattingNo, String price, String oriPriceValue, Boolean COD, final int index,Boolean inStock,long offersApplied,String satuanText){

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.load)).into(productImage);
            Log.i("image",resource);
            productTitle.setText(title);
            if(freeCouponNo !=0 && inStock){
                couponIcon.setVisibility(View.VISIBLE);
                freeCoupon.setVisibility(View.VISIBLE);
                if (freeCouponNo ==1){
                    freeCoupon.setText("free "+ freeCouponNo + " coupon");
                }else {
                    freeCoupon.setText("free "+freeCouponNo+" coupon");
                }

            }else {
                couponIcon.setVisibility(View.INVISIBLE);
                freeCoupon.setVisibility(View.INVISIBLE);
            }
            LinearLayout linearLayout = (LinearLayout) ratting.getParent();
            if (!satuanText.equals("")) {
                satuan.setVisibility(View.VISIBLE);
                satuan.setText(" /"+satuanText);
            }else {
                satuan.setVisibility(View.GONE);
            }

            if (inStock){
                ratting.setVisibility(View.VISIBLE);
                totalRattings.setVisibility(View.VISIBLE);
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(Color.parseColor("#000000"));
                oriPrice.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);


                ratting.setText(avarageRate);
                totalRattings.setText("("+totalRattingNo+")rattings");
                productPrice.setText("Rp."+price);


                if (offersApplied>0) {
                    oriPrice.setText("Rp." + oriPriceValue );
                    priceCut.setVisibility(View.VISIBLE);
                    oriPrice.setVisibility(View.VISIBLE);
                }else {
                    priceCut.setVisibility(View.INVISIBLE);
                    oriPrice.setVisibility(View.INVISIBLE);
                }

            }else {
                linearLayout.setVisibility(View.INVISIBLE);
                ratting.setVisibility(View.INVISIBLE);
                totalRattings.setVisibility(View.INVISIBLE);
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                oriPrice.setVisibility(View.INVISIBLE);
                priceCut.setVisibility(View.INVISIBLE);
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
                    if (fromSearch){
                        ProductDetailActivity.fromSearch = true;
                    }
                    Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                    productDetailsIntent.putExtra("productID",productId);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });
        }

    }

}
