package com.reynagagroup.ryelloshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.reynagagroup.ryelloshopping.adapter.CartAdapter;
import com.reynagagroup.ryelloshopping.model.CartItemModel;
import com.reynagagroup.ryelloshopping.ui.MyCartFragment;

import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {

    private RecyclerView deliveryRecycleView;
    private Button changeOraddNewAddressBtn;
    public static final int SELECT_ADDRESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        deliveryRecycleView = findViewById(R.id.delivery_recycleview);
        changeOraddNewAddressBtn = findViewById(R.id.change_or_add_address_btn);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecycleView.setLayoutManager(linearLayoutManager);

        List<CartItemModel> cartItemModelList = new ArrayList<>();
        cartItemModelList.add(new CartItemModel(0,R.drawable.barang,"BAN TERKENAL",2,"Rp. 499999/-","Rp.500000/-",1,0,0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.barang,"BAN TERKENAL",0,"Rp. 499999/-","Rp.500000/-",1,1,0));
        cartItemModelList.add(new CartItemModel(0,R.drawable.barang,"BAN TERKENAL",2,"Rp. 499999/-","Rp.500000/-",1,2,0));
        cartItemModelList.add(new CartItemModel(1,"Price (3 items)","Rp.1699999/-","Free","Rp 3000/-","Rp.1699999/-"));


        CartAdapter cartAdapter = new CartAdapter(cartItemModelList);
        deliveryRecycleView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
