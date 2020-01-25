package com.reynagagroup.ryelloshopping.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

import static com.reynagagroup.ryelloshopping.DBqueries.lists;
import static com.reynagagroup.ryelloshopping.DBqueries.loadCategories;
import static com.reynagagroup.ryelloshopping.DBqueries.loadFragmentData;
import static com.reynagagroup.ryelloshopping.DBqueries.loadedCategoriesNames;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView categoryRecycleView;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private HomePageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String title = getIntent().getStringExtra("CategoryName");

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///home page fake list
        List<SliderModel> sliderModeFakelList = new ArrayList<>();
        sliderModeFakelList.add(new SliderModel("null","#ffffff"));
        sliderModeFakelList.add(new SliderModel("null","#ffffff"));
        sliderModeFakelList.add(new SliderModel("null","#ffffff"));
        sliderModeFakelList.add(new SliderModel("null","#ffffff"));
        sliderModeFakelList.add(new SliderModel("null","#ffffff"));
        sliderModeFakelList.add(new SliderModel("null","#ffffff"));


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
        homePageModelFakeList.add(new HomePageModel(1,"","#ffffff"));
        homePageModelFakeList.add(new HomePageModel(2,"","#ffffff",horizontalProductScrollModelFakeList,new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"","#ffffff",horizontalProductScrollModelFakeList));
        ///home page fake list


        categoryRecycleView = findViewById(R.id.category_recycleview);
       LinearLayoutManager testingLayoutManager = new LinearLayoutManager(this);
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecycleView.setLayoutManager(testingLayoutManager);
        adapter = new HomePageAdapter(homePageModelFakeList);
        categoryRecycleView.setAdapter(adapter);

        int listPosition = 0;
        for (int x = 0;x< loadedCategoriesNames.size();x++){
            if (loadedCategoriesNames.get(x).equals(title.toUpperCase())){
                listPosition = x;
            }
        }
        if (listPosition==0){
            loadedCategoriesNames.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(categoryRecycleView,this,loadedCategoriesNames.size() -1,title );
        }else {
            adapter = new HomePageAdapter(lists.get(listPosition));
        }


        adapter.notifyDataSetChanged();

        ///////////////////////////////////////




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.search_icon){
            Intent searchIntent = new Intent(this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }else if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
