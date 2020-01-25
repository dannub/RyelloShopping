package com.reynagagroup.ryelloshopping.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reynagagroup.ryelloshopping.Activity.AddAddressActivity;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.AddressModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.reynagagroup.ryelloshopping.Activity.DeliveryActivity.SELECT_ADDRESS;
import static com.reynagagroup.ryelloshopping.Activity.MyAddressesActivity.refreshItem;
import static com.reynagagroup.ryelloshopping.fragment.ui.MyAcountFragment.MANAGE_ADDRESS;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.Viewholder> {

    private List<AddressModel> addressModelList;
    private int MODE;
    private int preSelectedPosition;
    private boolean refreesh =false;
    private Dialog loadingDialog;

    public AddressesAdapter(List<AddressModel> addressModelList, int MODE,Dialog loadingDialog) {
        this.addressModelList = addressModelList;
        this.MODE = MODE;
        preSelectedPosition = DBqueries.selectedAddress;

        this.loadingDialog = loadingDialog;


    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addresses_item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {

        String  city= addressModelList.get(position).getCity();
        String  locality=addressModelList.get(position).getLocality();
        String  flatNo=addressModelList.get(position).getFlatNo();
        String  pincode= addressModelList.get(position).getPincode();

        String  landmark= addressModelList.get(position).getLandmark();
        String  name= addressModelList.get(position).getName();
        String  mobileNo= addressModelList.get(position).getMobileNo();
        String  alternativeMobileNo=addressModelList.get(position).getAlternativeMobileNo();
        String  state=addressModelList.get(position).getState();
        Boolean selected = addressModelList.get(position).getSelected();
        viewholder.setData(city,locality,flatNo,pincode,landmark,name,mobileNo,alternativeMobileNo,state,selected,position);
    }

    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView fullname;
        private TextView address;
        private TextView pincode;
        private ImageView icon;
        private LinearLayout manage_address_container;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            pincode = itemView.findViewById(R.id.pincode);
            icon = itemView.findViewById(R.id.icon_view);
            manage_address_container = itemView.findViewById(R.id.manage_address_container);
            manage_address_container.setVisibility(View.GONE);
        }

        private  void setData(String  city, String locality, String flatNo, String pincode, String landmark, String name, String mobileNo, String  alternativeMobileNo, String state, Boolean selected, final int position){
            if (alternativeMobileNo.equals("")){
                fullname.setText(name + " | "+mobileNo);
            }else {
                fullname.setText(name + " | "+mobileNo+" or "+alternativeMobileNo);
            }
            if (landmark.equals("")) {
                address.setText(locality + " No."+flatNo+" "+city+" "+state);
            }else {
                address.setText(landmark+" "+locality + " No."+flatNo+" "+city+" "+state);
            }

            this.pincode.setText(pincode);

            if(MODE == SELECT_ADDRESS){
                icon.setImageResource(R.drawable.check);
                if(selected){
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                }else {
                    icon.setVisibility(View.INVISIBLE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(preSelectedPosition != position){
                            addressModelList.get(position).setSelected(true);
                            addressModelList.get(preSelectedPosition).setSelected(false);
                            refreshItem(preSelectedPosition,position);
                            preSelectedPosition =position;
                            DBqueries.selectedAddress = position;
                        }

                    }
                });

            }else if(MODE == MANAGE_ADDRESS){
                manage_address_container.setVisibility(View.GONE);
                manage_address_container.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//edit address
                        Intent addAddressIntent = new Intent(itemView.getContext(), AddAddressActivity.class);
                        addAddressIntent.putExtra("INTENT","update_address");
                        addAddressIntent.putExtra("index",position);
                        itemView.getContext().startActivity(addAddressIntent);
                        refreesh = false;
                    }
                });

                manage_address_container.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//remove address

                        loadingDialog.show();
                        int x = 0;
                        int selected = -1;
                        Map<String,Object> addresses = new HashMap<>();
                        for (int i =0;i<addressModelList.size();i++){
                            if (i!=position){
                                x++;
                                addresses.put("city_"+x,addressModelList.get(i).getCity());
                                addresses.put("locality_"+x,addressModelList.get(i).getLocality());
                                addresses.put("flatNo_"+x,addressModelList.get(i).getFlatNo());
                                addresses.put("pincode_"+x,addressModelList.get(i).getPincode());
                                addresses.put("landmark_"+x,addressModelList.get(i).getLandmark());
                                addresses.put("name_"+x,addressModelList.get(i).getName());
                                addresses.put("mobile_no_"+x,addressModelList.get(i).getName());
                                addresses.put("alternativeMobileNo_"+x,addressModelList.get(i).getAlternativeMobileNo());
                                addresses.put("state_"+x,addressModelList.get(i).getState());
                                if (addressModelList.get(position).getSelected()){
                                    if (position - 1 >= 0){
                                        if (x==position){
                                            addresses.put("selected_"+x,true);
                                            selected = x;
                                        }else {
                                            addresses.put("selected_"+x,addressModelList.get(i).getSelected());
                                        }
                                    }else {
                                        if (x==1){
                                            addresses.put("selected_"+x,true);
                                            selected = x;
                                        }else {
                                            addresses.put("selected_"+x,addressModelList.get(i).getSelected());
                                        }
                                    }
                                }else {
                                    addresses.put("selected_"+x,addressModelList.get(i).getSelected());
                                    if (addressModelList.get(i).getSelected()){
                                        selected=x;
                                    }
                                }
                            }
                        }
                        addresses.put("list_size",x);
                        final int finalSelected = selected;
                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                .document("MY_ADDRESSES").set(addresses).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    DBqueries.addressModelList.remove(position);
                                    if (finalSelected!= -1) {
                                        DBqueries.selectedAddress = finalSelected - 1;
                                        DBqueries.addressModelList.get(finalSelected - 1).setSelected(true);
                                    }
                                    notifyDataSetChanged();
                                }else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });
                        refreesh = false;
                    }
                });

                icon.setImageResource(R.drawable.vertical_date);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manage_address_container.setVisibility(View.VISIBLE);
                        if (refreesh){
                            refreshItem(preSelectedPosition,preSelectedPosition);
                        }else {
                            refreesh = true;
                        }
                        preSelectedPosition = position;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manage_address_container.setVisibility(View.GONE);
                        refreshItem(preSelectedPosition,preSelectedPosition);
                        preSelectedPosition = -1;
                    }
                });
            }
        }
    }
}
