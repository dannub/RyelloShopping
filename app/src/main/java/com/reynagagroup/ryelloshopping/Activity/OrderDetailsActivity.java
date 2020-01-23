package com.reynagagroup.ryelloshopping.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.MyOrderItemModel;
import com.reynagagroup.ryelloshopping.ui.MyOrdersFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderDetailsActivity extends AppCompatActivity {

    private int position;
    private MyOrderItemModel myOrderItemModel;

    //wighet
    private TextView productTitle;
    private TextView productPrice;
    private TextView productQuantity;
    private ImageView productImage;

    private ImageView orderedIndikator;
    private TextView orderedTitle;
    private TextView orderedDate;
    private TextView orderedBody;
    private TextView canceledOrdered;

    private ProgressBar orderedPackedProgressBar;

    private ImageView packedIndikator;
    private TextView packedTitle;
    private TextView packedDate;
    private TextView packedBody;
    private TextView canceledPacked;

    private ProgressBar packedShippingProgressBar;

    private ImageView shippingIndikator;
    private TextView shippingTitle;
    private TextView shippingDate;
    private TextView shippingBody;
    private TextView detailShipping;

    private Button copyResi;

    private ProgressBar shippingDeliveredProgressBar;

    private ImageView deliveredIndikator;
    private TextView deliveredTitle;
    private TextView deliveredDate;
    private TextView deliveredBody;


    private LinearLayout rateNowContainer;

    public static TextView fullname;
    public static TextView fullAddress;
    public static TextView pincode;

    public static final int SELECT_ADDRESS_ORDER = 1;

    private Button changeOraddNewAddressBtn;
    private Button Call;
    private Button cancel;
    private Button delivered;

    private Dialog loadingDialog;


    private  TextView totalItemsText;
    private  TextView totalItemPriceText;
    private  TextView deliveryPriceText;
    private  TextView totalAmountText;
    private  TextView saveAmountText;

    private int saveAmount;
    private int totalItemPrice;
    private int totalAmount;
    private Button hapus;
    private FirebaseFirestore firebaseFirestore ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Order details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        //loading dialog

        productTitle= findViewById(R.id.product_title);
        productPrice = findViewById(R.id.product_price);
        productQuantity = findViewById(R.id.product_quantity);
        productImage = findViewById(R.id.product_image);

        //Ordered
        orderedIndikator= findViewById(R.id.ordered_indicator);
        orderedTitle= findViewById(R.id.ordered_title);
        orderedDate= findViewById(R.id.ordered_date);
        orderedBody = findViewById(R.id.ordered_body);

        orderedPackedProgressBar = findViewById(R.id.ordered_packed_progressbar);

        canceledOrdered = findViewById(R.id.cancel_konfirmasi);

        firebaseFirestore =FirebaseFirestore.getInstance();

        hapus = findViewById(R.id.hapus);


        //Packed
        packedIndikator= findViewById(R.id.packed_indicator);
        packedTitle= findViewById(R.id.packed_title);
        packedDate= findViewById(R.id.packed_date);
        packedBody = findViewById(R.id.packed_body);

        packedShippingProgressBar = findViewById(R.id.packed_shipping_progressbar);

        canceledPacked = findViewById(R.id.cancel_dikemas);

        //Shipping
        shippingIndikator= findViewById(R.id.shipping_indicator);
        shippingTitle= findViewById(R.id.shipping_title);
        shippingDate= findViewById(R.id.shipping_date);
        shippingBody = findViewById(R.id.shipping_body);

        shippingDeliveredProgressBar = findViewById(R.id.shipping_delivered_progressbar);

        detailShipping = findViewById(R.id.detail_shiping);

        copyResi = findViewById(R.id.copy_resi);


        //Delivered
        deliveredIndikator= findViewById(R.id.delivered_indicator);
        deliveredTitle= findViewById(R.id.delivered_title);
        deliveredDate= findViewById(R.id.delivered_date);
        deliveredBody = findViewById(R.id.delivered_body);

        rateNowContainer = findViewById(R.id.rate_now_container);

        cancel= findViewById(R.id.cancel_order);

        fullname = findViewById(R.id.fullname);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);

        changeOraddNewAddressBtn = findViewById(R.id.change_or_add_address_btn);

        totalItemsText = findViewById(R.id.total_items);
        totalItemPriceText = findViewById(R.id.total_items_price);
        deliveryPriceText = findViewById(R.id.delivery_price);
        totalAmountText = findViewById(R.id.total_price);
        saveAmountText = findViewById(R.id.saved_amount);
        Call = findViewById(R.id.call);

     //   DBqueries.loadOrders(OrderDetailsActivity.this,loadingDialog,MyOrdersFragment.layoutManager,MyOrdersFragment.myOrderRecycleView);

        position = getIntent().getIntExtra("position",0);
        myOrderItemModel = DBqueries.myOrderItemModelArrayList.get(position);

        delivered = findViewById(R.id.pesanan_sampai);



        setProgress();





        /////ratting layout
        setRatting(myOrderItemModel.getRatting());
        for (int x = 0; x <rateNowContainer.getChildCount();x++){
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingDialog.show();
                    setRatting(starPosition);
                    final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS")
                            .document(myOrderItemModel.getProductID());
                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {

                        @Nullable
                        @Override
                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);

                            if (myOrderItemModel.getRatting() != 0) {
                                Long increase = documentSnapshot.getLong(starPosition + 1 + "_star") + 1;
                                Long decrease = documentSnapshot.getLong(myOrderItemModel.getRatting() + 1 + "_star") - 1;
                                Long total = documentSnapshot.getLong("total_ratings");


                                Double totalStars = Double.valueOf(0);
                                for (int x = 1; x < 6; x++) {
                                    if ((x != starPosition + 1) && (x != myOrderItemModel.getRatting() + 1)) {
                                        totalStars = totalStars + (documentSnapshot.getLong(x + "_star") * x);
                                    }
                                }

                                totalStars = totalStars + (increase * (starPosition + 1)) + (decrease * (myOrderItemModel.getRatting() + 1));
                                String rate = String.valueOf(Double.valueOf(totalStars / total)).substring(0, 3);
                                Log.i("total", rate);

                                transaction.update(documentReference, starPosition + 1 + "_star", increase);
                                transaction.update(documentReference, myOrderItemModel.getRatting() + 1 + "_star", decrease);
                                transaction.update(documentReference, "average_ratting", rate);
                            } else {
                                Long increase = documentSnapshot.getLong(starPosition + 1 + "_star") + 1;


                                Long total = documentSnapshot.getLong("total_ratings");


                                Double totalStars = Double.valueOf(0);
                                for (int x = 1; x < 6; x++) {
                                    if ((x != starPosition + 1)) {
                                        totalStars = totalStars + (documentSnapshot.getLong(x + "_star") * x);
                                    }
                                }


                            }


                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Object>() {
                        @Override
                        public void onSuccess(Object o) {

                            Map<String, Object> myRating = new HashMap<>();
                            if (DBqueries.myRatedIds.contains(myOrderItemModel.getProductID())) {
                                myRating.put("rating_" + DBqueries.myRatedIds.indexOf(myOrderItemModel.getProductID()), (long) starPosition + 1);
                            } else {
                                myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                myRating.put("product_ID_" + DBqueries.myRatedIds.size(), myOrderItemModel.getProductID());
                                myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);
                            }


                            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                    .document("MY_RATINGS").update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        DBqueries.myOrderItemModelArrayList.get(position).setRatting(starPosition+1);
                                        if (DBqueries.myRatedIds.contains(myOrderItemModel.getProductID())){
                                            DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(myOrderItemModel.getProductID()), Long.parseLong(String.valueOf(starPosition+1)));
                                        }else {
                                            DBqueries.myRatedIds.add(myOrderItemModel.getProductID());
                                            DBqueries.myRating.add(Long.parseLong(String.valueOf(starPosition+1)));
                                        }
                                        loadingDialog.dismiss();
                                    }else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });

                }
            });
        }
        /////ratting layout


        productTitle.setText(myOrderItemModel.getProductTitle());
        if (!TextUtils.isEmpty(myOrderItemModel.getOriPrice())){
            productPrice.setText("Rp."+myOrderItemModel.getProductPrice()+"/-");
            if (!TextUtils.isEmpty(myOrderItemModel.getSelectedCouponId())){
                productPrice.setText("Rp."+myOrderItemModel.getDiscountedPrice()+"/-");
            }
        }else {
            productPrice.setText("Rp."+myOrderItemModel.getProductPrice()+"/-");
            if (!TextUtils.isEmpty(myOrderItemModel.getSelectedCouponId())){
                productPrice.setText("Rp."+myOrderItemModel.getDiscountedPrice()+"/-");
            }
        }


        productQuantity.setText("Qty:"+myOrderItemModel.getProductQuantity());
        Glide.with(this).load(myOrderItemModel.getProductImage()).apply(new RequestOptions().placeholder(R.drawable.load)).into(productImage);


        fullname.setText(myOrderItemModel.getFullnameAddress());
        fullAddress.setText(myOrderItemModel.getFullAddress());
        pincode.setText(myOrderItemModel.getPincodeAddress());

        changeOraddNewAddressBtn.setVisibility(View.GONE);

        totalItemsText.setText("Price("+myOrderItemModel.getProductQuantity()+" items)");
        if (myOrderItemModel.getDeliveryPrice().equals("FREE")) {
            deliveryPriceText.setText(myOrderItemModel.getDeliveryPrice());
            totalAmount =0;
        }else {
            deliveryPriceText.setText("Rp."+myOrderItemModel.getDeliveryPrice()+"/-");
            totalAmount =20000;
        }


        int qty = Integer.parseInt(String.valueOf(myOrderItemModel.getProductQuantity()));
        if (TextUtils.isEmpty(myOrderItemModel.getSelectedCouponId())){
            totalItemPrice = (Integer.parseInt(myOrderItemModel.getProductPrice())* qty);

        }else {
            totalItemPrice = (Integer.parseInt(myOrderItemModel.getDiscountedPrice())* qty);
        }
        totalAmount = totalAmount + totalItemPrice;

        if (!TextUtils.isEmpty(myOrderItemModel.getOriPrice())){
            saveAmount =  (Integer.parseInt(myOrderItemModel.getOriPrice())-Integer.parseInt(myOrderItemModel.getProductPrice()))*qty;
            if (!TextUtils.isEmpty(myOrderItemModel.getSelectedCouponId())){
                saveAmount = saveAmount+(Integer.parseInt(myOrderItemModel.getProductPrice())-Integer.parseInt(myOrderItemModel.getDiscountedPrice()));
            }
        }else {
            if (!TextUtils.isEmpty(myOrderItemModel.getSelectedCouponId())){
                saveAmount = (Integer.parseInt(myOrderItemModel.getProductPrice())-Integer.parseInt(myOrderItemModel.getDiscountedPrice()));
            }
        }


        totalItemPriceText.setText("Rp."+totalItemPrice+"/-");
        totalAmountText.setText("Rp."+totalAmount+"/-");
        saveAmountText.setText("You saved Rp." + saveAmount + "/- on this order");

        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
                startActivity(intent);
            }
        });

        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(OrderDetailsActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Terima Kasih")
                        .setContentText("Pastikan pesanan benar-benar sampai di lokasi?")
                        .setConfirmText("Ya")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                loadingDialog.show();
                                Map < String, Object > updateCancel = new HashMap<>();
                                updateCancel.put("delivered",true);
                                final Date date = new Date();
                                updateCancel.put("delivered_date",date);


                                firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_NOTA").document(myOrderItemModel.getId_nota())
                                        .update(updateCancel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            sweetAlertDialog.dismiss();
                                            myOrderItemModel.setDelivered(true);
                                            myOrderItemModel.setDelivered_date(date);
                                            setProgress();
                                            loadingDialog.dismiss();
                                        }else {
                                            sweetAlertDialog.dismiss();
                                            String error = task.getException().getMessage();
                                            Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                            loadingDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        }).show();
            }
        });

        copyResi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("No.Resi tersalin", myOrderItemModel.getKet_kirim());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(OrderDetailsActivity.this,"No.Resi tersalin",Toast.LENGTH_SHORT).show();

            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(OrderDetailsActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Hapus Data!")
                        .setContentText("Apakah benar data ini akan dihapus?")
                        .setConfirmText("Ya, Hapus!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {

                                loadingDialog.show();
                                Map < String, Object > updateHapus = new HashMap<>();
                                updateHapus.put("ishapus",true);

                                firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_NOTA").document(myOrderItemModel.getId_nota())
                                        .update(updateHapus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            sDialog.dismiss();
                                            DBqueries.loadOrders(MyOrdersFragment.context,MyOrdersFragment.loadingDialog,MyOrdersFragment.layoutManager, MyOrdersFragment.myOrderRecycleView);
                                            finish();
                                            Toast.makeText(MyOrdersFragment.context, "Pesanan telah di Hapus", Toast.LENGTH_SHORT).show();
                                        }else {
                                            sDialog.dismiss();
                                            String error = task.getException().getMessage();
                                            Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                            loadingDialog.dismiss();
                                        }
                                    }
                                });
                            }
                        })
                        .show();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(OrderDetailsActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Cancel Order!")
                        .setContentText("Apakah benar pemesanan ini akan dibatalkan (Ini akan membatalkan semua pesanan anda)?")
                        .setConfirmText("Ya, Batalkan!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                             @Override
                             public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                 loadingDialog.show();
                                 Map < String, Object > updateCancel = new HashMap<>();
                                 updateCancel.put("canceled",true);
                                 final Date date = new Date();
                                 updateCancel.put("canceled_date",date);


                                 firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_NOTA").document(myOrderItemModel.getId_nota())
                                         .update(updateCancel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                sweetAlertDialog.dismiss();
                                                myOrderItemModel.setCanceled(true);
                                                myOrderItemModel.setCanceled_date(date);
                                                setProgress();
                                                loadingDialog.dismiss();
                                            }else {
                                                sweetAlertDialog.dismiss();
                                                String error = task.getException().getMessage();
                                                Toast.makeText(OrderDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                loadingDialog.dismiss();
                                            }
                                     }
                                 });
                             }
                         }).show();

            }
        });

    }

    private String changeDate(Date date){
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
        String day = sdf2.format(date);
        return day;
    }

    private void setRatting(int starPosition) {
        if (starPosition>0) {
            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
                starBtn.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
                if (x <= starPosition) {
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
                }

            }
        }
    }

    private void setProgress(){
        initProgress();
        if (myOrderItemModel.isOrdered()){
            orderedDate.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            orderedDate.setText(changeDate(myOrderItemModel.getOrdered_date()));
            orderedIndikator.setImageTintList(getResources().getColorStateList(R.color.colorSuccess));
            orderedBody.setVisibility(View.VISIBLE);
            orderedPackedProgressBar.setProgress(100);
            if (myOrderItemModel.isPacked()){
                cancel.setVisibility(View.VISIBLE);
                packedDate.setVisibility(View.VISIBLE);
                packedDate.setText(changeDate(myOrderItemModel.getPacked_date()));
                packedBody.setVisibility(View.VISIBLE);
                packedIndikator.setImageTintList(getResources().getColorStateList(R.color.colorSuccess));
                packedShippingProgressBar.setProgress(100);
                if (myOrderItemModel.isShipped()){
                    shippingDate.setVisibility(View.VISIBLE);
                    cancel.setVisibility(View.GONE);
                    shippingBody.setVisibility(View.VISIBLE);
                    shippingDate.setText(changeDate(myOrderItemModel.getShipped_date()));
                    shippingIndikator.setImageTintList(getResources().getColorStateList(R.color.colorSuccess));
                    shippingDeliveredProgressBar.setProgress(100);
                    delivered.setVisibility(View.VISIBLE);
                    detailShipping.setText(myOrderItemModel.getMetode_kirim()+" Resi : "+myOrderItemModel.getKet_kirim());
                    detailShipping.setVisibility(View.VISIBLE);
                    copyResi.setVisibility(View.VISIBLE);
                    if (myOrderItemModel.isDelivered()){
                        deliveredBody.setVisibility(View.VISIBLE);
                        deliveredDate.setVisibility(View.VISIBLE);
                        hapus.setVisibility(View.VISIBLE);
                        delivered.setVisibility(View.GONE);
                        cancel.setVisibility(View.GONE);
                        deliveredDate.setText(changeDate(myOrderItemModel.getDelivered_date()));
                        deliveredIndikator.setImageTintList(getResources().getColorStateList(R.color.colorSuccess));
                    }
                }
            }else {
                cancel.setVisibility(View.VISIBLE);
                if (myOrderItemModel.isCanceled()){
                    canceledPacked.setVisibility(View.VISIBLE);
                    hapus.setVisibility(View.VISIBLE);
                    canceledPacked.setText("Cancel Order \n "+changeDate(myOrderItemModel.getCanceled_date()));
                }
            }

        }else {
            cancel.setVisibility(View.VISIBLE);
            if (myOrderItemModel.isCanceled()){
                canceledOrdered.setVisibility(View.VISIBLE);
                hapus.setVisibility(View.VISIBLE);
                canceledOrdered.setText("Cancel Order \n "+changeDate(myOrderItemModel.getCanceled_date()));
            }
        }
    }

    private void initProgress(){
        orderedDate.setVisibility(View.GONE);
        orderedIndikator.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
        orderedBody.setVisibility(View.GONE);
        orderedPackedProgressBar.setProgress(0);
        packedDate.setVisibility(View.GONE);
        packedIndikator.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
        packedBody.setVisibility(View.GONE);
        packedShippingProgressBar.setProgress(0);
        shippingDate.setVisibility(View.GONE);
        shippingIndikator.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
        shippingBody.setVisibility(View.GONE);
        shippingDeliveredProgressBar.setProgress(0);
        deliveredDate.setVisibility(View.GONE);
        deliveredIndikator.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
        deliveredBody.setVisibility(View.GONE);
        copyResi.setVisibility(View.GONE);
        canceledOrdered.setVisibility(View.GONE);
        detailShipping.setVisibility(View.GONE);
        delivered.setVisibility(View.GONE);
        hapus.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            DBqueries.loadOrders(MyOrdersFragment.context,MyOrdersFragment.loadingDialog,MyOrdersFragment.layoutManager, MyOrdersFragment.myOrderRecycleView);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        DBqueries.loadOrders(MyOrdersFragment.context,MyOrdersFragment.loadingDialog,MyOrdersFragment.layoutManager, MyOrdersFragment.myOrderRecycleView);
        finish();
        super.onBackPressed();
    }
}
