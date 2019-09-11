package com.reynagagroup.ryelloshopping.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.ProductSpesificationModel;

import java.util.List;

public class ProductSpesificationAdapter  extends RecyclerView.Adapter<ProductSpesificationAdapter.ViewHolder> {
    private List<ProductSpesificationModel> productSpesificationModelList;

    public ProductSpesificationAdapter(List<ProductSpesificationModel> productSpesificationModelList) {
        this.productSpesificationModelList = productSpesificationModelList;
    }

    @NonNull
    @Override
    public ProductSpesificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i){
            case  ProductSpesificationModel.SPECIFICATION_TITLE:
                TextView title = new TextView(viewGroup.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setUp(16,viewGroup.getContext())
                        ,setUp(16,viewGroup.getContext())
                        ,setUp(16,viewGroup.getContext())
                        ,setUp(8,viewGroup.getContext()));

                title.setLayoutParams(layoutParams);
                return new ViewHolder(title);

            case  ProductSpesificationModel.SPECIFICATION_BODY:
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_spesification_item_layout,viewGroup,false);
                return new ViewHolder(view);
            default:
                return null;
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (productSpesificationModelList.get(position).getType()){
            case 0:
                return ProductSpesificationModel.SPECIFICATION_TITLE;
            case 1:
                return ProductSpesificationModel.SPECIFICATION_BODY;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSpesificationAdapter.ViewHolder viewHolder, int position) {

        switch (productSpesificationModelList.get(position).getType()){
            case ProductSpesificationModel.SPECIFICATION_TITLE:
                    viewHolder.setTitle(productSpesificationModelList.get(position).getTitle());
                    break;
            case ProductSpesificationModel.SPECIFICATION_BODY:
                String featureTitle = productSpesificationModelList.get(position).getFeatureName();
                String featureDetail = productSpesificationModelList.get(position).getFeatureValue();

                viewHolder.setFeatures(featureTitle,featureDetail);
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemCount() {
        return productSpesificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView featureName;
        private TextView featureValue;
        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        public void setTitle(String titleText) {
            title = (TextView)itemView;
            title.setText(titleText);
        }

        public void setFeatures(String featureTitle, String featureDetail) {
            featureName = itemView.findViewById(R.id.feature_name);
            featureValue = itemView.findViewById(R.id.feature_value);
            featureName.setText(featureTitle);
            featureValue.setText(featureDetail);
        }
    }

    private  int setUp(int dp, Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }
}
