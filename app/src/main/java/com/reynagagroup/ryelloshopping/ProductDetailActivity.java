package com.reynagagroup.ryelloshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.reynagagroup.ryelloshopping.adapter.MyRewardsAdapter;
import com.reynagagroup.ryelloshopping.adapter.ProductDetailAdapter;
import com.reynagagroup.ryelloshopping.adapter.ProductImagesAdapter;
import com.reynagagroup.ryelloshopping.fragment.ProductDescriptionFragment;
import com.reynagagroup.ryelloshopping.fragment.ProductSpesificationFragment;
import com.reynagagroup.ryelloshopping.fragment.SignInFragment;
import com.reynagagroup.ryelloshopping.fragment.SignUpFragment;
import com.reynagagroup.ryelloshopping.model.ProductSpesificationModel;
import com.reynagagroup.ryelloshopping.model.RewardModel;
import com.reynagagroup.ryelloshopping.model.WishlistModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.reynagagroup.ryelloshopping.MainActivity.showCart;
import static com.reynagagroup.ryelloshopping.RegisterActivity.setSignUpFragment;

public class ProductDetailActivity extends AppCompatActivity {

    public static Boolean running_wishlist_query = false;

    private ViewPager productImageViewPager;
    private  TextView productTitle;
    private  TextView averageRatingMiniView;
    private TabLayout viewpagerIndikator;
    private TextView productPrice;
    private TextView cuttedPrice;
    private  Button couponRedermBtn;
    private  TextView totalRatingMiniView;
    private ImageView codIndicator;
    private  TextView tvCodIndicator;

    private  LinearLayout couponRedemptionLayout;

    private  TextView rewardTitle;
    private TextView rewardBody1;
    private TextView rewardBody2;
    private  ImageView rewardIcon;
    private TextView rewarddiscount;



    ////coupondialog
    public static TextView coupontitle;
    public static TextView couponexpiryDate;
    public static TextView coupondiscount;
    public static TextView couponCouponBody1;
    public static TextView couponCouponBody2;
    private static RecyclerView couponRecyclerView;
    private  static  LinearLayout selectedCoupon;
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
    public static LinearLayout rateNowContainer;
    private  TextView totalRatings;
    private LinearLayout ratingsNoContainer;
    private  TextView totalRatingsFigure;
    private LinearLayout ratingsProgressBarContainer;
    private  TextView averageRatings;
    /////ratting layout

    private  Dialog signInDialog;
    private Dialog loadingDialog;

    private Button buyNowBtn;
    private LinearLayout addToCartBtn;

    public static String productID;


    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static FloatingActionButton addToWishlistBtn;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser currentUser;

    private DocumentSnapshot documentSnapshot;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        averageRatingMiniView = findViewById(R.id.tv_product_ratting_miniview);
        totalRatingMiniView = findViewById(R.id.total_ratings_minview);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        tvCodIndicator= findViewById(R.id.cod_indicator);
        codIndicator = findViewById(R.id.cod_img_indicator);
        rewardTitle = findViewById(R.id.reward_title);
        rewardIcon = findViewById(R.id.reward_icon);
        rewardBody1 = findViewById(R.id.reward_body1);
        rewardBody2 = findViewById(R.id.reward_body2);
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

        //loading dialog
        loadingDialog = new Dialog(ProductDetailActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //loading dialog

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

                    productPrice.setText("Rp."+documentSnapshot.get("product_price").toString()+"/-");
                    cuttedPrice.setText("Rp."+documentSnapshot.get("cutted_price").toString()+"/-");

                    if ((boolean)documentSnapshot.get("COD")){
                        codIndicator.setVisibility(View.VISIBLE);
                        tvCodIndicator.setVisibility(View.VISIBLE);
                    }else {
                        codIndicator.setVisibility(View.INVISIBLE);
                        tvCodIndicator.setVisibility(View.INVISIBLE);
                    }
                    rewardTitle.setText((long)documentSnapshot.get("free_coupon")+" "+documentSnapshot.get("free_coupon_title").toString());
                    rewardBody1.setText(documentSnapshot.get("free_coupon_body1").toString());
                    rewardBody2.setText(documentSnapshot.get("free_coupon_body2").toString());

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
                        if (DBqueries.wishlist.size() == 0) {
                            DBqueries.loadWishlist(ProductDetailActivity.this, loadingDialog,false);
                        } else {
                            loadingDialog.dismiss();
                        }
                    }else {
                        loadingDialog.dismiss();
                    }

                    if (DBqueries.wishlist.contains(productID)){
                        ALREADY_ADDED_TO_WISHLIST = true;
                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent4));

                    }else {
                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent3));
                        ALREADY_ADDED_TO_WISHLIST = false;
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


                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Map<String, Object> updateListSize = new HashMap<>();
                                        updateListSize.put("list_size", (long) DBqueries.wishlist.size() + 1);

                                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                                .update(updateListSize).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    if (DBqueries.wishlistModelList.size() != 0) {
                                                        DBqueries.wishlistModelList.add(new WishlistModel(
                                                                productID
                                                                , documentSnapshot.get("product_image_1").toString()
                                                                , documentSnapshot.get("product_title").toString() + " " + documentSnapshot.get("product_subtitle").toString()
                                                                , (long) documentSnapshot.get("free_coupon")
                                                                , documentSnapshot.get("average_rating").toString()
                                                                , (long) documentSnapshot.get("total_ratings")
                                                                , documentSnapshot.get("product_price").toString()
                                                                , documentSnapshot.get("cutted_price").toString()
                                                                , (Boolean) documentSnapshot.get("COD")
                                                        ));
                                                        DBqueries.loadRatingList(ProductDetailActivity.this);
                                                    }

                                                    ALREADY_ADDED_TO_WISHLIST = true;
                                                    addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent4));
                                                    DBqueries.wishlist.add(productID);
                                                    Toast.makeText(ProductDetailActivity.this, "Added to wishlist successfull", Toast.LENGTH_SHORT).show();
                                                } else {

                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(ProductDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                               running_wishlist_query= false;
                                            }
                                        });

                                    } else {
                                         running_wishlist_query= false;
                                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent3));
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
                        setRatting(starPosition);
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
                    Intent deliveryIntent = new Intent(ProductDetailActivity.this,DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null){
                    signInDialog.show();
                }else {
                    ////todo: add to cart

                }
            }
        });

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
        couponCouponBody2 = checkCouponPriceDialog.findViewById(R.id.reward_body2);
        coupondiscount = checkCouponPriceDialog.findViewById(R.id.discount_reward);

        TextView originalPrice = checkCouponPriceDialog.findViewById(R.id.original_price);
        TextView discountedPrice = checkCouponPriceDialog.findViewById(R.id.discounted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        couponRecyclerView.setLayoutManager(layoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("Cashback","till 3.9.2019","20 %","Diskon hingga Rp10rb","Min. Blj Rp300rb"));
        rewardModelList.add(new RewardModel("Cashback","till 3.9.2019","8 %","Diskon hingga Rp15rb",""));
        rewardModelList.add(new RewardModel("Cashback","till 3.9.2019","5 %","Diskon hingga Rp10rb",""));
        rewardModelList.add(new RewardModel("Cashback","till 3.9.2019","16 %","Diskon hingga Rp10rb","Min. Blj Rp400rb"));
        rewardModelList.add(new RewardModel("Cashback","till 3.9.2019","25 %","Diskon hingga Rp20rb","Min. Blj Rp600rb"));
        rewardModelList.add(new RewardModel("Cashback","till 3.9.2019","35 %","Diskon hingga Rp10rb","Min. Blj Rp900rb"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList,true);
        couponRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        couponRecyclerView.setVisibility(View.GONE);
        selectedCoupon.setVisibility(View.VISIBLE);

        toogleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }
        });

        /////coupondialog

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
                DBqueries.loadRatingList(ProductDetailActivity.this);
            } else {
                loadingDialog.dismiss();
            }
        }else {
            loadingDialog.dismiss();
        }

        if (DBqueries.wishlist.contains(productID)){
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent4));

        }else {
            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorAccent3));
            ALREADY_ADDED_TO_WISHLIST = false;
        }

    }

    public static void showDialogRecyclerView(){
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home){
            finish();
            return true;
        }else if(id ==R.id.search_icon){
            //todo: search
            return true;

        }else if(id ==R.id.main_chart_icon){
            if (currentUser==null){
                signInDialog.show();
            }else {
                Intent cartIntent = new Intent(ProductDetailActivity.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
