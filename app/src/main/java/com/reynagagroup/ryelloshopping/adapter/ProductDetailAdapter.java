package com.reynagagroup.ryelloshopping.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.reynagagroup.ryelloshopping.fragment.ProductDescriptionFragment;
import com.reynagagroup.ryelloshopping.fragment.ProductSpesificationFragment;
import com.reynagagroup.ryelloshopping.model.ProductSpesificationModel;

import java.util.List;

public class ProductDetailAdapter extends FragmentPagerAdapter {

    private int totalTabs;
    private String productDescription;
    private String productOtherDetails;
    private List<ProductSpesificationModel> productSpesificationModelList;

    public ProductDetailAdapter(FragmentManager fm, int totalTabs,String productDescription, String productOtherDetails, List<ProductSpesificationModel> productSpesificationModelList) {
        super(fm);
        this.productDescription = productDescription;
        this.productOtherDetails = productOtherDetails;
        this.productSpesificationModelList = productSpesificationModelList;
        this.totalTabs = totalTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ProductDescriptionFragment productDescriptionFragment1 = new ProductDescriptionFragment();
                productDescriptionFragment1.body = productDescription;
                return productDescriptionFragment1;
            case 1:
                ProductSpesificationFragment productSpesificationFragment = new ProductSpesificationFragment();
                productSpesificationFragment.productSpesificationModelList = productSpesificationModelList;
                return productSpesificationFragment;
            case 2:
                ProductDescriptionFragment productDescriptionFragment2 = new ProductDescriptionFragment();
                productDescriptionFragment2.body = productOtherDetails;
                return productDescriptionFragment2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
