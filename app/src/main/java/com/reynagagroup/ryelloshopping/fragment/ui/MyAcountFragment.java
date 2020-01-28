package com.reynagagroup.ryelloshopping.fragment.ui;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Gravity;
import android.view.LayoutInflater;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.reynagagroup.ryelloshopping.Activity.MainActivity;
import com.reynagagroup.ryelloshopping.Activity.MyAddressesActivity;
import com.reynagagroup.ryelloshopping.Activity.UpdateUserInfoActivity;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.MyOrderItemModel;
import com.tooltip.Tooltip;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAcountFragment extends Fragment {


    public MyAcountFragment() {
        // Required empty public constructor
    }

    private FloatingActionButton settingBtn;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Button viewAllAddressBtn;
    public static final int MANAGE_ADDRESS =1;
    private CircleImageView profileview,currentOrderImage;
    private TextView name,email,tvCurrentOrderStatus,currentOrderTitle;
    private LinearLayout layoutContainer,recentOrdersContainer;
    private Dialog loadingDialog;
    private ImageView orderIndicator,packedIndicator,shippedIndicator,deliveredIndicator;
    private ProgressBar O_P_progreess,P_S_progress,S_D_progress;
    private TextView yourRecentOrdersTitle;
    private TextView addressname,address,pincode;
    private  Button signoutBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_acount, container, false);


        //loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        //loading dialog

        swipeRefreshLayout = view.findViewById(R.id.swipe);
        layoutContainer = view.findViewById(R.id.layout_container);
        profileview = view.findViewById(R.id.profile_image);
        name = view.findViewById(R.id.fullname);
        email = view.findViewById(R.id.user_email);
        currentOrderImage = view.findViewById(R.id.current_order_image);
        currentOrderTitle = view.findViewById(R.id.current_order_tv);
        tvCurrentOrderStatus = view.findViewById(R.id.tv_current_order_status);
        orderIndicator = view.findViewById(R.id.ordered_indikator);
        packedIndicator = view.findViewById(R.id.packed_indikator);
        shippedIndicator = view.findViewById(R.id.shipping_indikator);
        deliveredIndicator = view.findViewById(R.id.delivered_indikator);
        O_P_progreess = view.findViewById(R.id.ordered_packed_progressbar);
        P_S_progress = view.findViewById(R.id.packed_shipping_progressbar);
        S_D_progress = view.findViewById(R.id.shipping_delivered_progressbar);
        yourRecentOrdersTitle = view.findViewById(R.id.your_recent_orders_title);
        recentOrdersContainer = view.findViewById(R.id.recent_order_container);
        addressname = view.findViewById(R.id.address_fullname);
        address = view.findViewById(R.id.address);
        pincode = view.findViewById(R.id.address_pincode);
        signoutBtn = view.findViewById(R.id.sign_out_btn);
        settingBtn = view.findViewById(R.id.setting_btn);




        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadPage();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                DBqueries.clearData();
                MainActivity.drawer.closeDrawer(GravityCompat.START);
                MainActivity.mainActivity = null;
                MainActivity.currentFragment = -1;
                Toast.makeText(getContext(),"Logged Out successfully!",Toast.LENGTH_SHORT).show();
                Intent mainActivityIntent = new Intent(getContext(), MainActivity.class);
                MainActivity.showCart = false;
                startActivity(mainActivityIntent);
                getActivity().finish();
            }
        });

        layoutContainer.setVisibility(View.GONE);
        layoutContainer.getChildAt(1).setVisibility(View.GONE);





        viewAllAddressBtn = view.findViewById(R.id.view_all_addresses_btn);
        viewAllAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myAddressIntent = new Intent(getContext(), MyAddressesActivity.class);
                myAddressIntent.putExtra("MODE",MANAGE_ADDRESS);
                startActivity(myAddressIntent);

            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateUserInfo = new Intent(getContext(), UpdateUserInfoActivity.class);
                updateUserInfo.putExtra("Name",name.getText());
                updateUserInfo.putExtra("Email",email.getText());
                updateUserInfo.putExtra("Photo",DBqueries.profile);
                startActivity(updateUserInfo);
            }
        });


        return view;
    }

    private void reloadPage(){

        //DBqueries.loadOrders(getContext(), loadingDialog, null, null);


        loadingDialog.show();
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                loadingDialog.setOnDismissListener(null);
                    for (MyOrderItemModel orderItemModel : DBqueries.myOrderItemModelArrayList) {
                        if (!orderItemModel.isCanceled()) {
                            layoutContainer.getChildAt(1).setVisibility(View.VISIBLE);
                            currentOrderTitle.setText(orderItemModel.getProductTitle());
                            Glide.with(getContext()).load(orderItemModel.getProductImage()).apply(new RequestOptions().placeholder(getContext().getResources().getDrawable(R.drawable.load))).into(currentOrderImage);
                            setProgress(orderItemModel);


                        }
                    }

                int i =0;

                for (final MyOrderItemModel myOrderItemModel : DBqueries.myOrderItemModelArrayList) {
                    if (i<4) {
                        if (!myOrderItemModel.isCanceled()) {
                            Glide.with(getContext()).load(myOrderItemModel.getProductImage()).apply(new RequestOptions().placeholder(getContext().getResources().getDrawable(R.drawable.load))).into((CircleImageView) recentOrdersContainer.getChildAt(i));
                            recentOrdersContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Glide.with(getContext()).load(myOrderItemModel.getProductImage()).apply(new RequestOptions().placeholder(getContext().getResources().getDrawable(R.drawable.load))).into(currentOrderImage);
                                    setProgress(myOrderItemModel);

                                }
                            });
                            i++;
                        }

                    }else {
                        break;
                    }
                }
                if (i==0){
                    yourRecentOrdersTitle.setText("No recent Orders");
                }

                if (i<4){

                    for (int x=i;x<4;x++){
                        recentOrdersContainer.getChildAt(x).setVisibility(View.GONE);
                    }
                }
                layoutContainer.setVisibility(View.GONE);
                loadingDialog.show();

                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        loadingDialog.setOnDismissListener(null);
                        if (DBqueries.addressModelList.size()==0) {
                            addressname.setText("No Address");
                            address.setText("-");
                            pincode.setText("-");
                        }else {
                            setAddress();
                        }
                        layoutContainer.setVisibility(View.VISIBLE);
                    }
                });
                DBqueries.loadAddresses(getContext(), loadingDialog, false, 0);
                //loadingDialog.dismiss();

                layoutContainer.setVisibility(View.VISIBLE);

            }



        });
        DBqueries.loadOrders(getContext(), loadingDialog, null, null);

        //loadingDialog.dismiss();

    }

    private void setAddress() {
        String name,mobileNo;
        name = DBqueries.addressModelList.get(DBqueries.selectedAddress).getName();
        mobileNo = DBqueries.addressModelList.get(DBqueries.selectedAddress).getMobileNo();
        if (DBqueries.addressModelList.get(DBqueries.selectedAddress).getAlternativeMobileNo().equals("")){
            addressname.setText(name + " | "+mobileNo);
        }else {
            addressname.setText(name + " | "+mobileNo+" or "+DBqueries.addressModelList.get(DBqueries.selectedAddress).getAlternativeMobileNo());
        }
        String flatNo = DBqueries.addressModelList.get(DBqueries.selectedAddress).getFlatNo();
        String locality = DBqueries.addressModelList.get(DBqueries.selectedAddress).getLocality();
        String landmark = DBqueries.addressModelList.get(DBqueries.selectedAddress).getLandmark();
        String city = DBqueries.addressModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressModelList.get(DBqueries.selectedAddress).getState();
        if (landmark.equals("")) {
            address.setText(locality + " No."+flatNo+" "+city+" "+state);
        }else {
            address.setText(landmark+" "+locality + " No."+flatNo+" "+city+" "+state);
        }
        pincode.setText(DBqueries.addressModelList.get(DBqueries.selectedAddress).getPincode());

    }

    private void initProgress(){
        currentOrderTitle.setText("Pesananmu");
        tvCurrentOrderStatus.setText("Status pesananmu sekarang");
        orderIndicator.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
        O_P_progreess.setProgress(0);
        packedIndicator.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
        P_S_progress.setProgress(0);
        shippedIndicator.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));
        S_D_progress.setProgress(0);
        deliveredIndicator.setImageTintList(getResources().getColorStateList(R.color.colorAccent3));

        final Tooltip tooltip = new Tooltip.Builder(orderIndicator)

                .setText("Telah Dikonfirmasi")
                .setCancelable(true)
                .setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary))
                .setGravity(Gravity.TOP)
                .setTextColor(getContext().getResources().getColor(R.color.colorAccent)).build();

        orderIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tooltip.isShowing()){
                    tooltip.dismiss();
                }else {
                    tooltip.show();
                }
            }
        });


        final Tooltip tooltip2 = new Tooltip.Builder(packedIndicator)

                .setText("Telah Dikemas")
                .setCancelable(true)
                .setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary))
                .setGravity(Gravity.TOP)
                .setTextColor(getContext().getResources().getColor(R.color.colorAccent)).build();


        packedIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tooltip2.isShowing()){
                    tooltip2.dismiss();
                }else {
                    tooltip2.show();
                }
            }
        });

        final Tooltip tooltip3 = new Tooltip.Builder(shippedIndicator)

                .setText("Sedang Dikirim")
                .setCancelable(true)
                .setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary))
                .setGravity(Gravity.TOP)
                .setTextColor(getContext().getResources().getColor(R.color.colorAccent)).build();


        shippedIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tooltip3.isShowing()){
                    tooltip3.dismiss();
                }else {
                    tooltip3.show();
                }
            }
        });


        final Tooltip tooltip4 = new Tooltip.Builder(deliveredIndicator)

                .setText("Telah Sampai")
                .setCancelable(true)
                .setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary))
                .setGravity(Gravity.TOP)
                .setTextColor(getContext().getResources().getColor(R.color.colorAccent)).build();


        deliveredIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tooltip4.isShowing()){
                    tooltip4.dismiss();
                }else {
                    tooltip4.show();
                }
            }
        });




    }

    private void setProgress(MyOrderItemModel myOrderItemModel){
        initProgress();
        currentOrderTitle.setText(myOrderItemModel.getProductTitle());
        if (myOrderItemModel.isOrdered()) {
            currentOrderTitle.setText(myOrderItemModel.getProductTitle());
            tvCurrentOrderStatus.setText("Telah Dikonfirmasi");
            orderIndicator.setImageTintList(getResources().getColorStateList(R.color.colorSuccess));

            if (myOrderItemModel.isPacked()) {
                tvCurrentOrderStatus.setText("Telah dikemas");
                O_P_progreess.setProgress(100);
                packedIndicator.setImageTintList(getResources().getColorStateList(R.color.colorSuccess));

                if (myOrderItemModel.isShipped()) {
                    tvCurrentOrderStatus.setText("Sedang dikirim");
                    P_S_progress.setProgress(100);
                    shippedIndicator.setImageTintList(getResources().getColorStateList(R.color.colorSuccess));

                    if (myOrderItemModel.isDelivered()) {
                        S_D_progress.setProgress(100);
                        tvCurrentOrderStatus.setText("Telah sampai");
                        deliveredIndicator.setImageTintList(getResources().getColorStateList(R.color.colorSuccess));
                    }
                }
            }
        }else {
            tvCurrentOrderStatus.setText("Menunggu konfirmasi");

        }

    }



    @Override
    public void onStart() {
       super.onStart();

        name.setText(DBqueries.fullname);
        email.setText(DBqueries.email);


        ///drawer update
        MainActivity.fullnameText.setText(DBqueries.fullname);
        MainActivity.email.setText(DBqueries.email);
        if (!DBqueries.profile.equals("")){
           // MainActivity.imageAdd.setVisibility(View.INVISIBLE);
            Glide.with(getContext()).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.account)).into(MainActivity.imageAcc);

        }else {
            //MainActivity.imageAdd.setVisibility(View.VISIBLE);
            MainActivity.imageAcc.setImageDrawable(getResources().getDrawable(R.drawable.account));

        }
        ///drawer update

        if (!DBqueries.profile.equals("")){
            Glide.with(getContext()).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.account)).into(profileview);

        }else {
            profileview.setImageDrawable(getContext().getResources().getDrawable(R.drawable.account));

        }

       if (!loadingDialog.isShowing()){
           if (DBqueries.addressModelList.size()==0) {
               addressname.setText("No Address");
               address.setText("-");
               pincode.setText("-");
           }else {
               setAddress();
           }

       }
       reloadPage();
    }
}
