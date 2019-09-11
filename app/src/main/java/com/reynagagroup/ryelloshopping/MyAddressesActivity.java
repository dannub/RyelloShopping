package com.reynagagroup.ryelloshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.reynagagroup.ryelloshopping.adapter.AddressesAdapter;
import com.reynagagroup.ryelloshopping.model.AddressModel;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

import static com.reynagagroup.ryelloshopping.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {

    private FButton deliverHereBtn;
    private RecyclerView myAddressesRecyclerView;
    private Button add_new_address_btn;

    private static AddressesAdapter addressesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addresses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        add_new_address_btn = findViewById(R.id.add_new_address_btn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Address");
        deliverHereBtn = findViewById(R.id.delivery_here_btn);
        deliverHereBtn.setButtonColor(getResources().getColor(R.color.colorPrimaryDark));
        deliverHereBtn.setTextColor(getResources().getColor(R.color.colorAccent));

        add_new_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAddressIntent = new Intent(MyAddressesActivity.this,AddAddressActivity.class);
                startActivity(addAddressIntent);
            }
        });


        myAddressesRecyclerView = findViewById(R.id.addresses_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(linearLayoutManager);


        List<AddressModel> addressModelList = new ArrayList<>();
        addressModelList.add(new AddressModel("Fardannu","Semarang, Wonosari","32322",true));
        addressModelList.add(new AddressModel("Fardannu B","Semarang, Wonosari","32322",false));
        addressModelList.add(new AddressModel("Fardannu C","Semarang, Wonosari","32322",false));
        addressModelList.add(new AddressModel("Hizbul","Semarang, Wonosari","32322",false));
        addressModelList.add(new AddressModel("Fardannu D","Semarang, Wonosari","32322",false));
        addressModelList.add(new AddressModel("Fardannu E","Semarang, Wonosari","32322",false));
        addressModelList.add(new AddressModel("Fardannu","Semarang, Wonosari","32322",false));

        int mode = getIntent().getIntExtra("MODE",-1);

        if(mode ==SELECT_ADDRESS){
            deliverHereBtn.setVisibility(View.VISIBLE);
        }else {
            deliverHereBtn.setVisibility(View.GONE);
        }

        addressesAdapter = new AddressesAdapter(addressModelList,mode);
        myAddressesRecyclerView.setAdapter(addressesAdapter);
        ((SimpleItemAnimator)myAddressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        addressesAdapter.notifyDataSetChanged();

    }

    public static void refreshItem(int deselect,int select){
        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
