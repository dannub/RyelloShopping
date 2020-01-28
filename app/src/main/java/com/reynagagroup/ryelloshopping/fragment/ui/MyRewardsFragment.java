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
import com.reynagagroup.ryelloshopping.adapter.MyRewardsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class  MyRewardsFragment extends Fragment {


    public MyRewardsFragment() {
        // Required empty public constructor
    }



    public static TextView noData;
    public static ConstraintLayout background;
    public static ImageView no_internet;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;


    public static RecyclerView rewardRecyclerView;
    private Dialog loadingDialog;
    public static MyRewardsAdapter myRewardsAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);


        //loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        rewardRecyclerView = view.findViewById(R.id.my_rewards_recycleview);
        linearLayoutManager = new LinearLayoutManager(getContext());


        noData = view.findViewById(R.id.pesanan);
        background =  view.findViewById(R.id.bg);
        no_internet = view.findViewById(R.id.no_internet_connection);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        connectivityManager =(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadPage();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


        //loading dialog


        myRewardsAdapter = new MyRewardsAdapter(DBqueries.rewardModelList,false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rewardRecyclerView.setLayoutManager(linearLayoutManager);
        rewardRecyclerView.setAdapter(myRewardsAdapter);

        myRewardsAdapter.notifyDataSetChanged();



        return view;
    }

    public void reloadPage(){
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo !=null && networkInfo.isConnected()==true) {

            DBqueries.loadRewards(getContext(),loadingDialog,true);
        }else {
            noData.setVisibility(View.GONE);
            background.setBackgroundColor(getContext().getResources().getColor(R.color.colorAccent));
            no_internet.setVisibility(View.VISIBLE);
            rewardRecyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        reloadPage();
    }
}
