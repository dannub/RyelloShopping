package com.reynagagroup.ryelloshopping;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reynagagroup.ryelloshopping.adapter.CartAdapter;
import com.reynagagroup.ryelloshopping.adapter.CategoryAdapter;
import com.reynagagroup.ryelloshopping.adapter.HomePageAdapter;
import com.reynagagroup.ryelloshopping.model.CategoryModel;
import com.reynagagroup.ryelloshopping.model.HomePageModel;
import com.reynagagroup.ryelloshopping.model.HorizontalProductScrollModel;
import com.reynagagroup.ryelloshopping.model.SliderModel;
import com.reynagagroup.ryelloshopping.model.WishlistModel;
import com.reynagagroup.ryelloshopping.ui.MyWishlistFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.reynagagroup.ryelloshopping.ProductDetailActivity.addToWishlistBtn;

import static com.reynagagroup.ryelloshopping.ui.HomeFragment.swipeRefreshLayout;

public class DBqueries {

    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static  List<String> loadedCategoriesNames = new ArrayList<>();
    public static List<String> wishlist = new ArrayList<>();
    public static List<WishlistModel> wishlistModelList = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();

    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context){
        categoryModelList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                if (!documentSnapshot.get("categoryName").toString().equals("Home")){
                                    categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(),documentSnapshot.get("categoryName").toString()));
                                }
                            }
                            CategoryAdapter categoryAdapter= new CategoryAdapter(categoryModelList);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public static void loadFragmentData(final RecyclerView homePageRecyclerView, final Context context, final int index, String categoryName){
        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){

                                if ((long)documentSnapshot.get("view_type")==0){
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banners = (long)documentSnapshot.get("no_of_banners");

                                    for (long x = 1;x<no_of_banners+1;x++){
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_"+x).toString()
                                                ,documentSnapshot.get("banner_"+x+"_background").toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(0,sliderModelList));
                                }else if ((long)documentSnapshot.get("view_type")==1){
                                    lists.get(index).add(new HomePageModel(1,documentSnapshot.get("strip_ad_banner").toString(),
                                            documentSnapshot.get("background").toString()));
                                }else if ((long)documentSnapshot.get("view_type")==2){


                                    List<WishlistModel> vieAllProductList = new ArrayList<>();
                                    List<HorizontalProductScrollModel>horizontalProductScrollModelList = new ArrayList<>();
                                    long no_of_products = (long)documentSnapshot.get("no_of_products");

                                    for (long x = 1;x<no_of_products+1;x++){
                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(
                                                documentSnapshot.get("product_ID_"+x).toString()
                                                ,documentSnapshot.get("product_image_"+x).toString()
                                                ,documentSnapshot.get("product_title_"+x).toString()
                                                ,documentSnapshot.get("product_subtitle_"+x).toString()
                                                ,documentSnapshot.get("product_price_"+x).toString()
                                        ));

                                        vieAllProductList.add(new WishlistModel(
                                                documentSnapshot.get("product_ID_"+x).toString()
                                                ,documentSnapshot.get("product_image_"+x).toString()
                                                ,documentSnapshot.get("product_title_"+x).toString()+" "+documentSnapshot.get("product_subtitle_"+x).toString()
                                                ,(long)documentSnapshot.get("free_coupon_"+x)
                                                ,documentSnapshot.get("average_rating_"+x).toString()
                                                ,(long)documentSnapshot.get("total_ratings_"+x)
                                                ,documentSnapshot.get("product_price_"+x).toString()
                                                ,documentSnapshot.get("cutted_price_"+x).toString()
                                                ,(Boolean)documentSnapshot.get("COD_"+x)
                                        ));
                                    }
                                    lists.get(index).add(new HomePageModel(2,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),horizontalProductScrollModelList,vieAllProductList));

                                }else if ((long)documentSnapshot.get("view_type")==3){
                                    List<HorizontalProductScrollModel>GridLayoutModelList = new ArrayList<>();
                                    long no_of_products = (long)documentSnapshot.get("no_of_products");

                                    for (long x = 1;x<no_of_products+1;x++){
                                        GridLayoutModelList.add(new HorizontalProductScrollModel(
                                                documentSnapshot.get("product_ID_"+x).toString()
                                                ,documentSnapshot.get("product_image_"+x).toString()
                                                ,documentSnapshot.get("product_title_"+x).toString()
                                                ,documentSnapshot.get("product_subtitle_"+x).toString()
                                                ,documentSnapshot.get("product_price_"+x).toString()
                                        ));
                                    }
                                    lists.get(index).add(new HomePageModel(3,documentSnapshot.get("layout_title").toString(),documentSnapshot.get("layout_background").toString(),GridLayoutModelList));
                                }


                            }
                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                            homePageRecyclerView.setAdapter(homePageAdapter);
                            homePageAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public static void loadWishlist(final Context context, final Dialog dialog,final Boolean loadProductData ){
        wishlist.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    for (long x = 0;x<(long)task.getResult().get("list_size");x++){
                        wishlist.add(task.getResult().get("product_ID_"+x).toString());


                        if (DBqueries.wishlist.contains(ProductDetailActivity.productID)){
                            ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = true;
                            if (addToWishlistBtn!=null) {
                                ProductDetailActivity.addToWishlistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorAccent4));
                            }
                        }else {
                            if (addToWishlistBtn!=null) {
                                ProductDetailActivity.addToWishlistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorAccent3));
                            }
                            ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = false;
                        }

                        if (loadProductData) {
                            wishlistModelList.clear();
                            final String productId = task.getResult().get("product_ID_" + x).toString();
                            firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        wishlistModelList.add(new WishlistModel(
                                                productId,task.getResult().get("product_image_1").toString()
                                                , task.getResult().get("product_title").toString()
                                                , (long) task.getResult().get("free_coupon")
                                                , task.getResult().get("average_ratting").toString()
                                                , (long) task.getResult().get("total_ratings")
                                                , task.getResult().get("product_price").toString()
                                                , task.getResult().get("cutted_price").toString()
                                                , (Boolean) task.getResult().get("COD")
                                        ));
                                        MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();

                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }
    public static void removeFromWishlist(final int index, final Context context){
        wishlist.remove(index);
        Map<String ,Object> updateWishlist = new HashMap<>();

        for (int x = 0;x<wishlist.size();x++){
                updateWishlist.put("product_ID_"+x,wishlist.get(x));
        }
        updateWishlist.put("list_size",(long)wishlist.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_WISHLIST")
                .set(updateWishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if (wishlistModelList.size()!=0){
                        wishlistModelList.remove(index);
                        MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                    }
                    ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = false;
                    Toast.makeText(context,"Removed successfully!",Toast.LENGTH_SHORT).show();
                }else {
                    if (addToWishlistBtn!= null) {
                        addToWishlistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorAccent4));
                    }
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                ProductDetailActivity.running_wishlist_query =false;
            }
        });

    }

    public static void  loadRatingList(final Context context){
        myRatedIds.clear();
        myRating.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_RATINGS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    for (long x = 0;x<(long)task.getResult().get("list_size");x++){
                        myRatedIds.add(task.getResult().get("product_ID_"+x).toString());
                        myRating.add((long)task.getResult().get("rating_"+x));
                        if (task.getResult().get("product_ID_"+x).toString().equals(ProductDetailActivity.productID) && ProductDetailActivity.rateNowContainer!=null){
                            ProductDetailActivity.setRatting(Integer.parseInt(String.valueOf((long)task.getResult().get("rating_"+x))));
                        }
                    }

                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public static void clearData(){
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        wishlist.clear();
        wishlistModelList.clear();
    }
}
