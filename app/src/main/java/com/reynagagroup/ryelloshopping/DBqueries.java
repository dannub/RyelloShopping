package com.reynagagroup.ryelloshopping;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reynagagroup.ryelloshopping.Activity.AddAddressActivity;
import com.reynagagroup.ryelloshopping.Activity.DeliveryActivity;
import com.reynagagroup.ryelloshopping.Activity.MainActivity;
import com.reynagagroup.ryelloshopping.Activity.PaymentActivity;
import com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.adapter.CategoryAdapter;
import com.reynagagroup.ryelloshopping.adapter.HomePageAdapter;
import com.reynagagroup.ryelloshopping.model.AddressModel;
import com.reynagagroup.ryelloshopping.model.CartItemModel;
import com.reynagagroup.ryelloshopping.model.CategoryModel;
import com.reynagagroup.ryelloshopping.model.HomePageModel;
import com.reynagagroup.ryelloshopping.model.HorizontalProductScrollModel;
import com.reynagagroup.ryelloshopping.model.RewardModel;
import com.reynagagroup.ryelloshopping.model.SliderModel;
import com.reynagagroup.ryelloshopping.model.WishlistModel;
import com.reynagagroup.ryelloshopping.ui.MyCartFragment;
import com.reynagagroup.ryelloshopping.ui.MyRewardsFragment;
import com.reynagagroup.ryelloshopping.ui.MyWishlistFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity.addToWishlistBtn;

import static com.reynagagroup.ryelloshopping.ui.HomeFragment.swipeRefreshLayout;
import static com.reynagagroup.ryelloshopping.ui.MyCartFragment.cartItemsRecyclerView;
import static com.reynagagroup.ryelloshopping.ui.MyCartFragment.cartlistAdapter;
import static com.reynagagroup.ryelloshopping.ui.MyCartFragment.continueBtn;
import static com.reynagagroup.ryelloshopping.ui.MyCartFragment.linearLayoutManager;
import static com.reynagagroup.ryelloshopping.ui.MyCartFragment.loadingDialog;
import static com.reynagagroup.ryelloshopping.ui.MyCartFragment.recyclerViewState;
import static com.reynagagroup.ryelloshopping.ui.MyCartFragment.totalAmount;

public class DBqueries {


    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static  List<String> loadedCategoriesNames = new ArrayList<>();
    public static List<String> wishlist = new ArrayList<>();
    public static List<WishlistModel> wishlistModelList = new ArrayList<>();

    public static List<String> myRatedIds = new ArrayList<>();
    public static List<Long> myRating = new ArrayList<>();

    public static List<String> cartlist = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();

    public static int selectedAddress = -1;
    private static long size;
    public static List<AddressModel> addressModelList = new ArrayList<>();

    public static List<RewardModel> rewardModelList = new ArrayList<>();

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
                                        if ((long)documentSnapshot.get("offers_applied_"+ x)>0) {
                                            vieAllProductList.add(new WishlistModel(
                                                    documentSnapshot.get("product_ID_" + x).toString()
                                                    , documentSnapshot.get("product_image_" + x).toString()
                                                    , documentSnapshot.get("product_title_" + x).toString() + " " + documentSnapshot.get("product_subtitle_" + x).toString()
                                                    , (long) documentSnapshot.get("free_coupon_" + x)
                                                    , documentSnapshot.get("average_rating_" + x).toString()
                                                    , (long) documentSnapshot.get("total_ratings_" + x)
                                                    , documentSnapshot.get("cutted_price_" + x).toString()
                                                    , documentSnapshot.get("product_price_" + x).toString()
                                                    , (Boolean) documentSnapshot.get("COD_" + x)
                                                    , (Boolean) documentSnapshot.get("in_stock_" + x)
                                                    , (long) documentSnapshot.get("offers_applied_"+ x)
                                            ));
                                        }else {
                                            vieAllProductList.add(new WishlistModel(
                                                    documentSnapshot.get("product_ID_" + x).toString()
                                                    , documentSnapshot.get("product_image_" + x).toString()
                                                    , documentSnapshot.get("product_title_" + x).toString() + " " + documentSnapshot.get("product_subtitle_" + x).toString()
                                                    , (long) documentSnapshot.get("free_coupon_" + x)
                                                    , documentSnapshot.get("average_rating_" + x).toString()
                                                    , (long) documentSnapshot.get("total_ratings_" + x)
                                                    , documentSnapshot.get("product_price_" + x).toString()
                                                    ,""
                                                    , (Boolean) documentSnapshot.get("COD_" + x)
                                                    , (Boolean) documentSnapshot.get("in_stock_" + x)
                                                    , (long) documentSnapshot.get("offers_applied_"+ x)
                                            ));
                                        }
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
                                        if ((Long)task.getResult().get("offers_applied")>0) {
                                            wishlistModelList.add(new WishlistModel(
                                                    productId, task.getResult().get("product_image_1").toString()
                                                    , task.getResult().get("product_title").toString()
                                                    , (long) task.getResult().get("free_coupon")
                                                    , task.getResult().get("average_ratting").toString()
                                                    , (long) task.getResult().get("total_ratings")
                                                    , task.getResult().get("cutted_price").toString()
                                                    , task.getResult().get("product_price").toString()
                                                    , (Boolean) task.getResult().get("COD")
                                                    , (Boolean) task.getResult().get("in_stock")
                                                    ,(Long)task.getResult().get("offers_applied")
                                            ));
                                        }else {
                                            wishlistModelList.add(new WishlistModel(
                                                    productId, task.getResult().get("product_image_1").toString()
                                                    , task.getResult().get("product_title").toString()
                                                    , (long) task.getResult().get("free_coupon")
                                                    , task.getResult().get("average_ratting").toString()
                                                    , (long) task.getResult().get("total_ratings")
                                                    , task.getResult().get("product_price").toString()
                                                    , ""
                                                    , (Boolean) task.getResult().get("COD")
                                                    , (Boolean) task.getResult().get("in_stock")
                                                    ,(Long)task.getResult().get("offers_applied")
                                            ));
                                        }
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
        final String removedProductID = wishlist.get(index);
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
                    wishlist.add(index,removedProductID);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                ProductDetailActivity.running_wishlist_query =false;
            }
        });

    }

    public static void  loadRatingList(final Context context){
        if (ProductDetailActivity.running_rating_query) {
            ProductDetailActivity.running_rating_query =true;
            myRatedIds.clear();
            myRating.clear();
            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                    .document("MY_RATINGS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                            myRatedIds.add(task.getResult().get("product_ID_" + x).toString());
                            myRating.add((long) task.getResult().get("rating_" + x));
                            if (task.getResult().get("product_ID_" + x).toString().equals(ProductDetailActivity.productID) ) {
                                ProductDetailActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if (ProductDetailActivity.rateNowContainer != null) {
                                    ProductDetailActivity.setRatting(ProductDetailActivity.initialRating);
                                }
                            }
                        }

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                    ProductDetailActivity.running_rating_query=false;
                }
            });
        }


    }


    public static void loadCartList(final Context context, final Dialog dialog, final boolean loadProductData, final TextView badgeCount,final TextView cartTotalAmount){

        cartlist.clear();
        cartItemModelList.clear();


            dialog.show();



        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){



                    int x;
                    size = (long)task.getResult().get("list_size");
                    for (x = 0;x<size;x++){

                        if (loadProductData) {
                        }
                        if (!loadProductData) {
                            cartlist.add(task.getResult().get("product_ID_" + x).toString());
                        }


                        if (DBqueries.cartlist.contains(ProductDetailActivity.productID)){
                            ProductDetailActivity.ALREADY_ADDED_TO_CART = true;
                        }else {
                            ProductDetailActivity.ALREADY_ADDED_TO_CART = false;
                        }



                            if (loadProductData) {

                            final String productId = task.getResult().get("product_ID_" + x).toString();

                                final int finalX = x;

                                firebaseFirestore.collection("PRODUCTS").document(productId)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        cartlist.add(productId);

                                        int index = 0;
                                        if (cartlist.size() >= 2){
                                            index = cartlist.size()-2;
                                        }
                                        if ((Long)task.getResult().get("offers_applied")>0) {
                                            cartItemModelList.add(index, new CartItemModel(CartItemModel.CART_ITEM,
                                                    productId, task.getResult().get("product_image_1").toString()
                                                    , task.getResult().get("product_title").toString()
                                                    , (long) task.getResult().get("free_coupon")
                                                    , task.getResult().get("cutted_price").toString()
                                                    , task.getResult().get("product_price").toString()
                                                    , (long) 1
                                                    , (long) task.getResult().get("offers_applied")
                                                    , (long) 0
                                                    , (Boolean) task.getResult().get("in_stock")
                                            ));
                                        }else {
                                            cartItemModelList.add(index, new CartItemModel(CartItemModel.CART_ITEM,
                                                    productId, task.getResult().get("product_image_1").toString()
                                                    , task.getResult().get("product_title").toString()
                                                    , (long) task.getResult().get("free_coupon")
                                                    , task.getResult().get("product_price").toString()
                                                    , ""
                                                    , (long) 1
                                                    , (long) task.getResult().get("offers_applied")
                                                    , (long) 0
                                                    , (Boolean) task.getResult().get("in_stock")
                                            ));
                                        }
                                        String temp = cartlist.remove(cartlist.size()-1);
                                        cartlist.add(index, temp);

                                        if (finalX == size-1){
                                            cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                                            cartlistAdapter.notifyDataSetChanged();
                                            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                            parent.setVisibility(View.VISIBLE);

                                            continueBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (!totalAmount.getText().toString().replace(" ","").equals("Rp.-/-")) {

                                                        MyCartFragment.mycartfragment = MyCartFragment.this_fragment;

                                                        DeliveryActivity.cartItemModelList = new ArrayList<>();
                                                        PaymentActivity.fromCart = true;

                                                        for (int x = 0; x < DBqueries.cartItemModelList.size() - 1; x++) {
                                                            CartItemModel cartItemModel = DBqueries.cartItemModelList.get(x);
                                                            if (cartItemModel.getInStock()) {
                                                                DeliveryActivity.cartItemModelList.add(cartItemModel);
                                                            }
                                                        }
                                                        DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                                                        dialog.show();
                                                        if (DBqueries.addressModelList.size() == 0) {
                                                            DBqueries.loadAddresses(context, dialog);
                                                        } else {
                                                            dialog.dismiss();
                                                            Intent deliveryIntent = new Intent(context, DeliveryActivity.class);
                                                            context.startActivity(deliveryIntent);
                                                        }
                                                    }else {
                                                        MyCartFragment.mycartfragment = null;
                                                        Toast.makeText(context,"Stock is Empty!",Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                            dialog.dismiss();
                                        }

                                        if (cartlist.size() == 1 ) {


                                        }




                                        if (cartlist.size() == 0) {


                                            cartItemModelList.clear();
                                            continueBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    MyCartFragment.mycartfragment = null;
                                                    Toast.makeText(context,"Cart is Empty!",Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                        cartlistAdapter.notifyDataSetChanged();
                                        recyclerViewState = cartItemsRecyclerView.getLayoutManager().onSaveInstanceState();
                                        cartItemsRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                                        //linearLayoutManager.scrollToPosition(cartlist.size() - 1);
                                        cartItemsRecyclerView.smoothScrollToPosition(0);
                                        linearLayoutManager.scrollToPositionWithOffset(0, 0);







                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });



                        }
                        MainActivity.loadingDialog.dismiss();



                    }

                    if (x==0){
                        MainActivity.loadingDialog.dismiss();
                        dialog.dismiss();
                    }






                    if (!loadProductData) {
                        if (size != 0) {
                            badgeCount.setVisibility(View.VISIBLE);
                        } else {
                            badgeCount.setVisibility(View.INVISIBLE);
                        }
                        if (size < 99) {
                            badgeCount.setText(String.valueOf(size));
                        } else {
                            badgeCount.setText("99+");
                        }
                        dialog.dismiss();
                    }



                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public static void removeFromCart(final int index, final Context context){
        final String removedProductID = cartlist.get(index);
        cartlist.remove(index);
        Map<String ,Object> updateCartlist = new HashMap<>();

        for (int x = 0;x<cartlist.size();x++){
            updateCartlist.put("product_ID_"+x,cartlist.get(x));
        }
        updateCartlist.put("list_size",(long)cartlist.size());

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_CART")
                .set(updateCartlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if (cartItemModelList.size()>1){
                        cartItemModelList.remove(index);
                        cartlistAdapter.notifyDataSetChanged();
                        continueBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DeliveryActivity.cartItemModelList = new ArrayList<>();
                                MyCartFragment.mycartfragment = MyCartFragment.this_fragment;

                                for (int x = 0;x< DBqueries.cartItemModelList.size()-1;x++){
                                    CartItemModel cartItemModel = DBqueries.cartItemModelList.get(x);
                                    Log.i("daa",cartItemModel.getProductID());
                                    if (cartItemModel.getInStock()){
                                        DeliveryActivity.cartItemModelList.add(cartItemModel);
                                    }
                                }
                                DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));

                                loadingDialog.show();
                                if (DBqueries.addressModelList.size()==0){
                                    DBqueries.loadAddresses(context,loadingDialog);
                                }else {
                                    loadingDialog.dismiss();
                                    Intent deliveryIntent = new Intent(context,DeliveryActivity.class);
                                    context.startActivity(deliveryIntent);
                                }

                            }
                        });

                    }
                    if (cartItemModelList.size() ==1)
                     {
                        cartItemModelList.clear();
                        totalAmount.setText("Rp. -/-");
                        continueBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyCartFragment.mycartfragment = null;
                                Toast.makeText(context,"Cart is Empty!",Toast.LENGTH_SHORT).show();
                            }
                        });
                        cartlistAdapter.notifyDataSetChanged();
                    }

                    Toast.makeText(context,"Removed successfully!",Toast.LENGTH_SHORT).show();
                }else {
                    cartlist.add(index,removedProductID);
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                ProductDetailActivity.running_cart_query =false;
            }
        });

    }

    public static void loadAddresses(final Context context, final Dialog loadingDialog){
        addressModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_ADDRESSES").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Intent deliveryIntent;
                    if ((long)task.getResult().get("list_size")==0){
                        deliveryIntent = new Intent(context, AddAddressActivity.class);
                        deliveryIntent.putExtra("INTENT","deliveryIntent");
                    }else {

                        for (long x = 1;x < (long)task.getResult().get("list_size")+1;x++){
                            addressModelList.add(new AddressModel(task.getResult().get("fullname_"+x).toString(),
                                    task.getResult().get("address_"+x).toString(),
                                    task.getResult().get("pincode_"+x).toString(),
                                    task.getResult().get("phone_"+x).toString(),
                                    (Boolean) task.getResult().get("selected_"+x)));
                            if ((Boolean) task.getResult().get("selected_"+x)){
                                selectedAddress = Integer.parseInt(String.valueOf(x -1));
                            }
                        }
                        deliveryIntent = new Intent(context, DeliveryActivity.class);

                    }
                    context.startActivity(deliveryIntent);
                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }
        });
    }

    public  static void loadRewards(final Context context, final Dialog loadingDialog, final Boolean onRewardFragment){
        rewardModelList.clear();
        loadingDialog.show();

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            final Date lastseenDate = task.getResult().getDate("Last seen");

                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_REWARDS").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()){
                                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                                    if (documentSnapshot.get("type").toString().toLowerCase().equals("discount") && lastseenDate.before(documentSnapshot.getDate("validity"))){
                                                        rewardModelList.add(new RewardModel(documentSnapshot.getId(),documentSnapshot.get("type").toString()
                                                                ,documentSnapshot.get("lower_limit").toString()
                                                                ,documentSnapshot.get("upper_limit").toString()
                                                                ,documentSnapshot.get("precentage").toString()
                                                                ,documentSnapshot.get("body").toString().replace("\\n", "\n")
                                                                ,(Timestamp) documentSnapshot.get("validity")
                                                                ,(Boolean) documentSnapshot.get("already_used")
                                                        ));
                                                    }else if(documentSnapshot.get("type").toString().equals("Potongan Rp.*") && lastseenDate.before(documentSnapshot.getDate("validity"))){
                                                        rewardModelList.add(new RewardModel(documentSnapshot.getId(),documentSnapshot.get("type").toString()
                                                                ,documentSnapshot.get("lower_limit").toString()
                                                                ,documentSnapshot.get("upper_limit").toString()
                                                                ,documentSnapshot.get("amount").toString()
                                                                ,documentSnapshot.get("body").toString().replace("\\n", "\n")
                                                                ,(Timestamp) documentSnapshot.get("validity")
                                                                ,(Boolean) documentSnapshot.get("already_used")
                                                        ));
                                                    }
                                                }
                                                if (onRewardFragment) {
                                                    MyRewardsFragment.myRewardsAdapter.notifyDataSetChanged();
                                                    loadingDialog.dismiss();
                                                }
                                            }else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else {
                            loadingDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public static void clearData(){
        categoryModelList.clear();
        if (!MainActivity.showCart) {
            lists.clear();
        }
        loadedCategoriesNames.clear();
        wishlist.clear();
        wishlistModelList.clear();
        myRating.clear();
        myRatedIds.clear();
        cartlist.clear();
        cartItemModelList.clear();
        addressModelList.clear();
        rewardModelList.clear();
    }



}
