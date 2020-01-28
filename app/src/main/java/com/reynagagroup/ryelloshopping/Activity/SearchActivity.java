package com.reynagagroup.ryelloshopping.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.WishlistAdapter;
import com.reynagagroup.ryelloshopping.model.WishlistModel;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private androidx.appcompat.widget.SearchView searchView;
    private TextView textView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_view);
        textView = findViewById(R.id.textview);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        final List<WishlistModel> wishlistModelList = new ArrayList<>();
        final List<String> ids = new ArrayList<>();

        final Adapter adapter = new Adapter(wishlistModelList,false);
        adapter.setFromSearch(true);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                wishlistModelList.clear();
                ids.clear();


                final String [] tags = query.toLowerCase().split(" ");
                for (final String tag : tags){
                    tag.trim();
                    FirebaseFirestore.getInstance().collection("PRODUCTS").whereArrayContains("tags",tag)
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                                    WishlistModel wishlistModel;
                                    if ((Long)documentSnapshot.get("offers_applied")>0) {
                                        wishlistModel = new WishlistModel(
                                                documentSnapshot.getId(), documentSnapshot.get("product_image_1").toString()
                                                , documentSnapshot.get("product_title").toString()
                                                , (long) documentSnapshot.get("free_coupon")
                                                , documentSnapshot.get("average_ratting").toString()
                                                , (long) documentSnapshot.get("total_ratings")
                                                , documentSnapshot.get("cutted_price").toString()
                                                , documentSnapshot.get("product_price").toString()
                                                , (Boolean) documentSnapshot.get("COD")
                                                , (Boolean) documentSnapshot.get("in_stock")
                                                ,(Long)documentSnapshot.get("offers_applied")
                                                , documentSnapshot.get("satuan").toString()
                                        );
                                    }else {
                                        wishlistModel = new WishlistModel(
                                                documentSnapshot.getId(), documentSnapshot.get("product_image_1").toString()
                                                , documentSnapshot.get("product_title").toString()
                                                , (long) documentSnapshot.get("free_coupon")
                                                , documentSnapshot.get("average_ratting").toString()
                                                , (long) documentSnapshot.get("total_ratings")
                                                , documentSnapshot.get("product_price").toString()
                                                , ""
                                                , (Boolean) documentSnapshot.get("COD")
                                                , (Boolean) documentSnapshot.get("in_stock")
                                                ,(Long)documentSnapshot.get("offers_applied")
                                                , documentSnapshot.get("satuan").toString()
                                        );
                                    }

                                    wishlistModel.setTags((ArrayList<String>)documentSnapshot.get("tags"));

                                    if (!ids.contains(wishlistModel.getProductID())){
                                        wishlistModelList.add(wishlistModel);
                                        ids.add(wishlistModel.getProductID());
                                    }

                                }
                                if (tag.equals(tags[tags.length-1])){
                                    if (wishlistModelList.size()==0){
                                        textView.setVisibility(View.VISIBLE);
                                        recyclerView.setVisibility(View.GONE);
                                    }else {
                                        textView.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapter.getFilter().filter(query);
                                    }
                                }
                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(SearchActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    class Adapter extends WishlistAdapter implements Filterable{



        private List<WishlistModel> originalList;

        public Adapter(List<WishlistModel> wishlistModelList, Boolean wishlist) {
            super(wishlistModelList, wishlist);
            originalList = wishlistModelList;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults results = new FilterResults();
                    List<WishlistModel> filteredList = new ArrayList<>();

                    final String [] tags = constraint.toString().toLowerCase().split(" ");

                    for (WishlistModel model : originalList){
                        ArrayList<String> presentTags = new ArrayList<>();
                        for (String tag : tags){
                            if (model.getTags().contains(tag)){
                                presentTags.add(tag);
                            }
                        }
                        model.setTags(presentTags);
                    }

                    for (int i = tags.length;i>0;i--){
                        for (WishlistModel model:originalList){
                           if (model.getTags().size()==i){
                                filteredList.add(model);
                           }
                        }
                    }

                    results.values = filteredList;
                    results.count = filteredList.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (results.count>0){
                        setWishlistModelList((List<WishlistModel>) results.values);
                    }
                    notifyDataSetChanged();
                }
            };
        }
    }
}
