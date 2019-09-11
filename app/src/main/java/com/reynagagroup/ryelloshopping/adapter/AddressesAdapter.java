package com.reynagagroup.ryelloshopping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.model.AddressModel;

import java.util.List;

import static com.reynagagroup.ryelloshopping.DeliveryActivity.SELECT_ADDRESS;
import static com.reynagagroup.ryelloshopping.MyAddressesActivity.refreshItem;
import static com.reynagagroup.ryelloshopping.ui.MyAcountFragment.MANAGE_ADDRESS;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.Viewholder> {

    private List<AddressModel> addressModelList;
    private int MODE;
    private int preSelectedPosition;

    public AddressesAdapter(List<AddressModel> addressModelList,int MODE) {
        this.addressModelList = addressModelList;
        this.MODE = MODE;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addresses_item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {
        String name = addressModelList.get(position).getFullname();
        String address = addressModelList.get(position).getAddress();
        String pincode = addressModelList.get(position).getPincode();
        Boolean selected = addressModelList.get(position).getSelected();
        viewholder.setData(name,address,pincode,selected,position);
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

        private  void setData(String username, String userAddress, String userPincode, Boolean selected, final int position){
            fullname.setText(username);
            address.setText(userAddress);
            pincode.setText(userPincode);

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
                        }

                    }
                });

            }else if(MODE == MANAGE_ADDRESS){
                manage_address_container.setVisibility(View.GONE);
                icon.setImageResource(R.drawable.vertical_date);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manage_address_container.setVisibility(View.VISIBLE);
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
