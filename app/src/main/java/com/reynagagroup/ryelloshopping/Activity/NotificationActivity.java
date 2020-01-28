package com.reynagagroup.ryelloshopping.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.NotificationAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.reynagagroup.ryelloshopping.Activity.MainActivity.loadingDialog;
import static com.reynagagroup.ryelloshopping.Activity.MainActivity.showCart;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static NotificationAdapter adapter;
    private boolean runQuery=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.inflateMenu(R.menu.setting);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.delete)
                {

                    loadingDialog.show();
                    loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {

                        }
                    });

                    Map<String, Object> notifdata = new HashMap<>();
                    notifdata.put("list_size", 0);

                   FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                            .document("MY_NOTIFICATIONS").set(notifdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                DBqueries.checkNotifications(NotificationActivity.this,false, null);
                                Toast.makeText(NotificationActivity.this,"Notifikasi telah terhapus", Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(NotificationActivity.this, error, Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                            }
                        }
                            });
                }

                return false;
            }
        });


        recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new NotificationAdapter(DBqueries.notificationModelList);
        recyclerView.setAdapter(adapter);

        Map<String ,Object> readMap = new HashMap<>();
        for (int x=0;x<DBqueries.notificationModelList.size();x++){
            if (!DBqueries.notificationModelList.get(x).isReaded()) {
               runQuery = true;
            }
            readMap.put("Readed_" + x, true);
        }


        if (runQuery) {
            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA")
                    .document("MY_NOTIFICATIONS").update(readMap);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int x=0;x<DBqueries.notificationModelList.size();x++){
            DBqueries.notificationModelList.get(x).setReaded(true);

        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if(id==android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





}
