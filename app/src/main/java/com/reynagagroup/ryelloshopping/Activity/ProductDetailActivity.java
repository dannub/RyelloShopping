package com.reynagagroup.ryelloshopping.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.MyRewardsAdapter;
import com.reynagagroup.ryelloshopping.adapter.ProductDetailAdapter;
import com.reynagagroup.ryelloshopping.adapter.ProductImagesAdapter;
import com.reynagagroup.ryelloshopping.fragment.SignInFragment;
import com.reynagagroup.ryelloshopping.fragment.SignUpFragment;
import com.reynagagroup.ryelloshopping.model.CartItemModel;
import com.reynagagroup.ryelloshopping.model.ProductSpesificationModel;
import com.reynagagroup.ryelloshopping.model.WishlistModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.reynagagroup.ryelloshopping.DBqueries.cartItemModelList;
import static com.reynagagroup.ryelloshopping.DBqueries.cartlist;
import static com.reynagagroup.ryelloshopping.Activity.MainActivity.showCart;
import static com.reynagagroup.ryelloshopping.Activity.RegisterActivity.setSignUpFragment;

public class ProductDetailActivity extends AppCompatActivity {

    public static Boolean running_wishlist_query = false;
    public static Boolean running_rating_query = false;
    public  static Activity productDetailsActivity;
    public static Boolean running_cart_query = false;

    private ViewPager productImageViewPager;
    private  TextView productTitle;
    private  TextView averageRatingMiniView;
    private TabLayout viewpagerIndikator;
    private TextView productPrice;
    private String productOriginalPrice;
    private TextView oriPrice;
    private  Button couponRedermBtn;
    private  TextView totalRatingMiniView;
    private ImageView codIndicator;
    private  TextView tvCodIndicator;
    private View divider;


    private  LinearLayout couponRedemptionLayout;

    private  TextView rewardTitle;
    private TextView rewardBody;
    private  ImageView rewardIcon;
    private TextView rewarddiscount;



    ////coupondialog
    private TextView coupontitle;
    private TextView couponexpiryDate;
    private TextView coupondiscount;
    private TextView couponCouponBody1;
    private RecyclerView couponRecyclerView;
    private LinearLayout selectedCoupon;
    private LinearLayout discountLayout;
    private TextView tv_SK;
    private TextView discountedPrice;
    private TextView originalPrice;
    private ConstraintLayout couponLayout;
    private LinearLayout applyOrRemoveBtnContainer;
    ////coupondialog

    ///ProductDescription
    private ConstraintLayout productDetailTabsContainer;
    private ConstraintLayout productDetailOnlyContainer;
    private ViewPager productDetailsViewpager;
    private TabLayout productDetailTablayout;
    private TextView productOnlyDescriptionBody;

    private  List<ProductSpesificationModel> productSpesificationModelList = new ArrayList<>();
    private String productDescription;
    private String productOtherDetails;
    ///ProductDescription


    /////ratting layout
    public static int initialRating;
    public static LinearLayout rateNowContainer;
    private  TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private  TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private  TextView averageRatings;
    /////ratting layout

    private  Dialog signInDialog;
    public static Dialog loadingDialog;

    private Button buyNowBtn;
    private LinearLayout addToCartBtn;

    public static String productID;


    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;
    public static FloatingActionButton addToWishlistBtn;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;
    private TextView badgeCount;
    public static Boolean isCategory,isFragment;


    private DocumentSnapshot documentSnapshot;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //loading dialog
        loadingDialog = new Dialog(ProductDetailActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //loading dialog

        ///////coupon dialog

        final Dialog checkCouponPriceDialog = new Dialog(ProductDetailActivity.this);
        checkCouponPriceDialog.setContentView(R.layout.coupon_redeem_dialog);
        checkCouponPriceDialog.setCancelable(true);
        checkCouponPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toogleRecyclerView = checkCouponPriceDialog.findViewById(R.id.toogle_recyclreview);
        couponRecyclerView = checkCouponPriceDialog.findViewById(R.id.coupons_recyclerview);
        selectedCoupon = checkCouponPriceDialog.findViewById(R.id.selected_coupon);

        coupontitle = checkCouponPriceDialog.findViewById(R.id.reward_title);
        couponexpiryDate = checkCouponPriceDialog.findViewById(R.id.reward_till_date);
        couponCouponBody1 = checkCouponPriceDialog.findViewById(R.id.reward_body1);
        coupondiscount = checkCouponPriceDialog.findViewById(R.id.discount_reward);
        discountLayout = checkCouponPriceDialog.findViewById(R.id.discount_layout);
        couponLayout = checkCouponPriceDialog.findViewById(R.id.coupon_layout);

        applyOrRemoveBtnContainer = checkCouponPriceDialog.findViewById(R.id.apply_or_remove_btn_container);

        applyOrRemoveBtnContainer.setVisibility(View.GONE);

        tv_SK = checkCouponPriceDialog.findViewById(R.id.tv_SK);

        originalPrice = checkCouponPriceDialog.findViewById(R.id.original_price);
        discountedPrice = checkCouponPriceDialog.findViewById(R.id.discounted_price);


        couponLayout.setBackground(getResources().getDrawable(R.drawable.border_background));
        discountLayout.setVisibility(View.GONE);
        tv_SK.setVisibility(View.INVISIBLE);
        couponexpiryDate.setVisibility(View.INVISIBLE);


        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        couponRecyclerView.setLayoutManager(layoutManager);



        couponRecyclerView.setVisibility(View.GONE);
        selectedCoupon.setVisibility(View.VISIBLE);

        toogleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }
        });

        /////coupondialog

        productImageViewPager = findViewById(R.id.product_images_viewpager);
        viewpagerIndikator = findViewById(R.id.viewpager_indicator);
        addToWishlistBtn = findViewById(R.id.add_to_wishlist_btn);
        productDetailsViewpager = findViewById(R.id.product_details_viewpager);
        productDetailTablayout = findViewById(R.id.product_detail_tablayout);

        productDetailTabsContainer = findViewById(R.id.product_details_tablayout_container);
        productDetailOnlyContainer = findViewById(R.id.product_detailsonly_container);
        productOnlyDescriptionBody = findViewById(R.id.product_detail_body);


        buyNowBtn = findViewById(R.id.buy_now_btn);
        couponRedermBtn = findViewById(R.id.coupen_redemption_btn);
        productTitle = findViewById(R.id.product_title);
        divider = findViewById(R.id.divider3);
        averageRatingMiniView = findViewById(R.id.tv_product_ratting_miniview);
        totalRatingMiniView = findViewById(R.id.total_ratings_minview);
        productPrice = findViewById(R.id.product_price);
        oriPrice = findViewById(R.id.ori_price);
        tvCodIndicator= findViewById(R.id.cod_indicator);
        codIndicator = findViewById(R.id.cod_img_indicator);
        rewardTitle = findViewById(R.id.reward_title);
        rewardIcon = findViewById(R.id.reward_icon);
        rewardBody = findViewById(R.id.reward_body1);
        rewarddiscount = findViewById(R.id.discount_reward);



        totalRatings = findViewById(R.id.total_rattings);
        ratingsNoContainer = findViewById(R.id.ratttings_number_container);
        totalRatingsFigure = findViewById(R.id.total_rattings_figure);
        ratingsProgressBarContainer = findViewById(R.id.rattings_progressbar_container);
        averageRatings = findViewById(R.id.average_rattings);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        couponRedemptionLayout = findViewById(R.id.coupen_redemption_layout);

        final List<String> productImages = new ArrayList<>();

        productID = getIntent().getStringExtra("productID").replace(" ","");

        initialRating = -1;


        firebaseFirestore = FirebaseFirestore.getInstance();



        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    documentSnapshot = task.getResult();

                    for (long x = 1;x< (long)documentSnapshot.get("no_of_product_images")+1;x++){
                        productImages.add(documentSnapshot.get("product_image_"+x).toString());
                    }
                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                    productImageViewPager.setAdapter(productImagesAdapter);

                    productTitle.setText(documentSnapshot.get("product_title").toString());
                    averageRatingMiniView.setText(documentSnapshot.get("average_ratting").toString());
                    totalRatingMiniView.setText("("+(long)documentSnapshot.get("total_ratings")+")ratings");

                    if ((Long)documentSnapshot.get("offers_applied")>0) {
                        oriPrice.setVisibility(View.VISIBLE);
                        divider.setVisibility(View.VISIBLE);
                        productPrice.setText("Rp." + documentSnapshot.get("cutted_price").toString() + "/-");
                        oriPrice.setText("Rp." + documentSnapshot.get("product_price").toString() + "/-");
                    }else {
                        oriPrice.setVisibility(View.GONE);
                        divider.setVisibility(View.GONE);
                        productPrice.setText("Rp." + documentSnapshot.get("product_price").toString() + "/-");
                    }

                    ///for  coupon dialog
                    originalPrice.setText(productPrice.getText());
                    if ((Long)documentSnapshot.get("offers_applied")>0) {
                        productOriginalPrice = documentSnapshot.get("cutted_price").toString();
                    }else {
                        productOriginalPrice = documentSnapshot.get("product_price").toString();
                    }
                    MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(DBqueries.rewardModelList,true,couponRecyclerView,selectedCoupon,productOriginalPrice,coupontitle,couponexpiryDate,couponCouponBody1,coupondiscount,discountLayout,couponLayout,tv_SK,discountedPrice);
                    couponRecyclerView.setAdapter(myRewardsAdapter);
                    myRewardsAdapter.notifyDataSetChanged();
                    ///for  coupon dialog


                    if ((boolean)documentSnapshot.get("COD")){
                        codIndicator.setVisibility(View.VISIBLE);
                        tvCodIndicator.setVisibility(View.VISIBLE);
                    }else {
                        codIndicator.setVisibility(View.INVISIBLE);
                        tvCodIndicator.setVisibility(View.INVISIBLE);
                    }
                    rewardTitle.setText((long)documentSnapshot.get("free_coupon")+" "+documentSnapshot.get("free_coupon_title").toString());
                    rewardBody.setText(documentSnapshot.get("free_coupon_body1").toString().replace("\\n", "\n"));

                    if ((Boolean) documentSnapshot.get("use_tab_layout")){
                        productDetailTabsContainer.setVisibility(View.VISIBLE);
                        productDetailOnlyContainer.setVisibility(View.GONE);
                        productDescription = documentSnapshot.get("product_description").toString().replace("\\n", "\n");

                        productOtherDetails = documentSnapshot.get("product_other_details").toString().replace("\\n", "\n");

                        for (long x = 1;x<(long) documentSnapshot.get("total_spec_titles")+1;x++){

                            productSpesificationModelList.add(new ProductSpesificationModel(0,documentSnapshot.get("spec_title_"+x).toString()));
                            for (long y =1;y < (long)documentSnapshot.get("spec_title_"+x+"_total_fields")+1;y++){
                               productSpesificationModelList.add(new ProductSpesificationModel(1,documentSnapshot.get("spec_title_"+x+"_field_"+y+"_name").toString(),documentSnapshot.get("spec_title_"+x+"_field_"+y+"_value").toString()));
                            }
                        }

                    }else {
                        productDetailTabsContainer.setVisibility(View.GONE);
                        productDetailOnlyContainer.setVisibility(View.VISIBLE);
                        productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString().replace("\\n", "\n"));
                    }

                    totalRatings.setText((long)documentSnapshot.get("total_ratings")+" ratings");

                    for (int x =0;x < 5;x++){
                        TextView rating = (TextView)ratingsNoContainer.getChildAt(x);
                        rating.setText(String.valueOf((long)documentSnapshot.get((5-x)+"_star")));

                        ProgressBar progressBar = (ProgressBar)ratingsProgressBarContainer.getChildAt(x);
                        int maxProgress = Integer.parseInt(String.valueOf((long)documentSnapshot.get("total_ratings")));
                        progressBar.setMax(maxProgress);
                        progressBar.setProgress(Integer.parseInt(String.valueOf((long)documentSnapshot.get((5-x)+"_star"))));

                    }
                    totalRatingsFigure.setText(String.valueOf((long)documentSnapshot.get("total_ratings")));
                    averageRatings.setText(documentSnapshot.get("average_ratting").toString());
                    productDetailsViewpager.setAdapter(new ProductDetailAdapter(getSupportFragmentManager(),productDetailTablayout.getTabCount(),productDescription,productOtherDetails,productSpesificationModelList));

                    if (currentUser!=null) {
                        if (DBqueries.myRating.size()==0){
                            DBqueries.loadRatingList(ProductDetailActivity.this);
                        }
                        if (DBqueries.cartlist.size() == 0) {
                            DBqueries.loadCartList(ProductDetailActivity.this, loadingDialog,false,badgeCount,new TextView(ProductDetailActivity.this));
                        }
                        if (DBqueries.wishlist.size() == 0) {
                            DBqueries.loadWishlist(ProductDetailActivity.this, loadingDialog,false);
                        } else {
                            loadingDialog.dismiss();
                        }
                        if (DBqueries.rewardModelList.size()==0){
                            DBqueries.loadRewards(ProductDetailActivity.this,loadingDialog,false);
                        }
                        if (DBqueries.cartlist.size()!=0 && DBqueries.rewardModelList.size()!=0 && DBqueries.wishlist.size() != 0){
                            loadingDialog.dismiss();
                        }
                    }else {
                        loadingDialog.dismiss();
                    }


                    if (DBqueries.myRatedIds.contains(productID)){
                        int index = DBqueries.myRatedIds.indexOf(productID);
                        initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) -1;
                        setRatting(initialRating);
                    }


                    if (DBqueries.wishlist.contains(productID)){
                        ALREADY_ADDED_TO_WISHLIST = true;
                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent4));

                    }else {
                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent3));
                        ALREADY_ADDED_TO_WISHLIST = false;
                    }


                    if ((boolean)documentSnapshot.get("in_stock")){
                        loadingDialog.show();
                        buyNowBtn.setVisibility(View.VISIBLE);
                        addToCartBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (currentUser == null){
                                    signInDialog.show();
                                }else {
                                    if (!running_cart_query) {
                                        running_cart_query = true;
                                        if (ALREADY_ADDED_TO_CART) {
                                            running_cart_query = false;
                                            Toast.makeText(ProductDetailActivity.this,"Already added to cart!",Toast.LENGTH_SHORT).show();
                                        } else {
                                            final Map<String, Object> addProduct = new HashMap<>();
                                            addProduct.put("product_ID_" + String.valueOf(DBqueries.cartlist.size()), productID);


                                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                                                    .collection("USER_DATA").document("MY_CART")
                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        loadingDialog.show();
                                                       addProduct.put("list_size", (long) task.getResult().get("list_size") + 1);


                                                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                                                .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {


                                                                    if ((Long)documentSnapshot.get("offers_applied")>0) {
                                                                        cartItemModelList.add(0, new CartItemModel(CartItemModel.CART_ITEM,
                                                                                productID, documentSnapshot.get("product_image_1").toString()
                                                                                , documentSnapshot.get("product_title").toString()
                                                                                , (long) documentSnapshot.get("free_coupon")
                                                                                , documentSnapshot.get("cutted_price").toString()
                                                                                , documentSnapshot.get("product_price").toString()
                                                                                , (long) 1
                                                                                , (long) documentSnapshot.get("offers_applied")
                                                                                , (long) 0
                                                                                , (Boolean) documentSnapshot.get("in_stock")
                                                                                ,(long)0
                                                                        ));
                                                                    }else {
                                                                        cartItemModelList.add(0, new CartItemModel(CartItemModel.CART_ITEM,
                                                                                productID, documentSnapshot.get("product_image_1").toString()
                                                                                , documentSnapshot.get("product_title").toString()
                                                                                , (long) documentSnapshot.get("free_coupon")
                                                                                , documentSnapshot.get("product_price").toString()
                                                                                ,""
                                                                                , (long) 1
                                                                                , (long) documentSnapshot.get("offers_applied")
                                                                                , (long) 0
                                                                                , (Boolean) documentSnapshot.get("in_stock")
                                                                                ,(long)0
                                                                        ));
                                                                    }

                                                                    DBqueries.loadCartList(ProductDetailActivity.this, new Dialog(ProductDetailActivity.this),false,badgeCount,new TextView(ProductDetailActivity.this));
                                                                    ALREADY_ADDED_TO_CART = true;
                                                                    Toast.makeText(ProductDetailActivity.this, "Added to Cart successfully!", Toast.LENGTH_SHORT).show();
                                                                    loadingDialog.dismiss();
                                                                    running_cart_query= false;

                                                                } else {
                                                                    running_cart_query= false;
                                                                    String error = task.getException().getMessage();
                                                                    Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }else {
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailActivity.this,error,Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });


                                        }
                                    }
                                }
                            }
                        });

                        isCategory=true;
                        loadingDialog.dismiss();
                    }else {
                        buyNowBtn.setVisibility(View.GONE);
                        TextView outOfStock =(TextView) addToCartBtn.getChildAt(0);
                        outOfStock.setText("Out of Stock");
                        outOfStock.setTextColor(getResources().getColor(R.color.colorAccent));
                        addToCartBtn.setBackgroundColor(getResources().getColor(R.color.colorAccent4));
                        outOfStock.setCompoundDrawables(null,null,null,null);
                        loadingDialog.dismiss();
                    }

                }else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailActivity.this,error,Toast.LENGTH_SHORT).show();
                }
            }
        });



        viewpagerIndikator.setupWithViewPager(productImageViewPager,true);

        addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                if (currentUser==null){
                    signInDialog.show();
                }else {
                    if (!running_wishlist_query) {
                        running_wishlist_query = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {
                            int index = DBqueries.wishlist.indexOf(productID);
                            DBqueries.removeFromWishlist(index, ProductDetailActivity.this);
                            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent3));
                        } else {
                            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent4));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBqueries.wishlist.size()), productID);
                            addProduct.put("list_size", (long) DBqueries.wishlist.size() + 1);


                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (DBqueries.wishlistModelList.size() != 0) {

                                            if ((Long)documentSnapshot.get("offers_applied")>0) {
                                                DBqueries.wishlistModelList.add(new WishlistModel(
                                                        productID
                                                        , documentSnapshot.get("product_image_1").toString()
                                                        , documentSnapshot.get("product_title").toString() + " " + documentSnapshot.get("product_subtitle").toString()
                                                        , (long) documentSnapshot.get("free_coupon")
                                                        , documentSnapshot.get("average_ratting").toString()
                                                        , (long) documentSnapshot.get("total_ratings")
                                                        , documentSnapshot.get("cutted_price").toString()
                                                        , documentSnapshot.get("product_price").toString()
                                                        , (Boolean) documentSnapshot.get("COD")
                                                        , (Boolean) documentSnapshot.get("in_stock")
                                                        ,(long)documentSnapshot.get("offers_applied")
                                                ));
                                            }else {
                                                DBqueries.wishlistModelList.add(new WishlistModel(
                                                        productID
                                                        , documentSnapshot.get("product_image_1").toString()
                                                        , documentSnapshot.get("product_title").toString() + " " + documentSnapshot.get("product_subtitle").toString()
                                                        , (long) documentSnapshot.get("free_coupon")
                                                        , documentSnapshot.get("average_ratting").toString()
                                                        , (long) documentSnapshot.get("total_ratings")
                                                        , documentSnapshot.get("product_price").toString()
                                                        ,""
                                                        , (Boolean) documentSnapshot.get("COD")
                                                        , (Boolean) documentSnapshot.get("in_stock")
                                                        ,(long)documentSnapshot.get("offers_applied")
                                                ));
                                            }
                                            DBqueries.loadRatingList(ProductDetailActivity.this);
                                        }

                                        ALREADY_ADDED_TO_WISHLIST = true;
                                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent4));
                                        DBqueries.wishlist.add(productID);
                                        loadingDialog.dismiss();
                                        Toast.makeText(ProductDetailActivity.this, "Added to wishlist successfull", Toast.LENGTH_SHORT).show();

                                    } else {
                                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent3));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishlist_query= false;
                                }
                            });


                        }
                    }
                }
            }
        });


        productDetailsViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailTablayout));

        productDetailTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        /////ratting layout

        rateNowContainer = findViewById(R.id.rate_now_container);
        for (int x = 0; x <rateNowContainer.getChildCount();x++){
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser ==null) {
                        signInDialog.show();
                    }else {
                         if (starPosition!= initialRating) {
                            if (!running_rating_query) {
                                running_rating_query = true;

                                setRatting(starPosition);

                                Map<String, Object> updateRating = new HashMap<>();
                                if (DBqueries.myRatedIds.contains(productID)) {

                                    TextView oldrating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalrating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);

                                    updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldrating.getText().toString()) - 1);
                                    updateRating.put(starPosition + 1 + "_star", Long.parseLong(finalrating.getText().toString()) + 1);
                                    updateRating.put("average_ratting", calculateAverageRating((long) starPosition - initialRating, true));
                                } else {
                                    updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                    updateRating.put("average_ratting", calculateAverageRating((long) starPosition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);
                                }

                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Map<String, Object> myRating = new HashMap<>();
                                            if (DBqueries.myRatedIds.contains(productID)) {
                                                myRating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);
                                            } else {
                                                myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                                myRating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                                myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);
                                            }


                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA")
                                                    .document("MY_RATINGS").update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {


                                                        if (DBqueries.myRatedIds.contains(productID)) {

                                                            DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);

                                                            TextView oldrating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                                            TextView finalrating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                            oldrating.setText(String.valueOf(Integer.parseInt(oldrating.getText().toString()) - 1));
                                                            finalrating.setText(String.valueOf(Integer.parseInt(finalrating.getText().toString()) + 1));

                                                        } else {
                                                            DBqueries.myRatedIds.add(productID);
                                                            DBqueries.myRating.add((long) starPosition + 1);

                                                            TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                            totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ")ratings");
                                                            totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                            totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));


                                                            Toast.makeText(ProductDetailActivity.this, "Thank you! for rating.", Toast.LENGTH_SHORT).show();
                                                        }

                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);

                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                                            int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                            progressBar.setMax(maxProgress);
                                                            progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));
                                                        }
                                                        initialRating = starPosition;
                                                        averageRatings.setText(calculateAverageRating(0, true));
                                                        averageRatingMiniView.setText(calculateAverageRating(0, true));

                                                        if (DBqueries.wishlist.contains(productID) && DBqueries.wishlistModelList.size() != 0) {
                                                            int index = DBqueries.wishlist.indexOf(productID);
                                                            DBqueries.wishlistModelList.get(index).setRatting(averageRatings.getText().toString());
                                                            DBqueries.wishlistModelList.get(index).setTotalRattings(Long.parseLong(totalRatingsFigure.getText().toString()));
                                                        }

                                                    } else {
                                                        setRatting(initialRating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    running_rating_query = false;

                                                }
                                            });
                                        } else {
                                            running_rating_query = false;
                                            setRatting(initialRating);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        }
                    }
                }
            });
        }
        /////ratting layout

        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser ==null) {
                    signInDialog.show();
                }else {
                    PaymentActivity.fromCart = false;
                    loadingDialog.show();
                    productDetailsActivity = ProductDetailActivity.this;
                    //DeliveryActivity.cartItemModelList.clear();
                    DeliveryActivity.cartItemModelList = new ArrayList<>();
                    DeliveryActivity.cartItemModelList.clear();
                    if ((Long)documentSnapshot.get("offers_applied")>0) {
                        DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM,
                                productID, documentSnapshot.get("product_image_1").toString()
                                , documentSnapshot.get("product_title").toString()
                                , (long) documentSnapshot.get("free_coupon")
                                , documentSnapshot.get("cutted_price").toString()
                                , documentSnapshot.get("product_price").toString()
                                , (long) 1
                                , (long) documentSnapshot.get("offers_applied")
                                , (long) 0
                                , (Boolean) documentSnapshot.get("in_stock")
                                ,(long)0
                        ));
                    }else {
                        DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.CART_ITEM,
                                productID, documentSnapshot.get("product_image_1").toString()
                                , documentSnapshot.get("product_title").toString()
                                , (long) documentSnapshot.get("free_coupon")
                                , documentSnapshot.get("cutted_price").toString()
                                ,""
                                , (long) 1
                                , (long) documentSnapshot.get("offers_applied")
                                , (long) 0
                                , (Boolean) documentSnapshot.get("in_stock")
                                ,(long)0
                        ));
                    }
                    DeliveryActivity.cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));
                    if (DBqueries.addressModelList.size()==0){
                        DBqueries.loadAddresses(ProductDetailActivity.this,loadingDialog);
                    }else {
                        loadingDialog.dismiss();
                        Intent deliveryIntent = new Intent(ProductDetailActivity.this,DeliveryActivity.class);
                        startActivity(deliveryIntent);
                    }
                }
            }
        });



        couponRedermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCouponPriceDialog.show();
            }
        });


        ///sign dialog
        signInDialog = new Dialog(ProductDetailActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);

        final Intent registerIntent = new Intent(ProductDetailActivity.this, RegisterActivity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseBtn = true;
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpFragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });
        ///sign dialog




    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser ==null){
            couponRedemptionLayout.setVisibility(View.GONE);
        }else {
            couponRedemptionLayout.setVisibility(View.VISIBLE);
        }


        if (currentUser!=null) {
            if (DBqueries.myRating.size()==0){
                DBqueries.loadRatingList(ProductDetailActivity.this);
            }
            if (DBqueries.wishlist.size() == 0) {
                DBqueries.loadWishlist(ProductDetailActivity.this, loadingDialog,false);
            } else {
                loadingDialog.dismiss();
            }
            if (DBqueries.rewardModelList.size()==0){
                DBqueries.loadRewards(ProductDetailActivity.this,loadingDialog,false);
            }
            if (DBqueries.cartlist.size()!=0 && DBqueries.rewardModelList.size()!=0 && DBqueries.wishlist.size() != 0){
                loadingDialog.dismiss();
            }
        }else {
            loadingDialog.dismiss();
        }

        if (DBqueries.myRatedIds.contains(productID)){
            int index = DBqueries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index)))-1;
            setRatting(initialRating);
        }


        if (DBqueries.cartlist.contains(productID)){
            ALREADY_ADDED_TO_CART = true;

        }else {
            ALREADY_ADDED_TO_CART = false;
        }

        if (DBqueries.wishlist.contains(productID)){
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent4));

        }else {
            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent3));
            ALREADY_ADDED_TO_WISHLIST = false;
        }

        invalidateOptionsMenu();


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

    public static void setRatting(int starPosition) {

        for (int x = 0; x <rateNowContainer.getChildCount(); x++){
            ImageView starBtn = (ImageView)rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#CCC8C8")));
            if (x <= starPosition){
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }

        }
    }

    private  String calculateAverageRating(long currentUserRating,boolean update){
        Double totalStars = Double.valueOf(0);
        for (int x=1;x<6;x++){
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5-x);
            totalStars = totalStars +(Long.parseLong(ratingNo.getText().toString())*x);
        }
        totalStars = totalStars + currentUserRating;
        if (update){
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0,3);
        }else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString())+ 1)).substring(0,3);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);

        MenuItem cartItem = menu.findItem(R.id.main_chart_icon);

        cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.shop);
            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

            if (currentUser!=null){
                if (DBqueries.cartlist.size() == 0) {
                    DBqueries.loadCartList(ProductDetailActivity.this, loadingDialog,false,badgeCount,new TextView(ProductDetailActivity.this));
                }else {
                    badgeCount.setVisibility(View.VISIBLE);
                    if (cartlist.size()<99) {
                        badgeCount.setText(String.valueOf(cartlist.size()));
                    }else {
                        badgeCount.setText("99");
                    }
                }
            }
            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser==null){
                        signInDialog.show();
                    }else {
                        Intent cartIntent = new Intent(ProductDetailActivity.this, MainActivity.class);
                        showCart = true;
                        startActivity(cartIntent);

                    }
                }
            });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home){
            productDetailsActivity = null;
            showCart = false;
            finish();
            MainActivity.currentFragment = MainActivity.HOME_FRAGMENT;

            return true;
        }else if(id ==R.id.search_icon){
            //todo: search
            return true;

        }else if(id ==R.id.main_chart_icon){
            if (currentUser==null){
                signInDialog.show();
            }else {
                showCart = true;
                productDetailsActivity = ProductDetailActivity.this;
                Intent cartIntent = new Intent(ProductDetailActivity.this, MainActivity.class);
                startActivity(cartIntent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        productDetailsActivity = null;
        showCart = false;
        finish();
        MainActivity.currentFragment = MainActivity.HOME_FRAGMENT;
        super.onBackPressed();
    }
}
