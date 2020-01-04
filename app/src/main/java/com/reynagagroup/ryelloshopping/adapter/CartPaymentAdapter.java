package com.reynagagroup.ryelloshopping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

                ((CartItemViewholder)viewHolder).setItemDetails(productID,title,quantity,productPrice,position);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice =0;
                String deliveryPrice ;
                int totalAmount ;
                int saveAmount = 0;


                for (int x=0;x<cartItemModelList.size();x++){

                    if (cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM){
                        if (cartItemModelList.get(x).getProductQuantity()==1){
                            totalItems++;
                        }else {
                            totalItems=totalItems+ Integer.parseInt(Long.toString(cartItemModelList.get(x).getProductQuantity()));
                        }

                        totalItemPrice = totalItemPrice + (Integer.parseInt(cartItemModelList.get(x).getProductPrice())* Integer.parseInt(Long.toString(cartItemModelList.get(x).getProductQuantity())));
                    }
                }


                if (totalItemPrice > 500000 || totalItemPrice == 0){
                    deliveryPrice = "Gratis";
                    totalAmount = totalItemPrice;
                }else {
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

    class CartItemViewholder extends RecyclerView.ViewHolder {

        private TextView productTitle;
        private TextView productPrice;
        private TextView productQuantity;

        public CartItemViewholder(View cartItemView) {
            super(cartItemView);
            productTitle = cartItemView.findViewById(R.id.nama_barang);
            productPrice = cartItemView.findViewById(R.id.hrg_barang);
            productQuantity = cartItemView.findViewById(R.id.jml_barang);
        }

        private void setItemDetails(String productID, String title, Long quantity, String productPriceText, int position) {
            productTitle.setText(title);
            productPrice.setText("Rp."+productPriceText);
            productQuantity.setText("x"+ Long.toString(quantity));
        }
    }

    class CartTotalAmountViewholder extends RecyclerView.ViewHolder{

        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;

        public CartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);
            totalItems = itemView.findViewById(R.id.total_items);
            totalItemPrice = itemView.findViewById(R.id.total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
        }

        private  void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText, int saveAmountText){
            totalItems.setText("Subtotal untuk ("+totalItemText+" item)");
            totalItemPrice.setText("Rp."+totalItemPriceText+"/-");
            if (deliveryPriceText.equals("Gratis")) {
                deliveryPrice.setText(deliveryPriceText);
            }else {
                deliveryPrice.setText("Rp."+deliveryPriceText+"/-");
            }
            if (!(totalAmountText==0)) {
                totalAmount.setText("Rp." + totalAmountText + "/-");
            }
        }


    }
}
