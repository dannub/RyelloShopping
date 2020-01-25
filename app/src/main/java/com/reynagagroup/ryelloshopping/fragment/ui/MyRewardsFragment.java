package com.reynagagroup.ryelloshopping.fragment.ui;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private RecyclerView rewardRecyclerView;
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





        //loading dialog

        DBqueries.loadRewards(getContext(),loadingDialog,true);

        myRewardsAdapter = new MyRewardsAdapter(DBqueries.rewardModelList,false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rewardRecyclerView.setLayoutManager(linearLayoutManager);
        rewardRecyclerView.setAdapter(myRewardsAdapter);

        myRewardsAdapter.notifyDataSetChanged();



        return view;
    }

}
