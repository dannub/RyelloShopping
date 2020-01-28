package com.reynagagroup.ryelloshopping.fragment.ui;


import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.MyOrderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFragment extends Fragment {


    public MyOrdersFragment() {
        // Required empty public constructor
    }

    public static RecyclerView myOrderRecycleView;
    public static Context context;

    public static TextView noData;
    public static ConstraintLayout background;
    public static ImageView no_internet;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    public static Dialog loadingDialog;
    public static MyOrderAdapter myOrderAdapter;
    public static LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);



        //loading dialog

        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        myOrderRecycleView = view.findViewById(R.id.my_orders_recyclerview);
        noData = view.findViewById(R.id.pesanan);
        background =  view.findViewById(R.id.bg);
        no_internet = view.findViewById(R.id.no_internet_connection);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        connectivityManager =(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();



        layoutManager = new LinearLayoutManager(getContext());


        context = getContext();

       // DBqueries.loadOrders(getContext(),loadingDialog,layoutManager,myOrderRecycleView);

        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadPage();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        return view;
    }

    public void reloadPage(){
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo !=null && networkInfo.isConnected()==true) {
            DBqueries.loadOrders(getContext(),loadingDialog,layoutManager,myOrderRecycleView);

        }else {
            noData.setVisibility(View.GONE);
            background.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            no_internet.setVisibility(View.VISIBLE);
            myOrderRecycleView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
      // DBqueries.myOrderItemModelArrayList.clear();
        reloadPage();


    }


}
