package com.reynagagroup.ryelloshopping.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
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
import com.reynagagroup.ryelloshopping.Activity.DeliveryActivity;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.Activity.PaymentActivity;
import com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.CartItemModel;
import com.reynagagroup.ryelloshopping.model.RewardModel;

import java.text.SimpleDateFormat;
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
    private Dialog loadingDialog;

    public CartAdapter(Context context,RecyclerView cartItemsRecyclerView,LinearLayoutManager linearLayoutManager,List<CartItemModel> cartItemModelList,TextView cartTotalAmount,boolean showDeleteBtn,Dialog loadingDialog) {
        this.cartItemModelList = cartItemModelList;
        this.context=context;
        this.cartItemsRecyclerView = cartItemsRecyclerView;
        this.linearLayoutManager = linearLayoutManager;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
        this.loadingDialog = loadingDialog;
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
                String oriPrice = cartItemModelList.get(position).getOriPrice();
                Long offersApplied = cartItemModelList.get(position).getOffersApplied();
                Boolean inStock = cartItemModelList.get(position).getInStock();
                Long quantity = cartItemModelList.get(position).getProductQuantity();
                String satuan = cartItemModelList.get(position).getSatuan();

                ((CartItemViewholder)viewHolder).setItemDetails(context,resource,productID,title,quantity,freeCoupons,productPrice,oriPrice,offersApplied,position,inStock,satuan);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice =0;
                String deliveryPrice = "";
                int totalAmount ;
                int saveAmount = 0;


                for (int x=0;x<cartItemModelList.size();x++){

                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).getInStock()){
                        int qty = Integer.parseInt(String.valueOf(cartItemModelList.get(x).getProductQuantity()));
                        totalItems= totalItems + qty;
                        if (TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCouponId())){
                            totalItemPrice = totalItemPrice + (Integer.parseInt(cartItemModelList.get(x).getProductPrice())* qty);
                        }else {
                            totalItemPrice = totalItemPrice + (Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice())* qty);
                        }

                        if (!TextUtils.isEmpty(cartItemModelList.get(x).getOriPrice())){
                            saveAmount = saveAmount + (Integer.parseInt(cartItemModelList.get(x).getOriPrice())-Integer.parseInt(cartItemModelList.get(x).getProductPrice()))*qty;
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCouponId())){
                                saveAmount = saveAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice())-Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice()));
                            }
                        }else {
                            if (!TextUtils.isEmpty(cartItemModelList.get(x).getSelectedCouponId())){
                                saveAmount = saveAmount + (Integer.parseInt(cartItemModelList.get(x).getProductPrice())-Integer.parseInt(cartItemModelList.get(x).getDiscountedPrice()));
                            }
                        }
                    }
                }


                if (totalItemPrice > 500000 || totalItemPrice == 0){
                    PaymentActivity.isfree = true;
                    deliveryPrice="FREE";
                    totalAmount = totalItemPrice;
                }else {
                    PaymentActivity.isfree = false;
                    deliveryPrice="20000";
                    totalAmount = totalItemPrice + 20000;
                }

                cartItemModelList.get(position).setTotalItems(totalItems);
                cartItemModelList.get(position).setTotalItemsPrice(totalItemPrice);
                cartItemModelList.get(position).setDeliveryPrice(deliveryPrice);
                cartItemModelList.get(position).setTotalAmount(totalAmount);
                cartItemModelList.get(position).setSavedAmount(saveAmount);



                ((CartTotalAmountViewholder)viewHolder).setTotalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount,saveAmount,position);
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
        private TextView oriPrice;
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
        private View divider;
        private ConstraintLayout couponLayout;
        private String productOriginalPrice;
        private TextView couponRedeemptionBody;
        private TextView satuan,satuan2;
        ////coupondialog

        public CartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            freeCouponsIcon = itemView.findViewById(R.id.free_coupon_icon);
            freeCoupons = itemView.findViewById(R.id.tv_free_coupon);
            productPrice = itemView.findViewById(R.id.product_price);
            oriPrice = itemView.findViewById(R.id.ori_price);
            offersApplied = itemView.findViewById(R.id.offers_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            couponRedeemptionLayout = itemView.findViewById(R.id.coupen_redemption_layout);
            couponsApplied = itemView.findViewById(R.id.coupon_applied);
            redeemBtn = itemView.findViewById(R.id.coupen_redemption_btn);
            deleteBtn = itemView.findViewById(R.id.remove_item_btn);
            divider = itemView.findViewById(R.id.price_cut_divider);
            couponRedeemptionBody = itemView.findViewById(R.id.tv_coupen_redemption);
            satuan = itemView.findViewById(R.id.satuan);
            satuan2 = itemView.findViewById(R.id.satuan2);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void  setItemDetails(final Context context, String resource, String productID, String title, Long quantity, Long freeCouponsNo, final String productPriceText, final String oriPriceText, final Long offersAppliedNo, final int position, Boolean inStock, String satuanText) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.load)).into(productImage);
            productTitle.setText(title);


            final Dialog checkCouponPriceDialog = new Dialog(itemView.getContext());
            checkCouponPriceDialog.setContentView(R.layout.coupon_redeem_dialog);
            checkCouponPriceDialog.setCancelable(true);
            checkCouponPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);


            if (!satuanText.equals("")){
                satuan.setText(satuanText);
                satuan2.setText("/"+satuanText);
                satuan.setVisibility(View.VISIBLE);
                satuan2.setVisibility(View.VISIBLE);
            }else {
                satuan.setVisibility(View.GONE);
                satuan2.setVisibility(View.GONE);
            }
            if (inStock) {
                couponRedeemptionLayout.setVisibility(View.VISIBLE);
                productQuantity.setVisibility(View.VISIBLE);
                freeCoupons.setVisibility(View.VISIBLE);
                freeCouponsIcon.setVisibility(View.VISIBLE);
                couponsApplied.setVisibility(View.INVISIBLE);

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

                productPrice.setText("Rp." + productPriceText );
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                if (offersAppliedNo > 0){
                    divider.setVisibility(View.VISIBLE);
                    oriPrice.setVisibility(View.VISIBLE);

                }else {
                    divider.setVisibility(View.GONE);
                    oriPrice.setVisibility(View.GONE);

                }
                oriPrice.setText("Rp." + oriPriceText );

                ///////coupon dialog


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
                MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(position,DBqueries.rewardModelList,true,couponRecyclerView,selectedCoupon,productOriginalPrice,coupontitle,couponexpiryDate,couponCouponBody1,coupondiscount,discountLayout,couponLayout,tv_SK,discountedPrice,cartItemModelList);
                couponRecyclerView.setAdapter(myRewardsAdapter);
                myRewardsAdapter.notifyDataSetChanged();
                ///for  coupon dialog

                applyCouponBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {
                            for (RewardModel rewardModel : DBqueries.rewardModelList) {
                                if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                                    rewardModel.setAlreadyUsed(true);
                                    couponRedeemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_gradient_background));
                                    couponRedeemptionBody.setText(rewardModel.getCouponBody());
                                    redeemBtn.setText("Coupen");
                                }
                            }
                            couponsApplied.setVisibility(View.VISIBLE);
                            cartItemModelList.get(position).setDiscountedPrice(discountedPrice.getText().toString().substring(3,discountedPrice.getText().length()));
                            cartItemModelList.get(position).setCouponsApplied(Long.parseLong("1"));
                            productPrice.setText(discountedPrice.getText());
                            String  offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText)-Long.valueOf(discountedPrice.getText().toString().substring(3,discountedPrice.getText().length())));
                            couponsApplied.setText("Coupen applied -Rp." + offerDiscountedAmt);
                            notifyItemChanged(cartItemModelList.size()-1);
                            checkCouponPriceDialog.dismiss();
                        }
                    }
                });

                removeCouponBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (RewardModel rewardModel: DBqueries.rewardModelList){
                            if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())){
                                rewardModel.setAlreadyUsed(false);
                            }
                        }
                        discountedPrice.setText("");
                        coupontitle.setText("My Reward");
                        cartItemModelList.get(position).setCouponsApplied(Long.parseLong("0"));
                        couponexpiryDate.setVisibility(View.INVISIBLE);
                        couponCouponBody1.setText("Klik icon di pojok kanan atas untuk memilih kupon Anda");
                        tv_SK.setVisibility(View.INVISIBLE);
                        couponsApplied.setVisibility(View.INVISIBLE);
                        couponLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.border_background));
                        discountLayout.setVisibility(View.GONE);
                        couponRedeemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                        couponRedeemptionBody.setText("Apply your coupen here");
                        redeemBtn.setText("Redeem");
                        productPrice.setText("Rp."+productPriceText);
                        cartItemModelList.get(position).setSelectedCouponId(null);
                        notifyItemChanged(cartItemModelList.size()-1);
                        checkCouponPriceDialog.dismiss();
                    }
                });

                couponRecyclerView.setVisibility(View.GONE);
                selectedCoupon.setVisibility(View.VISIBLE);

                toogleRecyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogRecyclerView();
                    }
                });

                if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {
                    for (RewardModel rewardModel : DBqueries.rewardModelList) {
                        if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())) {
                            couponRedeemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_gradient_background));
                            couponRedeemptionBody.setText(rewardModel.getCouponBody());
                            redeemBtn.setText("Coupen");

                           couponCouponBody1.setText(rewardModel.getCouponBody());
                            if (rewardModel.getType().toUpperCase().equals("DISCOUNT")){
                                discountLayout.setVisibility(View.VISIBLE);
                                coupontitle.setText(rewardModel.getType().toUpperCase());
                                coupondiscount.setText(rewardModel.getDiscount()+"%");
                            }else {
                                discountLayout.setVisibility(View.GONE);
                                coupontitle.setText("POTONGAN Rp."+rewardModel.getDiscount());
                                coupondiscount.setText("Rp."+rewardModel.getDiscount());
                            }
                            couponLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_gradient_background));
                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM YYYY");
                            couponexpiryDate.setText("till " + simpleDateFormat.format(rewardModel.getValidity().toDate()));
                        }
                    }
                    discountedPrice.setText("Rp."+cartItemModelList.get(position).getDiscountedPrice());
                    couponsApplied.setVisibility(View.VISIBLE);
                    productPrice.setText("Rp."+cartItemModelList.get(position).getDiscountedPrice());
                    String  offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText)-Long.valueOf(cartItemModelList.get(position).getDiscountedPrice()));
                    couponsApplied.setText("Coupen applied -Rp." + offerDiscountedAmt);
                }else {
                    couponsApplied.setVisibility(View.INVISIBLE);
                    couponRedeemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                    couponRedeemptionBody.setText("Apply your coupen here");
                    redeemBtn.setText("Redeem");
                }

                /////coupondialog


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
                                        notifyItemChanged(cartItemModelList.size()-1);

                                        loadingDialog.show();

                                        cartItemModelList.get(position).setProductQuantity(Long.parseLong(quantityNo.getText().toString()));

                                        linearLayoutManager = new LinearLayoutManager(context);
                                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                        cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

                                        linearLayoutManager.scrollToPosition(cartItemModelList.size() - 1);
                                        cartAdapter.notifyDataSetChanged();
                                        loadingDialog.dismiss();
                                        quantityDialog.dismiss();
                                    }else {
                                        Toast.makeText(context,"Maximal pesan 5",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });


                        quantityDialog.show();

                    }
                });

                if(offersAppliedNo >0){
                    offersApplied.setVisibility(View.VISIBLE);
                    String  offerDiscountedAmt = String.valueOf(Long.valueOf(oriPriceText)-Long.valueOf(productPriceText));
                    offersApplied.setText("Offer applied - Rp."+offerDiscountedAmt);
                }else {
                    offersApplied.setVisibility(View.INVISIBLE);
                }

            }else {
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorAccent4));
                oriPrice.setText("");
                couponRedeemptionLayout.setVisibility(View.GONE);
               productQuantity.setVisibility(View.INVISIBLE);
                freeCoupons.setVisibility(View.INVISIBLE);
                freeCouponsIcon.setVisibility(View.GONE);

            }


            if (showDeleteBtn){
                deleteBtn.setVisibility(View.VISIBLE);
            }else {
                deleteBtn.setVisibility(View.GONE);
            }




            redeemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (RewardModel rewardModel: DBqueries.rewardModelList){
                        if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())){
                            rewardModel.setAlreadyUsed(false);
                        }
                    }
                    checkCouponPriceDialog.show();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!TextUtils.isEmpty(cartItemModelList.get(position).getSelectedCouponId())) {
                        for (RewardModel rewardModel: DBqueries.rewardModelList){
                            if (rewardModel.getCouponId().equals(cartItemModelList.get(position).getSelectedCouponId())){
                                rewardModel.setAlreadyUsed(false);
                            }
                        }
                    }

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

        private  void setTotalAmount(int totalItemText, int totalItemPriceText,String deliveryPriceText,int totalAmountText, int saveAmountText,int position){



            totalItems.setText("Price("+totalItemText+" items)");
            totalItemPrice.setText("Rp."+totalItemPriceText);
            if (deliveryPriceText.equals("FREE")) {
                deliveryPrice.setText(deliveryPriceText);
            }else {
                deliveryPrice.setText("Rp."+deliveryPriceText);
            }
            if (!(totalAmountText==0)) {
                totalAmount.setText("Rp." + totalAmountText );
                cartTotalAmount.setText("Rp." + totalAmountText );
                saveAmount.setText("You saved Rp." + saveAmountText +" on this order");
            }
            if (totalItemPriceText == 0){
                cartItemModelList.remove(cartItemModelList.size()-1);
            }
        }
    }
}
