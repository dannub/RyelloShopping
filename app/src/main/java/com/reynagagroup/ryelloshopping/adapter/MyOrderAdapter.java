package com.reynagagroup.ryelloshopping.adapter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.reynagagroup.ryelloshopping.Activity.OrderDetailsActivity;
import com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.fragment.ui.MyOrdersFragment;
import com.reynagagroup.ryelloshopping.model.MyOrderItemModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity.rateNowContainer;
import static com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity.setRatting;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.Viewholder> {

    private List<MyOrderItemModel> myOrderItemModelList;



    private Dialog loadingDialog;

    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList,Dialog loadingDialog) {
        this.myOrderItemModelList = myOrderItemModelList;
        this.loadingDialog = loadingDialog;
    }

    @NonNull
    @Override
    public MyOrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_order_item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.Viewholder viewholder, int position) {
        String resource = myOrderItemModelList.get(position).getProductImage();
        int ratting = myOrderItemModelList.get(position).getRatting();
        String title = myOrderItemModelList.get(position).getProductTitle();
        String productID = myOrderItemModelList.get(position).getProductID();
       boolean ordered = myOrderItemModelList.get(position).isOrdered();
       boolean packed= myOrderItemModelList.get(position).isPacked();
       boolean shipped= myOrderItemModelList.get(position).isShipped();
       boolean delivered= myOrderItemModelList.get(position).isDelivered();
       boolean canceled= myOrderItemModelList.get(position).isCanceled();

       Date ordered_date=myOrderItemModelList.get(position).getOrdered_date();
       Date packed_date=myOrderItemModelList.get(position).getPacked_date();
        Date shipped_date=myOrderItemModelList.get(position).getShipped_date();
        Date delivered_date=myOrderItemModelList.get(position).getDelivered_date();
        Date canceled_date=myOrderItemModelList.get(position).getCanceled_date();

        Date tgl_pesan=myOrderItemModelList.get(position).getTgl_pesan();


        String ket_kirim=myOrderItemModelList.get(position).getKet_kirim();
        String metode_kirim=myOrderItemModelList.get(position).getMetode_kirim();

        viewholder.setData(resource,title,productID,ratting,ordered,packed,shipped,delivered,canceled,ordered_date,packed_date,shipped_date,delivered_date,canceled_date,tgl_pesan,ket_kirim,metode_kirim,position);

    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private ImageView orderIndicator;
        private TextView productTitle;
        private TextView deliveryStatus;
        private LinearLayout rateNowContainer;
        private int initialRating = -1;


        public Viewholder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            orderIndicator = itemView.findViewById(R.id.order_status_indicator);
            deliveryStatus = itemView.findViewById(R.id.order_delivered_day);
            rateNowContainer = itemView.findViewById(R.id.rate_now_container);


        }

        public void setData(String resource, String title, final String productID, final int ratting, boolean ordered, boolean packed, boolean shipped, boolean delivered, boolean canceled, Date ordered_date, Date packed_date, Date shipped_date, Date delivered_date, Date canceled_date, Date tgl_Pesan, String ket_kirim, String metode_kirim, final int position) {

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.load)).into(productImage);
            productTitle.setText(title);






            if(canceled) {
                deliveryStatus.setText("Cancel order \n"+changeDate(canceled_date));
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorAccent4)));
            }else {
                if (delivered){
                    orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.delivered)));
                    deliveryStatus.setText("Pesanan telah sampai \n"+changeDate(delivered_date));

                }else {
                    if (shipped){
                        orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.shipping)));
                        deliveryStatus.setText("Pesanan sedang dikirim \n"+changeDate(shipped_date));
                    }else {
                        if (packed){
                            orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.packed)));
                            deliveryStatus.setText("Pesanan telah dikemas \n"+changeDate(packed_date));
                        }else {
                            if (ordered){
                                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.ordered)));
                                deliveryStatus.setText("Telah dikonfirmasi \n" + changeDate(ordered_date));
                            }else {
                                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.pesanan)));
                                deliveryStatus.setText("Telah memesan menunggu konfirmasi \n" + changeDate(tgl_Pesan));
                            }
                        }
                    }
                }

            }



            /////ratting layout

            if (DBqueries.myRatedIds.contains(productID)){
                int index = DBqueries.myRatedIds.indexOf(productID);
                initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index)))-1;
                setRatting(initialRating);
            }


             for (int x = 0; x <rateNowContainer.getChildCount();x++){
                final int starPosition = x;
                rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        for (int x = 0; x <rateNowContainer.getChildCount();x++){
                            rateNowContainer.getChildAt(x).setEnabled(false);
                        }


                        setRatting(starPosition);
                        if (starPosition!=initialRating) {
                            loadingDialog.show();
                            final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS")
                                    .document(productID);
                            FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {

                                @Nullable
                                @Override
                                public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                                    DocumentSnapshot documentSnapshot = transaction.get(documentReference);

                                    if (DBqueries.myRatedIds.contains(productID))  {
                                        Long increase = documentSnapshot.getLong(starPosition +1 + "_star") + 1;
                                        Log.i("starpoin", String.valueOf(starPosition+1));
                                        Log.i("increase", String.valueOf(increase));

                                        Long decrease = documentSnapshot.getLong(initialRating+1 + "_star") - 1;
                                        Log.i("initial", String.valueOf(initialRating+1));
                                        Log.i("decrease", String.valueOf(decrease));
                                        Long total = documentSnapshot.getLong("total_ratings");



                                        Double totalStars = Double.valueOf(0);
                                        for (int x = 1; x < 6; x++) {
                                            if ((x != starPosition+1 ) && (x != initialRating+1  )) {
                                                totalStars = totalStars + (documentSnapshot.getLong(x + "_star") * x);
                                            }
                                        }



                                        totalStars = totalStars + (increase * (starPosition +1 ));

                                        totalStars = totalStars + (decrease * (initialRating +1 ));


                                        String rate = String.valueOf(Double.valueOf(totalStars / total)).substring(0, 3);



                                        transaction.update(documentReference, starPosition  + 1 +"_star", increase);
                                        transaction.update(documentReference, initialRating  +1+"_star", decrease);
                                        transaction.update(documentReference, "average_ratting", rate);
                                    } else {
                                        Long increase = documentSnapshot.getLong(starPosition + 1 + "_star") + 1;


                                        Long total = documentSnapshot.getLong("total_ratings");

                                        Double totalStars = Double.valueOf(0);
                                        for (int x = 1; x < 6; x++) {
                                            if ((x != starPosition + 1)) {
                                                totalStars = totalStars + (documentSnapshot.getLong(x + "_star") * x);
                                            }
                                        }


                                        totalStars = totalStars + (increase * (starPosition + 1));
                                        String rate = String.valueOf(Double.valueOf(totalStars / total + 1)).substring(0, 3);

                                        transaction.update(documentReference, "total_ratings", total + 1);
                                        transaction.update(documentReference, "average_ratting", rate);
                                        transaction.update(documentReference, starPosition + 1 + "_star", increase);
                                    }


                                    return null;
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Object>() {
                                @Override
                                public void onSuccess(Object o) {


                                    Map<String, Object> myRating = new HashMap<>();
                                    if (DBqueries.myRatedIds.contains(productID)) {
                                        myRating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) starPosition+ 1 );
                                    } else {
                                        myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                        myRating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                        myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition+ 1 );
                                    }


                                    FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                                            .document("MY_RATINGS").update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                loadingDialog.dismiss();
                                                DBqueries.myOrderItemModelArrayList.get(position).setRatting(starPosition+1);
                                                for (MyOrderItemModel myOrderItemModel : DBqueries.myOrderItemModelArrayList) {
                                                    if (myOrderItemModel.getProductID() == DBqueries.myOrderItemModelArrayList.get(position).getProductID()) {
                                                        myOrderItemModel.setRatting(starPosition+1);

                                                    }
                                                }
                                                if (DBqueries.myRatedIds.contains(productID)) {
                                                    DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID), Long.parseLong(String.valueOf(starPosition+1 )));
                                                } else {
                                                    DBqueries.myRatedIds.add(productID);
                                                    DBqueries.myRating.add(Long.parseLong(String.valueOf(starPosition+1 )));
                                                }
                                                loadingDialog.show();
                                                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {

                                                    }
                                                });
                                                DBqueries.loadOrders(itemView.getContext(),loadingDialog,MyOrdersFragment.layoutManager, MyOrdersFragment.myOrderRecycleView);
                                                for (int x = 0; x <rateNowContainer.getChildCount();x++){
                                                    rateNowContainer.getChildAt(x).setEnabled(true);
                                                }


                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                for (int x = 0; x <rateNowContainer.getChildCount();x++){
                                                    rateNowContainer.getChildAt(x).setEnabled(true);
                                                }
                                                loadingDialog.dismiss();
                                            }
                                        }
                                    });


                                }
                            });
                        }

                    }
                });
            }
            /////ratting layout






            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent orderDetailIntent = new Intent(itemView.getContext(), OrderDetailsActivity.class);

                    orderDetailIntent.putExtra("position",position);
                    itemView.getContext().startActivity(orderDetailIntent);
                }
            });



        }

        private String changeDate(Date date){

            SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
            String day = sdf2.format(date);
            return day;
        }

        private void setRatting(int starPosition) {

                for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                    ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
                    starBtn.setImageTintList(itemView.getContext().getResources().getColorStateList(R.color.colorAccent3));
                    if (x <= starPosition) {
                        starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
                    }

                }

        }





    }


}
