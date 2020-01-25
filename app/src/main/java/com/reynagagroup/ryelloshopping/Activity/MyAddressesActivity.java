package com.reynagagroup.ryelloshopping.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.AddressesAdapter;

import java.util.HashMap;
import java.util.Map;

import info.hoang8f.widget.FButton;

import static com.reynagagroup.ryelloshopping.Activity.DeliveryActivity.SELECT_ADDRESS;

public class MyAddressesActivity extends AppCompatActivity {



    private int previousAddress;
    private FButton deliverHereBtn;
    public static TextView addressesSaved;
    public static RecyclerView myAddressesRecyclerView;
    private Button add_new_address_btn;
    public static AddressesAdapter addressesAdapter;
    private Dialog loadingDialog;
    private int mode;

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


        //loading dialog
        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                addressesSaved.setText(" "+String.valueOf(DBqueries.addressModelList.size())+" saved addresses ");
            }
        });
        //loading dialog


        previousAddress = DBqueries.selectedAddress;

        addressesSaved = findViewById(R.id.address_saved);
        deliverHereBtn = findViewById(R.id.delivery_here_btn);
        deliverHereBtn.setButtonColor(getResources().getColor(R.color.colorPrimaryDark));
        deliverHereBtn.setTextColor(getResources().getColor(R.color.colorAccent));

        myAddressesRecyclerView = findViewById(R.id.addresses_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myAddressesRecyclerView.setLayoutManager(linearLayoutManager);


         mode = getIntent().getIntExtra("MODE",-1);

        if(mode ==SELECT_ADDRESS){
            deliverHereBtn.setVisibility(View.VISIBLE);
        }else {
            deliverHereBtn.setVisibility(View.GONE);
        }
        deliverHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (DBqueries.selectedAddress!= previousAddress){
                        final int previousAddressIndex = previousAddress;
                        loadingDialog.show();
                        Map<String,Object> updateSelection = new HashMap<>();
                        updateSelection.put("selected_"+String.valueOf(previousAddress+1),false);
                        updateSelection.put("selected_"+String.valueOf(DBqueries.selectedAddress+1),true);

                        previousAddress = DBqueries.selectedAddress;

                        FirebaseFirestore.getInstance().collection("USERS")
                                .document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                .document("MY_ADDRESSES").update(updateSelection).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    finish();
                                }else {
                                    previousAddress = previousAddressIndex;
                                    String error = task.getException().getMessage();
                                    Toast.makeText(MyAddressesActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });
                 }
            }
        });

            addressesAdapter = new AddressesAdapter(DBqueries.addressModelList, mode,loadingDialog);
            myAddressesRecyclerView.setAdapter(addressesAdapter);
            ((SimpleItemAnimator) myAddressesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
            addressesAdapter.notifyDataSetChanged();

        add_new_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAddressIntent = new Intent(MyAddressesActivity.this,AddAddressActivity.class);
                if (mode!=SELECT_ADDRESS){
                    addAddressIntent.putExtra("INTENT","manage");
                }else {
                    addAddressIntent.putExtra("INTENT","null");
                }
                addAddressIntent.putExtra("INTENT","null");
                startActivity(addAddressIntent);
            }
        });

     }

    @Override
    protected void onStart() {
        super.onStart();
        addressesSaved.setText(" "+String.valueOf(DBqueries.addressModelList.size())+" saved addresses ");

    }

    public static void refreshItem(int deselect, int select){
        addressesAdapter.notifyItemChanged(deselect);
        addressesAdapter.notifyItemChanged(select);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
        if(item.getItemId()==android.R.id.home){
            if (mode==SELECT_ADDRESS) {
                if (DBqueries.selectedAddress != previousAddress) {
                    DBqueries.addressModelList.get(DBqueries.selectedAddress).setSelected(false);
                    DBqueries.addressModelList.get(previousAddress).setSelected(true);
                    DBqueries.selectedAddress = previousAddress;
                }
            }

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mode == OrderDetailsActivity.SELECT_ADDRESS_ORDER){
            finish();
        }else {
            if (DBqueries.selectedAddress != previousAddress) {
                DBqueries.addressModelList.get(DBqueries.selectedAddress).setSelected(false);
                DBqueries.addressModelList.get(previousAddress).setSelected(true);
                DBqueries.selectedAddress = previousAddress;
            }
        }
        super.onBackPressed();
    }
}
