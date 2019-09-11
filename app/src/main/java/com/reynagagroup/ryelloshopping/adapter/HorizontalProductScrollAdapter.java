package com.reynagagroup.ryelloshopping.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.reynagagroup.ryelloshopping.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.HorizontalProductScrollModel;

import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter<HorizontalProductScrollAdapter.ViewHolder> {

    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public HorizontalProductScrollAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.ViewHolder viewHolder, int position) {

        String resource = horizontalProductScrollModelList.get(position).getProductImage();
        String title = horizontalProductScrollModelList.get(position).getProductTitle();
        String description = horizontalProductScrollModelList.get(position).getProductDescription();
        String price = horizontalProductScrollModelList.get(position).getProducPrice();
        String productId = horizontalProductScrollModelList.get(position).getProductID();
        viewHolder.setData(productId,resource,title,price,description);

    }

    @Override
    public int getItemCount() {
        if(horizontalProductScrollModelList.size()>8){
            return 8;
        }else {
            return horizontalProductScrollModelList.size();
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView productDescription;
        private TextView productPrice;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.hs_product_image);
            productTitle = itemView.findViewById(R.id.hs_product_title);
            productDescription = itemView.findViewById(R.id.hs_product_description);
            productPrice = itemView.findViewById(R.id.hs_product_price);

        }

        private  void  setData(final String productId, String resource, String title, String price, String description){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.icon2_round)).into(productImage);
            productTitle.setText(title);
            productDescription.setText(description);
            if(!price.equals("")){
                productPrice.setText("Rp."+price+"/-");
            }else {
                productPrice.setText("");
            }

            if(!title.equals("")) {
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
}
