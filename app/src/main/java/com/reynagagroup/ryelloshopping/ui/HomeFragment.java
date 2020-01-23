package com.reynagagroup.ryelloshopping.ui;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.Activity.MainActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.CategoryAdapter;
import com.reynagagroup.ryelloshopping.adapter.HomePageAdapter;
import com.reynagagroup.ryelloshopping.model.CategoryModel;
import com.reynagagroup.ryelloshopping.model.HomePageModel;
import com.reynagagroup.ryelloshopping.model.HorizontalProductScrollModel;
import com.reynagagroup.ryelloshopping.model.SliderModel;
import com.reynagagroup.ryelloshopping.model.WishlistModel;

import java.util.ArrayList;
import java.util.List;

import static com.reynagagroup.ryelloshopping.DBqueries.categoryModelList;
import static com.reynagagroup.ryelloshopping.DBqueries.lists;
import static com.reynagagroup.ryelloshopping.DBqueries.loadCategories;
import static com.reynagagroup.ryelloshopping.DBqueries.loadFragmentData;
import static com.reynagagroup.ryelloshopping.DBqueries.loadedCategoriesNames;
import static com.reynagagroup.ryelloshopping.Activity.MainActivity.badgeCount;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;

    public static SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryModelFakeList = new ArrayList<>();
    private RecyclerView home_page_recycle;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private HomePageAdapter adapter;

    private ImageView noInternetConnection;
    private  Button retryBtn;


    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);
        categoryRecyclerView = view.findViewById(R.id.category_recycleview);
        home_page_recycle = view.findViewById(R.id.home_page_recycle);
        retryBtn = view.findViewById(R.id.retry_btn);

        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary),getContext().getResources().getColor(R.color.colorPrimary));





        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        home_page_recycle.setLayoutManager(testingLayoutManager);


        //categories fake list
        categoryModelFakeList.add(new CategoryModel("null",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));

        //categories fake list

        ///home page fake list
        List<SliderModel> sliderModeFakelList = new ArrayList<>();
        sliderModeFakelList.add(new SliderModel("null","#dfdfdf"));
        sliderModeFakelList.add(new SliderModel("null","#dfdfdf"));
        sliderModeFakelList.add(new SliderModel("null","#dfdfdf"));
        sliderModeFakelList.add(new SliderModel("null","#dfdfdf"));
        sliderModeFakelList.add(new SliderModel("null","#dfdfdf"));

        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",","","","",""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel(",","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModeFakelList));
        homePageModelFakeList.add(new HomePageModel(1,"","#dfdfdf"));
        homePageModelFakeList.add(new HomePageModel(2,"","#dfdfdf",horizontalProductScrollModelFakeList,new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#dfdfdf",horizontalProductScrollModelFakeList));
        ///home page fake list


        categoryAdapter = new CategoryAdapter(categoryModelFakeList);
        categoryRecyclerView.setAdapter(categoryAdapter);

        adapter = new HomePageAdapter(homePageModelFakeList);


        connectivityManager =(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


        Log.i("hvhvv","hhhj");

        if (networkInfo !=null && networkInfo.isConnected()==true) {
            MainActivity.drawer.setDrawerLockMode(0);
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);

            categoryRecyclerView.setVisibility(View.VISIBLE);
            home_page_recycle.setVisibility(View.VISIBLE);

            if (categoryModelList.size() == 0){
                loadCategories(categoryRecyclerView,getContext());
            }else {
                categoryAdapter = new CategoryAdapter(categoryModelList);
                categoryAdapter.notifyDataSetChanged();
            }
            categoryRecyclerView.setAdapter(categoryAdapter);

            if (lists.size() == 0){
                loadedCategoriesNames.add("HOME");
                lists.add(new ArrayList<HomePageModel>());

                loadFragmentData(home_page_recycle,getContext(),0,"HOME");
            }else {
                adapter = new HomePageAdapter(lists.get(0));
                adapter.notifyDataSetChanged();
            }
            home_page_recycle.setAdapter(adapter);



        }else {
            MainActivity.drawer.setDrawerLockMode(1);
            categoryRecyclerView.setVisibility(View.GONE);
            home_page_recycle.setVisibility(View.GONE);
           Glide.with(this).load(R.drawable.no_internet).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
        }

        ////refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reloadPage();
            }
        });
        ////refresh layout

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage();
            }
        });

        return view;
    }

    @SuppressLint("WrongConstant")
    private void  reloadPage(){
        networkInfo = connectivityManager.getActiveNetworkInfo();

//        categoryModelList.clear();
       //  lists.clear();
//        loadedCategoriesNames.clear();
        DBqueries.clearData();

        if (networkInfo !=null && networkInfo.isConnected()==true) {
            MainActivity.drawer.setDrawerLockMode(0);
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);
            categoryRecyclerView.setVisibility(View.VISIBLE);
            home_page_recycle.setVisibility(View.VISIBLE);

            categoryAdapter = new CategoryAdapter(categoryModelFakeList);
            adapter= new HomePageAdapter(homePageModelFakeList);
            categoryRecyclerView.setAdapter(categoryAdapter);
            home_page_recycle.setAdapter(adapter);


            DBqueries.loadCartList(getContext(), new Dialog(getContext()),false,badgeCount,new TextView(getContext()),false,null);
            loadCategories(categoryRecyclerView,getContext());

            loadedCategoriesNames.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(home_page_recycle,getContext(),0,"HOME");


        }else {
            MainActivity.drawer.setDrawerLockMode(1);
            Toast.makeText(getContext(),"No internet Connection!",Toast.LENGTH_SHORT).show();
            categoryRecyclerView.setVisibility(View.GONE);
            home_page_recycle.setVisibility(View.GONE);
            Glide.with(getContext()).load(R.drawable.no_internet).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
