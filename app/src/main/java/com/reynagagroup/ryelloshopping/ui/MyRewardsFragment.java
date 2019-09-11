package com.reynagagroup.ryelloshopping.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.MyRewardsAdapter;
import com.reynagagroup.ryelloshopping.model.RewardModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class  MyRewardsFragment extends Fragment {


    public MyRewardsFragment() {
        // Required empty public constructor
    }

    private RecyclerView rewardRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);

        rewardRecyclerView = view.findViewById(R.id.my_rewards_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rewardRecyclerView.setLayoutManager(linearLayoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("Cashback","till 3.9.2019","20 %","Diskon hingga Rp10rb","Min. Blj Rp300rb"));
        rewardModelList.add(new RewardModel("Cashback","till 3.9.2019","8 %","Diskon hingga Rp15rb",""));
        rewardModelList.add(new RewardModel("Cashback","till 3.9.2019","5 %","Diskon hingga Rp10rb",""));
        rewardModelList.add(new RewardModel("Cashback","till 3.9.2019","16 %","Diskon hingga Rp10rb","Min. Blj Rp400rb"));
        rewardModelList.add(new RewardModel("Cashback","till 3.9.2019","25 %","Diskon hingga Rp20rb","Min. Blj Rp600rb"));
        rewardModelList.add(new RewardModel("Cashback","till 3.9.2019","35 %","Diskon hingga Rp10rb","Min. Blj Rp900rb"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList,false);
        rewardRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        return view;
    }

}
