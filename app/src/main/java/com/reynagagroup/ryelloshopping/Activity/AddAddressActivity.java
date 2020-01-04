package com.reynagagroup.ryelloshopping.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.AddressModel;

import java.util.HashMap;
import java.util.Map;

import info.hoang8f.widget.FButton;

public class AddAddressActivity extends AppCompatActivity {

    private EditText city;
    private EditText locality;
    private EditText flatNo;
    private EditText pincode;
    private Spinner stateSpinner;
    private EditText landmark;
    private EditText name;
    private EditText mobileNo;
    private EditText alternativeMobileNo;


    private Dialog loadingDialog;

    private String [] stateList;
    private String selectedState;

    private FButton saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Add a new address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //loading dialog
        loadingDialog = new Dialog(AddAddressActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        //loading dialog

        stateList  = getResources().getStringArray(R.array.countries_array);


        saveBtn = findViewById(R.id.save_btn);
        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality);
        flatNo = findViewById(R.id.flat_no);
        pincode = findViewById(R.id.pincode);
        stateSpinner = findViewById(R.id.state_spinner);
        landmark = findViewById(R.id.landmark);
        name = findViewById(R.id.name);
        mobileNo = findViewById(R.id.mobile_no);
        alternativeMobileNo = findViewById(R.id.alternative_mobile_no);

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,stateList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        stateSpinner.setAdapter(spinnerAdapter);

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = stateList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        saveBtn.setButtonColor(getResources().getColor(R.color.colorPrimary));
        saveBtn.setTextColor(getResources().getColor(R.color.colorAccent));
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(city.getText())){
                    if (!TextUtils.isEmpty(locality.getText())){
                        if (!TextUtils.isEmpty(flatNo.getText())){
                            if (!TextUtils.isEmpty(pincode.getText()) && pincode.getText().length() >= 5){
                                 if (!TextUtils.isEmpty(name.getText())){
                                        if (!TextUtils.isEmpty(mobileNo.getText())&& mobileNo.getText().length() <= 14){

                                                loadingDialog.show();

                                                final String fullAddress = locality.getText().toString()
                                                        +" No."+flatNo.getText().toString()
                                                        +" "+landmark.getText().toString()
                                                        +" "+city.getText().toString()
                                                        +" "+selectedState;

                                                Map<String ,Object> addAddress = new HashMap();
                                                addAddress.put("list_size",(long) DBqueries.addressModelList.size()+1);
                                                if (TextUtils.isEmpty(alternativeMobileNo.getText())) {
                                                    addAddress.put("fullname_" + String.valueOf((long) DBqueries.addressModelList.size() + 1), name.getText().toString() + " | " + mobileNo.getText().toString());
                                                }else {
                                                    addAddress.put("fullname_" + String.valueOf((long) DBqueries.addressModelList.size() + 1), name.getText().toString() + " | " + mobileNo.getText().toString()+" or "+alternativeMobileNo.getText().toString());
                                                }
                                                addAddress.put("address_"+String.valueOf((long)DBqueries.addressModelList.size()+1), fullAddress);
                                                addAddress.put("phone_"+String.valueOf((long)DBqueries.addressModelList.size()+1), mobileNo.getText().toString());
                                                addAddress.put("pincode_"+String.valueOf((long)DBqueries.addressModelList.size()+1),pincode.getText().toString());
                                                addAddress.put("selected_"+String.valueOf((long)DBqueries.addressModelList.size()+1), true);
                                                if (DBqueries.addressModelList.size()>0) {
                                                    addAddress.put("selected_"+(DBqueries.selectedAddress+1), false);
                                                }

                                                FirebaseFirestore.getInstance().collection("USERS")
                                                        .document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                                        .document("MY_ADDRESSES")
                                                        .update(addAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()){
                                                            if (DBqueries.addressModelList.size()>0) {
                                                                DBqueries.addressModelList.get(DBqueries.selectedAddress).setSelected(false);
                                                            }
                                                            if (TextUtils.isEmpty(alternativeMobileNo.getText())){
                                                                DBqueries.addressModelList.add(new AddressModel(name.getText().toString()+ " | "+mobileNo.getText().toString(),fullAddress,pincode.getText().toString(),mobileNo.getText().toString(),true));
                                                            }else {
                                                                DBqueries.addressModelList.add(new AddressModel(name.getText().toString()+ " | "+mobileNo.getText().toString()+ " or "+alternativeMobileNo.getText().toString(),fullAddress,pincode.getText().toString(),mobileNo.getText().toString(),true));
                                                            }
                                                           if (getIntent().getStringExtra("INTENT").equals("deliveryIntent")) {
                                                                Intent deliveryIntent = new Intent(AddAddressActivity.this, DeliveryActivity.class);
                                                                startActivity(deliveryIntent);
                                                            }else {
                                                                MyAddressesActivity.refreshItem(DBqueries.selectedAddress,DBqueries.addressModelList.size()-1);
                                                            }
                                                            DBqueries.selectedAddress = DBqueries.addressModelList.size() - 1;
                                                            finish();
                                                        }else {
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(AddAddressActivity.this, error, Toast.LENGTH_SHORT).show();
                                                        }

                                                        loadingDialog.dismiss();
                                                    }
                                                });



                                        }else {
                                            mobileNo.requestFocus();
                                            Toast.makeText(AddAddressActivity.this,"Please provide valid mobile number",Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        name.requestFocus();
                                    }

                            }else {
                                pincode.requestFocus();
                                Toast.makeText(AddAddressActivity.this,"Please provide valid pincode",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            flatNo.requestFocus();
                        }
                    }else {
                        locality.requestFocus();
                    }

                }else {
                    city.requestFocus();
                }


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
