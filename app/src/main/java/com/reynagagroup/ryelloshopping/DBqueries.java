package com.reynagagroup.ryelloshopping;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reynagagroup.ryelloshopping.Activity.AddAddressActivity;
import com.reynagagroup.ryelloshopping.Activity.DeliveryActivity;
import com.reynagagroup.ryelloshopping.Activity.MainActivity;
import com.reynagagroup.ryelloshopping.Activity.NotificationActivity;
import com.reynagagroup.ryelloshopping.Activity.PaymentActivity;
import com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.adapter.CartAdapter;
import com.reynagagroup.ryelloshopping.adapter.CategoryAdapter;
import com.reynagagroup.ryelloshopping.adapter.HomePageAdapter;
import com.reynagagroup.ryelloshopping.adapter.MyOrderAdapter;
import com.reynagagroup.ryelloshopping.model.AddressModel;
import com.reynagagroup.ryelloshopping.model.CartItemModel;
import com.reynagagroup.ryelloshopping.model.CategoryModel;
import com.reynagagroup.ryelloshopping.model.HomePageModel;
import com.reynagagroup.ryelloshopping.model.HorizontalProductScrollModel;
import com.reynagagroup.ryelloshopping.model.MyOrderItemModel;
import com.reynagagroup.ryelloshopping.model.NotificationModel;
import com.reynagagroup.ryelloshopping.model.RewardModel;
import com.reynagagroup.ryelloshopping.model.SliderModel;
import com.reynagagroup.ryelloshopping.model.UploadBuktiModel;
import com.reynagagroup.ryelloshopping.model.WishlistModel;
import com.reynagagroup.ryelloshopping.fragment.ui.MyCartFragment;
import com.reynagagroup.ryelloshopping.fragment.ui.MyOrdersFragment;
import com.reynagagroup.ryelloshopping.fragment.ui.MyRewardsFragment;
import com.reynagagroup.ryelloshopping.fragment.ui.MyWishlistFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity.addToWishlistBtn;

import static com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity.productID;
import static com.reynagagroup.ryelloshopping.fragment.ui.HomeFragment.swipeRefreshLayout;
import static com.reynagagroup.ryelloshopping.fragment.ui.MyCartFragment.cartItemsRecyclerView;
import static com.reynagagroup.ryelloshopping.fragment.ui.MyCartFragment.cartlistAdapter;
import static com.reynagagroup.ryelloshopping.fragment.ui.MyCartFragment.continueBtn;
import static com.reynagagroup.ryelloshopping.fragment.ui.MyCartFragment.linearLayoutManager;
import static com.reynagagroup.ryelloshopping.fragment.ui.MyCartFragment.loadingDialog;
import static com.reynagagroup.ryelloshopping.fragment.ui.MyCartFragment.recyclerViewState;
import static com.reynagagroup.ryelloshopping.fragment.ui.MyCartFragment.totalAmount;

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

    public static ListenerRegistration registration;

    public  static String fullname,email,profile;

    public static NotificationManager notifManager;

    public static Date lastseenDate;



    public static int selectedAddress = -1;
    private static long size;
    public static List<AddressModel> addressModelList = new ArrayList<>();

    public static List<RewardModel> rewardModelList = new ArrayList<>();

    public static List<MyOrderItemModel> myOrderItemModelArrayList = new ArrayList<>();

    public static List<NotificationModel> notificationModelList = new ArrayList<>();

    public static   int iterasi_order_1,n_order_1,iterasi_order_2,n_order_2;



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

                                        Log.i("saasas",documentSnapshot.get("satuan"+1).toString());

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
                                                    , documentSnapshot.get("satuan" + x).toString()

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
                                                    , documentSnapshot.get("satuan" + x).toString()

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
        wishlistModelList.clear();

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    if ((long)task.getResult().get("list_size")>0) {

                        if (loadProductData){
                            MyWishlistFragment.noData.setVisibility(View.GONE);
                            MyWishlistFragment.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
                            MyWishlistFragment.no_internet.setVisibility(View.GONE);
                            MyWishlistFragment.wistListRecyclerView.setVisibility(View.VISIBLE);

                        }

                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                            wishlist.add(task.getResult().get("product_ID_" + x).toString());


                            if (DBqueries.wishlist.contains(ProductDetailActivity.productID)) {
                                ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = true;
                                if (addToWishlistBtn != null) {
                                    ProductDetailActivity.addToWishlistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorAccent4));
                                }
                            } else {
                                if (addToWishlistBtn != null) {
                                    ProductDetailActivity.addToWishlistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorAccent3));
                                }
                                ProductDetailActivity.ALREADY_ADDED_TO_WISHLIST = false;
                            }

                            if (loadProductData) {
                                final String productId = task.getResult().get("product_ID_" + x).toString();
                                firebaseFirestore.collection("PRODUCTS").document(productId)
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if ((Long) task.getResult().get("offers_applied") > 0) {
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
                                                        , (Long) task.getResult().get("offers_applied")
                                                        , task.getResult().get("satuan").toString()

                                                ));
                                            } else {
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
                                                        , (Long) task.getResult().get("offers_applied")
                                                        , task.getResult().get("satuan").toString()

                                                ));
                                            }


                                            if(wishlistModelList.size()>1) {
                                                Collections.sort(wishlistModelList, new Comparator<WishlistModel>() {
                                                    @Override
                                                    public int compare(WishlistModel o1, WishlistModel o2) {
                                                        if (o1.getProductTitle() == null || o2.getProductTitle() == null)
                                                            return 0;
                                                        return o1.getProductTitle().compareTo(o2.getProductTitle());

                                                    }

                                                });

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
                        if (loadProductData){
                            MyWishlistFragment.no_internet.setVisibility(View.GONE);
                            MyWishlistFragment.noData.setVisibility(View.VISIBLE);
                            MyWishlistFragment.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                            MyWishlistFragment.wistListRecyclerView.setVisibility(View.GONE);
                            dialog.dismiss();
                            wishlistModelList.clear();
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

    public static void  loadRatingList(final Context context, @Nullable final Dialog loadindDialog){


        if (!ProductDetailActivity.running_rating_query) {
            ProductDetailActivity.running_rating_query =true;
            myRatedIds.clear();
            myRating.clear();
            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                    .document("MY_RATINGS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        List<String> orderProductIds = new ArrayList<>();
                        for (int x = 0; x<myOrderItemModelArrayList.size();x++){
                            orderProductIds.add(myOrderItemModelArrayList.get(x).getProductID());
                        }

                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                            myRatedIds.add(task.getResult().get("product_ID_" + x).toString());
                            myRating.add((long) task.getResult().get("rating_" + x));

                            if (task.getResult().get("product_ID_" + x).toString().equals(ProductDetailActivity.productID) ) {
                                ProductDetailActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if (ProductDetailActivity.rateNowContainer != null) {
                                    ProductDetailActivity.setRatting(ProductDetailActivity.initialRating);

                                }
                            }

                            if (orderProductIds.contains(task.getResult().get("product_ID_"+x).toString())){
                               myOrderItemModelArrayList.get(orderProductIds.indexOf(task.getResult().get("product_ID_"+x).toString())).setRatting(Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1);
                            }
                        }

                        if (MyOrdersFragment.myOrderAdapter!= null){
                            MyOrdersFragment.myOrderAdapter.notifyDataSetChanged();
                        }

                        if (loadindDialog!=null) {
                            loadindDialog.dismiss();
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

    public static void loadCartList(final Context context, final Dialog dialog, final boolean loadProductData, final TextView badgeCount, final TextView cartTotalAmount,final boolean loadProductDetail, final LinearLayout addCart){

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
                    if (size>0) {

                        if (loadProductData) {

                            MyCartFragment.noData.setVisibility(View.GONE);
                            MyCartFragment.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
                            MyCartFragment.no_internet.setVisibility(View.GONE);
                            MyCartFragment.cartItemsRecyclerView.setVisibility(View.VISIBLE);

                        }




                        for (x = 0; x < size; x++) {

                            if (loadProductData) {
                            }
                            if (!loadProductData) {

                                cartlist.add(task.getResult().get("product_ID_" + x).toString());
                                if (loadProductDetail) {
                                    addCart.setEnabled(true);
                                }
                                dialog.dismiss();
                            }


                            if (DBqueries.cartlist.contains(ProductDetailActivity.productID)) {
                                ProductDetailActivity.ALREADY_ADDED_TO_CART = true;
                            } else {
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
                                            if (cartlist.size() >= 2) {
                                                index = cartlist.size() - 2;
                                            }
                                            if ((Long) task.getResult().get("offers_applied") > 0) {
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
                                                        , (int) 0
                                                        , task.getResult().get("satuan").toString()
                                                ));


                                                if (cartItemModelList.size() > 1) {
                                                    Collections.sort(cartItemModelList, new Comparator<CartItemModel>() {
                                                        @Override
                                                        public int compare(CartItemModel o1, CartItemModel o2) {
                                                            if (o1.getProductTitle() == null || o2.getProductTitle() == null)
                                                                return 0;
                                                            return o1.getProductTitle().compareTo(o2.getProductTitle());

                                                        }
                                                    });
                                                }
                                            } else {
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
                                                        , (int) 0
                                                        , task.getResult().get("satuan").toString()
                                                ));
                                                if (cartItemModelList.size() > 1) {
                                                    Collections.sort(cartItemModelList, new Comparator<CartItemModel>() {
                                                        @Override
                                                        public int compare(CartItemModel o1, CartItemModel o2) {
                                                            if (o1.getProductTitle() == null || o2.getProductTitle() == null)
                                                                return 0;
                                                            return o1.getProductTitle().compareTo(o2.getProductTitle());

                                                        }
                                                    });
                                                }
                                            }
                                            String temp = cartlist.remove(cartlist.size() - 1);
                                            cartlist.add(index, temp);


                                            if (finalX == size - 1) {

                                                cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));


                                                LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
                                                parent.setVisibility(View.VISIBLE);


                                                continueBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (!totalAmount.getText().toString().replace(" ", "").equals("Rp.-/-")) {

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

                                                            dialog.dismiss();
                                                            if (DBqueries.addressModelList.size() == 0) {
                                                                DBqueries.loadAddresses(context, dialog, true, 0);
                                                            } else {
                                                                dialog.dismiss();
                                                                Intent deliveryIntent = new Intent(context, DeliveryActivity.class);
                                                                context.startActivity(deliveryIntent);
                                                            }
                                                        } else {
                                                            MyCartFragment.mycartfragment = null;
                                                            Toast.makeText(context, "Stock is Empty!", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });

                                                dialog.dismiss();
                                            }
                                            cartlistAdapter = new CartAdapter(context, cartItemsRecyclerView, linearLayoutManager, DBqueries.cartItemModelList, totalAmount, true, loadingDialog);
                                            cartlistAdapter.SetAdapter(cartlistAdapter);
                                            cartItemsRecyclerView.setAdapter(cartlistAdapter);
                                            linearLayoutManager = new LinearLayoutManager(context);
                                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                            cartItemsRecyclerView.setLayoutManager(linearLayoutManager);
                                            cartItemsRecyclerView.smoothScrollToPosition(0);
                                            linearLayoutManager.scrollToPositionWithOffset(0, 0);


                                            cartlistAdapter.notifyDataSetChanged();


                                            if (cartlist.size() == 1) {


                                            }


                                            if (cartlist.size() == 0) {


                                                cartItemModelList.clear();
                                                continueBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        MyCartFragment.mycartfragment = null;
                                                        Toast.makeText(context, "Cart is Empty!", Toast.LENGTH_SHORT).show();
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

                        if (x == 0) {
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
                        if (loadProductData) {

                            MyCartFragment.no_internet.setVisibility(View.GONE);
                            MyCartFragment.noData.setVisibility(View.VISIBLE);
                            MyCartFragment.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                            cartItemsRecyclerView.setVisibility(View.GONE);
                            cartItemModelList.clear();
                            dialog.dismiss();
                        }
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
                                    DBqueries.loadAddresses(context,loadingDialog,true,0);
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

    public static void loadAddresses(final Context context, final Dialog loadingDialog, final boolean gotoDeleveryActivity, final int SELECT_ADDRESS){
        addressModelList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                .document("MY_ADDRESSES").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                         Intent deliveryIntent = null;
                        if ((long) task.getResult().get("list_size") == 0) {
                            deliveryIntent = new Intent(context, AddAddressActivity.class);
                            deliveryIntent.putExtra("INTENT", "deliveryIntent");
                        } else {

                            for (long x = 1; x < (long) task.getResult().get("list_size") + 1; x++) {
                                addressModelList.add(new AddressModel(task.getResult().getBoolean("selected_"+x)
                                        ,task.getResult().getString("city_"+x)
                                        ,task.getResult().getString("locality_"+x)
                                        ,task.getResult().getString("flatNo_"+x)
                                        ,task.getResult().getString("pincode_"+x)
                                        ,task.getResult().getString("landmark_"+x)
                                        ,task.getResult().getString("name_"+x)
                                        ,task.getResult().getString("mobile_no_"+x)
                                        ,task.getResult().getString("alternativeMobileNo_"+x)
                                        ,task.getResult().getString("state_"+x)
                                ));
                                if ((Boolean) task.getResult().get("selected_" + x)) {
                                    selectedAddress = Integer.parseInt(String.valueOf(x - 1));
                                }
                            }
                            if (gotoDeleveryActivity) {
                                deliveryIntent = new Intent(context, DeliveryActivity.class);
                            }
                            loadingDialog.dismiss();
                        }
                        loadingDialog.dismiss();
                        if (gotoDeleveryActivity) {
                            context.startActivity(deliveryIntent);
                        }
                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            }
        });
    }

    public  static void loadRewards(final Context context, final Dialog loadingDialog, final Boolean onRewardFragment){

        if (onRewardFragment) {
            loadingDialog.show();
            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
        }

        rewardModelList.clear();

        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                             lastseenDate = task.getResult().getDate("Last seen");

                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_REWARDS").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()){

                                                if (task.getResult().size()>0) {

                                                    if (onRewardFragment){
                                                        MyRewardsFragment.noData.setVisibility(View.GONE);
                                                        MyRewardsFragment.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
                                                        MyRewardsFragment.no_internet.setVisibility(View.GONE);
                                                        MyRewardsFragment.rewardRecyclerView.setVisibility(View.VISIBLE);
                                                  }

                                                    int n = 0,i=0;
                                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                                        n =task.getResult().size();

                                                       if (documentSnapshot.get("type").toString().toLowerCase().equals("discount") && lastseenDate.before(documentSnapshot.getDate("validity"))){
                                                            rewardModelList.add(new RewardModel(documentSnapshot.getId(),documentSnapshot.get("type").toString()
                                                                    ,documentSnapshot.get("lower_limit").toString()
                                                                    ,documentSnapshot.get("upper_limit").toString()
                                                                    ,documentSnapshot.get("precentage").toString()
                                                                    ,documentSnapshot.get("body").toString().replace("\\n", "\n")
                                                                    ,(Timestamp) documentSnapshot.get("validity")
                                                                    ,(Boolean) documentSnapshot.get("already_used")
                                                            ));
                                                            i++;
                                                        }else if(documentSnapshot.get("type").toString().equals("Potongan Rp.*") && lastseenDate.before(documentSnapshot.getDate("validity"))){
                                                            rewardModelList.add(new RewardModel(documentSnapshot.getId(),documentSnapshot.get("type").toString()
                                                                    ,documentSnapshot.get("lower_limit").toString()
                                                                    ,documentSnapshot.get("upper_limit").toString()
                                                                    ,documentSnapshot.get("amount").toString()
                                                                    ,documentSnapshot.get("body").toString().replace("\\n", "\n")
                                                                    ,(Timestamp) documentSnapshot.get("validity")
                                                                    ,(Boolean) documentSnapshot.get("already_used")
                                                            ));
                                                            i++;
                                                        }



                                                    }


                                                    if(rewardModelList.size()>1) {
                                                        Collections.sort(rewardModelList, new Comparator<RewardModel>() {
                                                            @Override
                                                            public int compare(RewardModel o1, RewardModel o2) {
                                                                if (o1.getAlreadyUsed() == null || o2.getAlreadyUsed() == null)
                                                                    return 0;
                                                                return o1.getAlreadyUsed().compareTo(o2.getAlreadyUsed());
                                                            }

                                                        });

                                                   }

                                                    if (onRewardFragment) {




                                                        MyRewardsFragment.myRewardsAdapter.notifyDataSetChanged();
                                                            loadingDialog.dismiss();
                                                    }
                                                }else {


                                                    if (onRewardFragment) {
                                                        MyRewardsFragment.no_internet.setVisibility(View.GONE);
                                                        MyRewardsFragment.noData.setVisibility(View.VISIBLE);
                                                        MyRewardsFragment.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                                                        MyRewardsFragment.rewardRecyclerView.setVisibility(View.GONE);
                                                        rewardModelList.clear();
                                                    }

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

    public static void loadOrders(final Context context, final Dialog loadingDialog, @Nullable final LinearLayoutManager layoutManager, @Nullable final RecyclerView myOrderRecycleView){

        if (layoutManager!=null&&myOrderRecycleView!=null) {
            loadingDialog.show();
            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
loadingDialog.setOnDismissListener(null);
                }
            });
        }

        myOrderItemModelArrayList.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_NOTA").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (!task.getResult().isEmpty()){
                        iterasi_order_1=0;



                        n_order_1 = task.getResult().getDocuments().size();
                        if (layoutManager!=null&&myOrderRecycleView!=null) {

                            MyOrdersFragment.noData.setVisibility(View.GONE);
                            MyOrdersFragment.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent3));
                            MyOrdersFragment.no_internet.setVisibility(View.GONE);
                            MyOrdersFragment.myOrderRecycleView.setVisibility(View.VISIBLE);
                        }

                        for (final DocumentSnapshot documentSnapshot :task.getResult().getDocuments()) {

                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_NOTA").document(documentSnapshot.getId())
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                                    if (task.isSuccessful()) {
                                        iterasi_order_1++;
                                        final UploadBuktiModel uploadBuktiModel = new UploadBuktiModel(task.getResult().getString("atasNama")
                                                , task.getResult().getString("id_user")
                                                , task.getResult().getString("username")
                                                , task.getResult().getString("imageUrl")
                                                , task.getResult().getString("bank")
                                                , task.getResult().getString("tgl_transfer")
                                                , task.getResult().getString("fullnameAddress")
                                                , task.getResult().getString("fullAddress")
                                                , task.getResult().getString("phone")
                                                , task.getResult().getString("email")
                                                , task.getResult().getString("pincodeAddress")
                                                , task.getResult().getDate("tgl_pesan")
                                                , Integer.parseInt(Long.toString((Long) task.getResult().get("totalAmount")))
                                                , Integer.parseInt(Long.toString((Long) task.getResult().get("totalItems")))
                                                , Integer.parseInt(Long.toString((Long) task.getResult().get("totalItemsPrice")))
                                                , Integer.parseInt(Long.toString((Long) task.getResult().get("savedAmount")))
                                                , task.getResult().getString("deliveryPrice")
                                                , (boolean) task.getResult().get("ordered")
                                                , (boolean) task.getResult().get("packed")
                                                , (boolean) task.getResult().get("shipped")
                                                , (boolean) task.getResult().get("delivered")
                                                , (boolean) task.getResult().get("canceled")
                                                , task.getResult().getDate("ordered_date")
                                                , task.getResult().getDate("packed_date")
                                                , task.getResult().getDate("shipped_date")
                                                , task.getResult().getDate("delivered_date")
                                                , task.getResult().getDate("canceled_date")
                                                , task.getResult().getString("ket_kirim")
                                                , task.getResult().getString("metode_kirim")
                                                , (boolean) task.getResult().get("isfree")
                                                , (boolean) task.getResult().get("ishapus")
                                        );

                                        if ((boolean) task.getResult().get("ishapus") != true) {
                                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_NOTA").document(documentSnapshot.getId())
                                                    .collection("ITEM").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        final String idnota = documentSnapshot.getId();
                                                        iterasi_order_2 = 0;
                                                        n_order_2 = task.getResult().getDocuments().size();
                                                        for (final DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {

                                                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_NOTA").document(idnota)
                                                                    .collection("ITEM").document(documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if (task.isSuccessful()) {

                                                                        iterasi_order_2++;


                                                                        final CartItemModel cartItemModel = new CartItemModel();
                                                                        cartItemModel.setProductID(task.getResult().getString("productID"));
                                                                        cartItemModel.setProductImage(task.getResult().getString("productImage"));
                                                                        cartItemModel.setProductTitle(task.getResult().getString("productTitle"));
                                                                        cartItemModel.setProductPrice(task.getResult().getString("productPrice"));
                                                                        cartItemModel.setOriPrice(task.getResult().getString("oriPrice"));
                                                                        cartItemModel.setProductQuantity((long) task.getResult().get("productQuantity"));
                                                                        cartItemModel.setOffersApplied((long) task.getResult().get("offersApplied"));
                                                                        cartItemModel.setCouponsApplied((long) task.getResult().get("couponsApplied"));
                                                                        cartItemModel.setSelectedCouponId(task.getResult().getString("selectedCouponId"));
                                                                        cartItemModel.setDiscountedPrice(task.getResult().getString("discountedPrice"));
                                                                        cartItemModel.setSatuan(task.getResult().getString("satuan"));
                                                                        cartItemModel.setFreeCoupons((long) task.getResult().get("freeCoupons"));


                                                                        myOrderItemModelArrayList.add(new MyOrderItemModel(
                                                                                uploadBuktiModel.getAtasNama(),
                                                                                uploadBuktiModel.getId_user(),
                                                                                idnota,
                                                                                documentSnapshot.getId(),
                                                                                uploadBuktiModel.getUsername(),
                                                                                uploadBuktiModel.getImageUrl(),
                                                                                uploadBuktiModel.getBank(),
                                                                                uploadBuktiModel.getTgl_transfer(),
                                                                                uploadBuktiModel.getFullnameAddress(),
                                                                                uploadBuktiModel.getFullAddress(),
                                                                                uploadBuktiModel.getPhone(),
                                                                                uploadBuktiModel.getEmail(),
                                                                                uploadBuktiModel.getPincodeAddress(),
                                                                                uploadBuktiModel.getTgl_pesan(),
                                                                                uploadBuktiModel.getTotalAmount(),
                                                                                uploadBuktiModel.getTotalItems(),
                                                                                uploadBuktiModel.getTotalItemsPrice(),
                                                                                uploadBuktiModel.getSavedAmount(),
                                                                                uploadBuktiModel.getDeliveryPrice(),
                                                                                uploadBuktiModel.isOrdered(),
                                                                                uploadBuktiModel.isPacked(),
                                                                                uploadBuktiModel.isShipped(),
                                                                                uploadBuktiModel.isDelivered(),
                                                                                uploadBuktiModel.isCanceled(),
                                                                                uploadBuktiModel.getOrdered_date(),
                                                                                uploadBuktiModel.getPacked_date(),
                                                                                uploadBuktiModel.getShipped_date(),
                                                                                uploadBuktiModel.getDelivered_date(),
                                                                                uploadBuktiModel.getCanceled_date(),
                                                                                uploadBuktiModel.getKet_kirim(),
                                                                                uploadBuktiModel.getMetode_kirim(),
                                                                                uploadBuktiModel.isIsfree(),
                                                                                cartItemModel.getProductID(),
                                                                                cartItemModel.getProductImage(),
                                                                                cartItemModel.getProductTitle(),
                                                                                cartItemModel.getProductPrice(),
                                                                                cartItemModel.getOriPrice(),
                                                                                cartItemModel.getProductQuantity(),
                                                                                cartItemModel.getOffersApplied(),
                                                                                cartItemModel.getCouponsApplied(),
                                                                                cartItemModel.getSelectedCouponId(),
                                                                                cartItemModel.getDiscountedPrice(),
                                                                                cartItemModel.getFreeCoupons(),
                                                                                0,
                                                                                cartItemModel.getSatuan()
                                                                        ));


                                                                        Log.i("iterasi1", String.valueOf(iterasi_order_1));
                                                                        Log.i("iterasi2", String.valueOf(iterasi_order_2));
                                                                        Log.i("n1", String.valueOf(n_order_1));
                                                                        Log.i("n2", String.valueOf(n_order_2));

                                                                        if (iterasi_order_2 == n_order_2 && iterasi_order_1 == n_order_1) {



                                                                            if (myOrderItemModelArrayList.size() > 1) {
                                                                                Collections.sort(myOrderItemModelArrayList, new Comparator<MyOrderItemModel>() {
                                                                                    public int compare(MyOrderItemModel o1, MyOrderItemModel o2) {
                                                                                        if (o1.getTgl_pesan() == null || o2.getTgl_pesan() == null)
                                                                                            return 0;
                                                                                        return o1.getTgl_pesan().compareTo(o2.getTgl_pesan());
                                                                                    }
                                                                                });

                                                                                Collections.sort(myOrderItemModelArrayList, Collections.reverseOrder());
                                                                            }

                                                                            if (layoutManager != null && myOrderRecycleView != null) {
                                                                                loadingDialog.show();
                                                                                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                                                    @Override
                                                                                    public void onDismiss(DialogInterface dialog) {

                                                                                    }
                                                                                });
                                                                                loadRatingList(context, loadingDialog);

                                                                                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                                                                myOrderRecycleView.setLayoutManager(layoutManager);
                                                                                MyOrdersFragment.myOrderAdapter = new MyOrderAdapter(DBqueries.myOrderItemModelArrayList, loadingDialog);
                                                                                myOrderRecycleView.setAdapter(MyOrdersFragment.myOrderAdapter);
                                                                                MyOrdersFragment.myOrderAdapter.notifyDataSetChanged();
                                                                                loadingDialog.dismiss();

                                                                            }
                                                                            loadingDialog.dismiss();

                                                                        }


                                                                    } else {


                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    } else {

                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }


                                    } else {

                                        String error = task.getException().getMessage();
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        }

                    }else {
                        if (layoutManager!=null&&myOrderRecycleView!=null) {

                            MyOrdersFragment.no_internet.setVisibility(View.GONE);
                            MyOrdersFragment.noData.setVisibility(View.VISIBLE);
                            MyOrdersFragment.background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                            MyOrdersFragment.myOrderRecycleView.setVisibility(View.GONE);
                            loadingDialog.dismiss();
                            myOrderItemModelArrayList.clear();
                        }
                    }
                }else {

                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void checkNotifications(final Context context, boolean remove, @Nullable final TextView notifyCount){


        if (remove){
            registration.remove();
        }else {
            registration = firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                    .document("MY_NOTIFICATIONS").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {

                            if (documentSnapshot != null && documentSnapshot.exists()) {
                                notificationModelList.clear();
                                int unread = 0;
                                Log.i("bhbhb","vgvvg");
                                if ((long) documentSnapshot.get("list_size") > 0) {

                                    for (long x = 0; x < (long) documentSnapshot.get("list_size"); x++) {
                                        notificationModelList.add(0, new NotificationModel(
                                                documentSnapshot.get("Image_" + x).toString()
                                                , documentSnapshot.get("Body_" + x).toString()
                                                , documentSnapshot.getBoolean("Readed_" + x)
                                        ));
                                        if (!documentSnapshot.getBoolean("Readed_" + x)) {
                                            unread++;
                                            if (notifyCount != null) {
                                                if (unread > 0) {

                                                    if (unread < 99) {
                                                        notifyCount.setText(String.valueOf(unread));
                                                    } else {
                                                        notifyCount.setText("99+");
                                                    }
                                                  //  createNotification("Anda mendapatkan beberapa reward ",context);

                                                } else {
                                                    notifyCount.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        }



                                        if (NotificationActivity.adapter != null) {
                                            NotificationActivity.adapter.notifyDataSetChanged();
                                        }
                                    }

                                }else {

                                }

                            }
                        }
                    });
        }




        }






    public  static void addReward (final Context context, final String id_nota, final String id_user, final Dialog loadingDialog){


        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_NOTA").document(id_nota)
                .collection("ITEM").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    iterasi_order_1 = 0;
                    n_order_1=task.getResult().size();
                    for (final DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_NOTA").document(id_nota)
                                .collection("ITEM").document(documentSnapshot.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()) {
                                    if ((long) task.getResult().get("freeCoupons") > 0) {

                                        String product_ID = task.getResult().getString("productID");

                                        firebaseFirestore.collection("PRODUCTS").document(product_ID)
                                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {

                                                        final String reward = task.getResult().get("free_coupon_type").toString();

                                                    Map<String, Object> rewarddata = new HashMap<>();
                                                    long DAY_IN_MS = 1000 * 60 * 60 * 24;
                                                    Date date = new Date();
                                                    date = new Date(date.getTime() + (2 * DAY_IN_MS));
                                                    if (task.getResult().get("free_coupon_type").toString().toUpperCase().equals("DISCOUNT")) {
                                                        rewarddata.put("type", task.getResult().get("free_coupon_type").toString());
                                                        rewarddata.put("body", task.getResult().get("free_coupon_body").toString());
                                                        rewarddata.put("precentage", task.getResult().get("free_coupon_precentage").toString());
                                                        rewarddata.put("lower_limit", task.getResult().get("free_coupon_lower_limit").toString());
                                                        rewarddata.put("upper_limit", task.getResult().get("free_coupon_upper_limit").toString());
                                                        rewarddata.put("already_used", false);
                                                        rewarddata.put("validity", date);


                                                    } else {
                                                        rewarddata.put("type", task.getResult().get("free_coupon_type").toString());
                                                        rewarddata.put("body", task.getResult().get("free_coupon_body").toString());
                                                        rewarddata.put("amount", task.getResult().get("free_coupon_amount").toString());
                                                        rewarddata.put("lower_limit", task.getResult().get("free_coupon_lower_limit").toString());
                                                        rewarddata.put("upper_limit", task.getResult().get("free_coupon_upper_limit").toString());
                                                        rewarddata.put("already_used", false);
                                                        rewarddata.put("validity", date);
                                                    }


                                                    firebaseFirestore.collection("USERS").document(id_user).collection("USER_REWARDS").add(rewarddata)
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                    if (task.isSuccessful()) {


                                                                        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                                                                .document("MY_NOTIFICATIONS").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    notificationModelList.clear();

                                                                                    Map<String, Object> notifdata = new HashMap<>();
                                                                                    notifdata.put("list_size", (long) task.getResult().get("list_size") + 1);
                                                                                    notifdata.put("Body_" + (long) task.getResult().get("list_size"), "Selamat Anda Mendapatkan Reward " + reward);
                                                                                    notifdata.put("Image_" + (long) task.getResult().get("list_size"), "");
                                                                                    notifdata.put("Readed_" + (long) task.getResult().get("list_size"), false);

                                                                                    firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                                                                            .document("MY_NOTIFICATIONS").update(notifdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            iterasi_order_1++;


                                                                                            Toast.makeText(context, "Selamat Anda Mendapatkan Reward", Toast.LENGTH_LONG).show();
                                                                                            if (iterasi_order_1 == n_order_1) {
                                                                                                Log.i("iterasi", String.valueOf(iterasi_order_1));
                                                                                                Log.i("n2", String.valueOf(n_order_1));


                                                                                                loadingDialog.dismiss();

                                                                                            }
                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    String error = task.getException().getMessage();
                                                                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                                                    loadingDialog.dismiss();
                                                                                }
                                                                            }
                                                                        });


                                                                    } else {
                                                                        String error = task.getException().getMessage();
                                                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                                        loadingDialog.dismiss();
                                                                    }
                                                                }
                                                            });


                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                    loadingDialog.dismiss();
                                                }
                                            }
                                        });

                                    } else {
                                        iterasi_order_1++;
                                        if (iterasi_order_1 == n_order_1) {
                                            Log.i("iterasi2", String.valueOf(iterasi_order_1));
                                            Log.i("n2", String.valueOf(n_order_1));
                                            loadingDialog.dismiss();
                                        }

                                    }
                                }else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                    loadingDialog.dismiss();

                                }
                            }
                        });

                    }
                }else {

                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();

                }

            }});



    }



    public static void createNotification(String aMessage, Context context) {
        final int NOTIFY_ID = 0; // ID of notification
        String id = context.getString(R.string.default_notification_channel_id); // default_channel_id
        String title = context.getString(R.string.default_notification_channel_title); // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle("Ryello Shopping")                            // required
                    .setSmallIcon(R.mipmap.icon2_round)   // required
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.mipmap.icon2_round))
                    .setContentText(aMessage) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle("Ryello Shopping")                             // required
                    .setSmallIcon(R.mipmap.icon2_round)   // required
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.mipmap.icon2_round)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

    public static void lastSeen (final Context context,final Dialog loadingDialog){

        loadingDialog.show();
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                loadingDialog.setOnDismissListener(null);

            }
        });

        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).update("Last seen", FieldValue.serverTimestamp())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                lastseenDate = task.getResult().getDate("Last seen");
                                                Log.i("Last seen",lastseenDate.toString());

                                                loadingDialog.dismiss();
                                            }else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(context,error,Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void clearData(){
        categoryModelList.clear();
        if (!MainActivity.showCart) {
            lists.clear();
        }
        myOrderItemModelArrayList.clear();
        loadedCategoriesNames.clear();
        wishlist.clear();
        wishlistModelList.clear();
        myRating.clear();
        myRatedIds.clear();
        cartlist.clear();
        cartItemModelList.clear();
        addressModelList.clear();
        rewardModelList.clear();
        profile = "";
        fullname = "";
        email= "";
    }



}
