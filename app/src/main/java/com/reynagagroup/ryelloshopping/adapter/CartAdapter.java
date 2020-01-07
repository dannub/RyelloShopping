package com.reynagagroup.ryelloshopping.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.Activity.PaymentActivity;
import com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.CartItemModel;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private  int lastPosition = -1;
    private Context context;
    private  TextView cartTotalAmount;
    private boolean showDeleteBtn;
    private CartAdapter cartAdapter;
    private RecyclerView cartItemsRecyclerView;
    private LinearLayoutManager linearLayoutManager;

    public CartAdapter(Context context,RecyclerView cartItemsRecyclerView,LinearLayoutManager linearLayoutManager,List<CartItemModel> cartItemModelList,TextView cartTotalAmount,boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.context=context;
        this.cartItemsRecyclerView = cartItemsRecyclerView;
        this.linearLayoutManager = linearLayoutManager;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }
    public void SetAdapter(CartAdapter cartAdapter){
        this.cartAdapter=cartAdapter;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()){
            case 0:
                return  CartItemModel.CART_ITEM;
            case 1:
                return  CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType){
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_layout,viewGroup,false);
                return new CartItemViewholder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_total_amaount_layout,viewGroup,false);
                return new CartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                String productID = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                Long freeCoupons = cartItemModelList.get(position).getFreeCoupons();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                String cuttedPrice = cartItemModelList.get(position).getCuttedPrice();
                Long offersApplied = cartItemModelList.get(position).getOffersApplied();
                Boolean inStock = cartItemModelList.get(position).getInStock();
                Long quantity = cartItemModelList.get(position).getProductQuantity();

                ((CartItemViewholder)viewHolder).setItemDetails(context,resource,productID,title,quantity,freeCoupons,productPrice,cuttedPrice,offersApplied,position,inStock);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice =0;
                String deliveryPrice ;
                int totalAmount ;
                int saveAmount = 0;


                for (int x=0;x<cartItemModelList.size();x++){

                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).getInStock()){
                        if (cartItemModelList.get(x).getProductQuantity()==1){
                            totalItems++;
                        }else {
                            totalItems=totalItems+Integer.parseInt(Long.toString(cartItemModelList.get(x).getProductQuantity()));
                        }

                        totalItemPrice = totalItemPrice + (Integer.parseInt(cartItemModelList.get(x).getProductPrice())* Integer.parseInt(Long.toString(cartItemModelList.get(x).getProductQuantity())));
                    }
                }


                if (totalItemPrice > 500000 || totalItemPrice == 0){
                    PaymentActivity.isfree = true;
                    deliveryPrice = "FREE";
                    totalAmount = totalItemPrice;
                }else {
                    PaymentActivity.isfree = false;
                    deliveryPrice="50000";
                    totalAmount = totalItemPrice + 50000;
                }
                ((CartTotalAmountViewholder)viewHolder).setTotalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount,saveAmount);
                break;
            default:
                return;
        }

        if (lastPosition <position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewholder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private  ImageView freeCouponsIcon;
        private TextView productTitle;
        private TextView freeCoupons;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView offersApplied;
        private TextView couponsApplied;
        private TextView productQuantity;
        private LinearLayout couponRedeemptionLayout;

        private LinearLayout deleteBtn;
        private Button redeemBtn;


        ////coupondialog
        private TextView coupontitle;
        private TextView couponexpiryDate;
        private TextView coupondiscount;
        private TextView couponCouponBody1;
        private RecyclerView couponRecyclerView;
        private LinearLayout selectedCoupon;
        private LinearLayout discountLayout;
        private LinearLayout applyOrRemoveBtnContainer;
        private TextView footerText;
        private TextView tv_SK;
        private TextView discountedPrice;
        private TextView originalPrice;
        private Button removeCouponBtn;
        private Button applyCouponBtn;
        private ConstraintLayout couponLayout;
        private String productOriginalPrice;
        ////coupondialog

        public CartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCouponsIcon = itemView.findViewById(R.id.free_coupon_icon);
            freeCoupons = itemView.findViewById(R.id.tv_free_coupon);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            couponRedeemptionLayout = itemView.findViewById(R.id.coupen_redemption_layout);
            couponsApplied = itemView.findViewById(R.id.coupon_applied);
            redeemBtn = itemView.findViewById(R.id.coupen_redemption_btn);
            deleteBtn = itemView.findViewById(R.id.remove_item_btn);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void  setItemDetails(final Context context, String resource, String productID, String title, Long quantity, Long freeCouponsNo, final String productPriceText, String cuttedPriceText, final Long offersAppliedNo, final int position, Boolean inStock) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.load)).into(productImage);
            productTitle.setText(title);



            if (inStock) {
                couponRedeemptionLayout.setVisibility(View.VISIBLE);
                productQuantity.setVisibility(View.VISIBLE);
                freeCoupons.setVisibility(View.VISIBLE);
                couponsApplied.setVisibility(View.VISIBLE);
                offersApplied.setVisibility(View.VISIBLE);
                freeCouponsIcon.setVisibility(View.VISIBLE);

                if (freeCouponsNo > 0) {
                    freeCouponsIcon.setVisibility(View.VISIBLE);
                    freeCoupons.setVisibility(View.VISIBLE);
                    if (freeCouponsNo == 1) {
                        freeCoupons.setText("free " + freeCouponsNo + " Coupon");
                    } else {
                        freeCoupons.setText("free " + freeCouponsNo + " Coupon");
                    }

                } else {
                    freeCouponsIcon.setVisibility(View.INVISIBLE);
                    freeCoupons.setVisibility(View.INVISIBLE);
                }

                productPrice.setText("Rp." + productPriceText + "/-");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                cuttedPrice.setText("Rp." + cuttedPriceText + "/-");
                couponRedeemptionLayout.setVisibility(View.VISIBLE);
                productQuantity.setText("Qty: "+Long.toString(quantity));
                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);
                        final EditText quantityNo = quantityDialog.findViewById(R.id.quantity_no);
                        Button cancelbtn =  quantityDialog.findViewById(R.id.cancel_btn);
                        Button okbtn =   quantityDialog.findViewById(R.id.ok_btn);

                        cancelbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantityDialog.dismiss();
                            }
                        });

                        okbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!quantityNo.getText().toString().isEmpty()) {
                                    if (Integer.parseInt(quantityNo.getText().toString())<=5) {
                                        productQuantity.setText("Qty: " + quantityNo.getText());
                                        DBqueries.cartItemModelList.get(position).setProductQuantity(Long.parseLong(quantityNo.getText().toString()));

                                        linearLayoutManager = new LinearLayoutManager(context);
                                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

                                        linearLayoutManager.scrollToPosition(cartItemModelList.size() - 1);
                                        cartAdapter.notifyDataSetChanged();
                                        quantityDialog.dismiss();
                                    }else {
                                        Toast.makeText(context,"Maximal pesan 5",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        quantityDialog.show();
                        if(offersAppliedNo >0){
                            offersApplied.setVisibility(View.VISIBLE);
                            offersApplied.setText(offersAppliedNo + " offers applied");
                        }else {
                            offersApplied.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }else {
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent4));
                cuttedPrice.setText("");
                couponRedeemptionLayout.setVisibility(View.GONE);
               productQuantity.setVisibility(View.INVISIBLE);
                freeCoupons.setVisibility(View.INVISIBLE);
                couponsApplied.setVisibility(View.GONE);
                freeCouponsIcon.setVisibility(View.GONE);
                offersApplied.setVisibility(View.GONE);
            }


            if (showDeleteBtn){
                deleteBtn.setVisibility(View.VISIBLE);
            }else {
                deleteBtn.setVisibility(View.GONE);
            }

            redeemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ///////coupon dialog

                    final Dialog checkCouponPriceDialog = new Dialog(itemView.getContext());
                    checkCouponPriceDialog.setContentView(R.layout.coupon_redeem_dialog);
                    checkCouponPriceDialog.setCancelable(true);
                    checkCouponPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                    ImageView toogleRecyclerView = checkCouponPriceDialog.findViewById(R.id.toogle_recyclreview);
                    couponRecyclerView = checkCouponPriceDialog.findViewById(R.id.coupons_recyclerview);
                    selectedCoupon = checkCouponPriceDialog.findViewById(R.id.selected_coupon);

                    coupontitle = checkCouponPriceDialog.findViewById(R.id.reward_title);
                    couponexpiryDate = checkCouponPriceDialog.findViewById(R.id.reward_till_date);
                    couponCouponBody1 = checkCouponPriceDialog.findViewById(R.id.reward_body1);
                    coupondiscount = checkCouponPriceDialog.findViewById(R.id.discount_reward);
                    discountLayout = checkCouponPriceDialog.findViewById(R.id.discount_layout);
                    couponLayout = checkCouponPriceDialog.findViewById(R.id.coupon_layout);
                    removeCouponBtn = checkCouponPriceDialog.findViewById(R.id.remove_btn);
                    applyCouponBtn = checkCouponPriceDialog.findViewById(R.id.apply_btn);
                    applyOrRemoveBtnContainer = checkCouponPriceDialog.findViewById(R.id.apply_or_remove_btn_container);
                    footerText = checkCouponPriceDialog.findViewById(R.id.footer_text);

                    tv_SK = checkCouponPriceDialog.findViewById(R.id.tv_SK);

                    footerText.setVisibility(View.GONE);
                    applyOrRemoveBtnContainer.setVisibility(View.VISIBLE);



                    originalPrice = checkCouponPriceDialog.findViewById(R.id.original_price);
                    discountedPrice = checkCouponPriceDialog.findViewById(R.id.discounted_price);



                    couponLayout.setBackground(itemView.getResources().getDrawable(R.drawable.border_background));
                    discountLayout.setVisibility(View.GONE);
                    tv_SK.setVisibility(View.INVISIBLE);
                    couponexpiryDate.setVisibility(View.INVISIBLE);


                    LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    couponRecyclerView.setLayoutManager(layoutManager);

                    ///for  coupon dialog
                    originalPrice.setText(productPrice.getText());
                    productOriginalPrice = productPriceText;
                    MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(DBqueries.rewardModelList,true,couponRecyclerView,selectedCoupon,productOriginalPrice,coupontitle,couponexpiryDate,couponCouponBody1,coupondiscount,discountLayout,tv_SK,couponLayout,discountedPrice);
                    couponRecyclerView.setAdapter(myRewardsAdapter);
                    myRewardsAdapter.notifyDataSetChanged();
                    ///for  coupon dialog


                    couponRecyclerView.setVisibility(View.GONE);
                    selectedCoupon.setVisibility(View.VISIBLE);

                    toogleRecyclerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialogRecyclerView();
                        }
                    });
                    checkCouponPriceDialog.show();

                    /////coupondialog

                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ProductDetailActivity.running_cart_query){
                        ProductDetailActivity.running_cart_query = true;
                        DBqueries.removeFromCart(position,itemView.getContext());
                    }
                }
            });


        }

        private void showDialogRecyclerView(){
            if (couponRecyclerView.getVisibility() == View.GONE){
                couponRecyclerView.setVisibility(View.VISIBLE);
                selectedCoupon.setVisibility(View.GONE);
            }else {
                couponRecyclerView.setVisibility(View.GONE);
                selectedCoupon.setVisibility(View.VISIBLE);
            }
        }

    }

    class  CartTotalAmountViewholder extends RecyclerView.ViewHolder{

        private  TextView totalItems;
        private  TextView totalItemPrice;
        private  TextView deliveryPrice;
        private  TextView totalAmount;
        private  TextView saveAmount;


        public CartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);
            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            saveAmount = itemView.findViewById(R.id.saved_amount);
        }

        private  void setTotalAmount(int totalItemText, int totalItemPriceText,String deliveryPriceText,int totalAmountText, int saveAmountText){
            totalItems.setText("Price("+totalItemText+" items)");
            totalItemPrice.setText("Rp."+totalItemPriceText+"/-");
            if (deliveryPriceText.equals("FREE")) {
                deliveryPrice.setText(deliveryPriceText);
            }else {
                deliveryPrice.setText("Rp."+deliveryPriceText+"/-");
            }
            if (!(totalAmountText==0)) {
                totalAmount.setText("Rp." + totalAmountText + "/-");
                cartTotalAmount.setText("Rp." + totalAmountText + "/-");
                saveAmount.setText("You saved Rp." + saveAmountText + "/- on this order");
            }
            if (totalItemPriceText == 0){
                DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size()-1);
            }
        }
    }
}
