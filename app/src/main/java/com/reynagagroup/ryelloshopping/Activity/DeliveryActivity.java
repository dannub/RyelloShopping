package com.reynagagroup.ryelloshopping.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.CartAdapter;
import com.reynagagroup.ryelloshopping.adapter.CartPaymentAdapter;
import com.reynagagroup.ryelloshopping.model.CartItemModel;
import com.reynagagroup.ryelloshopping.fragment.ui.MyCartFragment;

import java.util.ArrayList;
import java.util.List;

import static com.reynagagroup.ryelloshopping.Activity.PaymentActivity.fromCart;

public class DeliveryActivity extends AppCompatActivity {

    public  static List<CartItemModel> cartItemModelList;
    public static RecyclerView deliveryRecycleView;
    private Button changeOraddNewAddressBtn;
    public static final int SELECT_ADDRESS = 0;
    public static TextView totalAmount;
    public static Activity deliveryActivity;
    private TextView fullname;
    private TextView fullAddress;
    private TextView pincode;
    public  static CartAdapter cartlistAdapter;
    public static Dialog loadingDialog;
    private  Dialog paymentMethodDialog;
    private Button continueBtn;
    private Parcelable recyclerViewState;
    public  static LinearLayoutManager linearLayoutManager;
    private ImageButton bri,bca,bni,mandiri;
    private String  name,mobileNo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //loading dialog
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        //loading dialog

        //loading dialog
        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        //loading dialog


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        deliveryRecycleView = findViewById(R.id.delivery_recycleview);
        changeOraddNewAddressBtn = findViewById(R.id.change_or_add_address_btn);
        totalAmount = findViewById(R.id.total_cart_amount);
        fullname = findViewById(R.id.fullname);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        continueBtn = findViewById(R.id.deliv_continue_btn);





        cartlistAdapter = new CartAdapter(DeliveryActivity.this,deliveryRecycleView,linearLayoutManager,cartItemModelList,totalAmount,false,loadingDialog);
        cartlistAdapter.SetAdapter(cartlistAdapter);
        deliveryRecycleView.setAdapter(cartlistAdapter);




// Restore state

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecycleView.setLayoutManager(linearLayoutManager);

        recyclerViewState = deliveryRecycleView.getLayoutManager().onSaveInstanceState();
        deliveryRecycleView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        if (cartItemModelList.size()!=0) {
            linearLayoutManager.scrollToPosition(cartItemModelList.size() - 1);
        }
//        deliveryRecycleView.smoothScrollToPosition(0);
//        linearLayoutManager.scrollToPositionWithOffset(0, 0);

        cartlistAdapter.notifyDataSetChanged();




        changeOraddNewAddressBtn.setVisibility(View.VISIBLE);

        changeOraddNewAddressBtn.setVisibility(View.VISIBLE);
        changeOraddNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressIntent = new Intent(DeliveryActivity.this,MyAddressesActivity.class);
                myAddressIntent.putExtra("MODE",SELECT_ADDRESS);
                startActivity(myAddressIntent);

            }
        });
        loadingDialog.dismiss();
        if (!cartItemModelList.isEmpty()) {

            continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deliveryActivity = DeliveryActivity.this;

                    if (DBqueries.addressModelList.size() == 0) {
                        DBqueries.loadAddresses(DeliveryActivity.this, loadingDialog,true,SELECT_ADDRESS);
                    } else {

                        paymentMethodDialog.show();
                        bri = paymentMethodDialog.findViewById(R.id.bri);
                        bni = paymentMethodDialog.findViewById(R.id.bni);
                        bca = paymentMethodDialog.findViewById(R.id.bca);
                        mandiri = paymentMethodDialog.findViewById(R.id.mandiri);

                        bri.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PaymentActivity.cartItemModelList = new ArrayList<>();
                                PaymentActivity.cartItemModelList.clear();

                                for (int x = 0; x < cartItemModelList.size(); x++) {
                                    CartItemModel cartItemModel = cartItemModelList.get(x);
                                        PaymentActivity.cartItemModelList.add(cartItemModel);

                                }

                                PaymentActivity.cartPaymentAdapter = new CartPaymentAdapter(PaymentActivity.cartItemModelList);

                                paymentMethodDialog.dismiss();
                                Intent deliveryIntent = new Intent(DeliveryActivity.this, PaymentInfoActivity.class);
                                deliveryIntent.putExtra("bank",1);
                                startActivity(deliveryIntent);


                            }
                        });

                        bni.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PaymentActivity.cartItemModelList = new ArrayList<>();
                                PaymentActivity.cartItemModelList.clear();

                                for (int x = 0; x < cartItemModelList.size(); x++) {
                                    CartItemModel cartItemModel = cartItemModelList.get(x);
                                    if (cartItemModel.getInStock()) {
                                        PaymentActivity.cartItemModelList.add(cartItemModel);
                                    }
                                }

                                PaymentActivity.cartPaymentAdapter = new CartPaymentAdapter(PaymentActivity.cartItemModelList);


                                paymentMethodDialog.dismiss();
                                Intent deliveryIntent = new Intent(DeliveryActivity.this, PaymentActivity.class);
                                deliveryIntent.putExtra("bank",2);
                                startActivity(deliveryIntent);
                            }
                        });

                        bca.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PaymentActivity.cartItemModelList = new ArrayList<>();
                                PaymentActivity.cartItemModelList.clear();

                                for (int x = 0; x < cartItemModelList.size() ; x++) {
                                    CartItemModel cartItemModel = cartItemModelList.get(x);
                                 //   if (cartItemModel.getInStock()) {
                                        PaymentActivity.cartItemModelList.add(cartItemModel);
                                   // }
                                }

                                PaymentActivity.cartPaymentAdapter = new CartPaymentAdapter(PaymentActivity.cartItemModelList);


                                paymentMethodDialog.dismiss();
                                Intent deliveryIntent = new Intent(DeliveryActivity.this, PaymentInfoActivity.class);
                                deliveryIntent.putExtra("bank",3);
                                startActivity(deliveryIntent);
                            }
                        });

                        mandiri.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PaymentActivity.cartItemModelList = new ArrayList<>();
                                PaymentActivity.cartItemModelList.clear();

                                for (int x = 0; x < cartItemModelList.size() - 1; x++) {
                                    CartItemModel cartItemModel = cartItemModelList.get(x);
                                    if (cartItemModel.getInStock()) {
                                        PaymentActivity.cartItemModelList.add(cartItemModel);
                                    }
                                }

                                PaymentActivity.cartPaymentAdapter = new CartPaymentAdapter(PaymentActivity.cartItemModelList);


                                paymentMethodDialog.dismiss();
                                Intent deliveryIntent = new Intent(DeliveryActivity.this, PaymentInfoActivity.class);
                                deliveryIntent.putExtra("bank",4);
                                startActivity(deliveryIntent);
                            }
                        });
//                        loadingDialog.dismiss();
//                        Intent deliveryIntent = new Intent(DeliveryActivity.this, PaymentActivity.class);
//                        startActivity(deliveryIntent);
                    }

                }


            });



        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        name = DBqueries.addressModelList.get(DBqueries.selectedAddress).getName();
        mobileNo = DBqueries.addressModelList.get(DBqueries.selectedAddress).getMobileNo();
        if (DBqueries.addressModelList.get(DBqueries.selectedAddress).getAlternativeMobileNo().equals("")){
            fullname.setText(name + " | "+mobileNo);
        }else {
            fullname.setText(name + " | "+mobileNo+" or "+DBqueries.addressModelList.get(DBqueries.selectedAddress).getAlternativeMobileNo());
        }
        String flatNo = DBqueries.addressModelList.get(DBqueries.selectedAddress).getFlatNo();
        String locality = DBqueries.addressModelList.get(DBqueries.selectedAddress).getLocality();
        String landmark = DBqueries.addressModelList.get(DBqueries.selectedAddress).getLandmark();
        String city = DBqueries.addressModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressModelList.get(DBqueries.selectedAddress).getState();
        if (landmark.equals("")) {
            fullAddress.setText(locality + " No."+flatNo+" "+city+" "+state);
        }else {
            fullAddress.setText(landmark+" "+locality + " No."+flatNo+" "+city+" "+state);
        }
        pincode.setText(DBqueries.addressModelList.get(DBqueries.selectedAddress).getPincode());

    }

    @Override
    public void onBackPressed() {
        if (fromCart) {
            linearLayoutManager = new LinearLayoutManager(DeliveryActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            MyCartFragment.cartItemsRecyclerView.setLayoutManager(linearLayoutManager);
            linearLayoutManager.scrollToPosition(cartItemModelList.size() - 1);
            MyCartFragment.cartlistAdapter.notifyDataSetChanged();
            deliveryActivity = null;
        }
        ProductDetailActivity.productDetailsActivity = null;
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            deliveryActivity = null;
            finish();

            if (fromCart){
            linearLayoutManager = new LinearLayoutManager(DeliveryActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            MyCartFragment.cartItemsRecyclerView.setLayoutManager(linearLayoutManager);

            linearLayoutManager.scrollToPosition(cartItemModelList.size() - 1);
            MyCartFragment.cartlistAdapter.notifyDataSetChanged();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
