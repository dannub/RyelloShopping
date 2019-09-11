package com.reynagagroup.ryelloshopping.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.reynagagroup.ryelloshopping.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.HorizontalProductScrollModel;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {

    List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    public GridProductLayoutAdapter(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }

    @Override
    public int getCount() {
        return horizontalProductScrollModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,null);
            view.setElevation(0);
            view.setBackgroundColor(Color.parseColor("#ffffff"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent(parent.getContext(), ProductDetailActivity.class);
                    productDetailsIntent.putExtra("productID",horizontalProductScrollModelList.get(position).getProductID());
                    parent.getContext().startActivity(productDetailsIntent);
                }
            });

            ImageView productImage = view.findViewById(R.id.hs_product_image);
            TextView productTitle = view.findViewById(R.id.hs_product_title);
            TextView productDescription = view.findViewById(R.id.hs_product_description);
            TextView productPrice = view.findViewById(R.id.hs_product_price);

            Glide.with(parent.getContext()).load(horizontalProductScrollModelList.get(position).getProductImage()).apply(new RequestOptions().placeholder(R.mipmap.icon2_round)).into(productImage);
            productTitle.setText(horizontalProductScrollModelList.get(position).getProductTitle());
            productDescription.setText(horizontalProductScrollModelList.get(position).getProductDescription());
            if(!horizontalProductScrollModelList.get(position).getProducPrice().equals("")){
                productPrice.setText("Rp."+horizontalProductScrollModelList.get(position).getProducPrice()+"/-");

            }else {
                productPrice.setText("");

            }


        }else {
            view = convertView;
        }
        return view;
    }
}
