package com.reynagagroup.ryelloshopping.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reynagagroup.ryelloshopping.Activity.PaymentActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.CartItemModel;

import java.util.List;

public class CartPaymentAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;


    public CartPaymentAdapter(List<CartItemModel> cartItemModelList) {
        this.cartItemModelList = cartItemModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()){
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_cart_item,viewGroup,false);
                return new CartItemViewholder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_cart_total,viewGroup,false);
                return new CartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                String productID = cartItemModelList.get(position).getProductID();
                String title = cartItemModelList.get(position).getProductTitle();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                Long quantity = cartItemModelList.get(position).getProductQuantity();
                String oriPrice = cartItemModelList.get(position).getOriPrice();
                String discountedPrice = cartItemModelList.get(position).getDiscountedPrice();
                Long offersApplied = cartItemModelList.get(position).getOffersApplied();
                Long couponsApplied=cartItemModelList.get(position).getCouponsApplied();


                ((CartItemViewholder)viewHolder).setItemDetails(productID,title,quantity,oriPrice,productPrice,discountedPrice,offersApplied,couponsApplied);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice =0;
                String deliveryPrice ;
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
                    deliveryPrice = "FREE";
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


                ((CartTotalAmountViewholder)viewHolder).setTotalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount,saveAmount);

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

    class CartItemViewholder extends RecyclerView.ViewHolder {

        private TextView productTitle;
        private TextView productPrice;
        private TextView productQuantity;
        private TextView discount;
        private TextView voucher;

        public CartItemViewholder(View cartItemView) {
            super(cartItemView);
            productTitle = cartItemView.findViewById(R.id.nama_barang);
            productPrice = cartItemView.findViewById(R.id.hrg_barang);
            productQuantity = cartItemView.findViewById(R.id.jml_barang);
            discount = cartItemView.findViewById(R.id.discounted);
            voucher = cartItemView.findViewById(R.id.couponed);
        }

        private void setItemDetails(String productID, String title, Long quantity, String oriPriceText,String productPriceText,String discountedPriceText, Long offerApplied,Long coupenApplied) {
            productTitle.setText(title);
            productPrice.setText("Rp."+productPriceText);
            productQuantity.setText("x"+ Long.toString(quantity));
            if (offerApplied>0) {
                String discountText = "Discount - Rp." + Long.toString((Long.parseLong(oriPriceText) - Long.parseLong(productPriceText)));
                discount.setText(discountText);
                discount.setVisibility(View.VISIBLE);

            }else {
                discount.setVisibility(View.GONE);
            }
            if (coupenApplied>0){
                String voucherText = "Voucher - Rp." + Long.toString((Long.parseLong(productPriceText) - Long.parseLong(discountedPriceText)));
                voucher.setText(voucherText);
                voucher.setVisibility(View.VISIBLE);
                productPrice.setText("Rp."+discountedPriceText);

            }else {
                voucher.setVisibility(View.GONE);
            }
        }
    }

    class CartTotalAmountViewholder extends RecyclerView.ViewHolder{

        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;
        private TextView savedAmount;


        public CartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);
            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
            savedAmount = itemView.findViewById(R.id.saveAmount);

        }

        private  void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText, int saveAmountText){
            totalItems.setText("Subtotal untuk ("+totalItemText+" item)");
            totalItemPrice.setText("Rp."+totalItemPriceText+"/-");
            if (deliveryPriceText.equals("FREE")) {
                deliveryPrice.setText(deliveryPriceText);
            }else {
                deliveryPrice.setText("Rp."+deliveryPriceText+"/-");
            }
            if (!(totalAmountText==0)) {
                totalAmount.setText("Rp." + totalAmountText + "/-");
                savedAmount.setText("You saved Rp." + saveAmountText + "/- on this order");
            }

        }


    }
}
