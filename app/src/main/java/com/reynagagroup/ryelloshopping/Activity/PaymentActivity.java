package com.reynagagroup.ryelloshopping.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.adapter.CartPaymentAdapter;
import com.reynagagroup.ryelloshopping.model.CartItemModel;
import com.reynagagroup.ryelloshopping.model.UploadBuktiModel;
import com.reynagagroup.ryelloshopping.ui.MyCartFragment;
import com.reynagagroup.ryelloshopping.util.BitmapUtils;
import com.reynagagroup.ryelloshopping.util.Utility;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import info.hoang8f.widget.FButton;

import static com.reynagagroup.ryelloshopping.Activity.DeliveryActivity.totalAmount;

public class PaymentActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1002;
    private static final int SELECT_FILE = 1000;
    private ImageView ivImage,bukti;
    public  static List<CartItemModel> cartItemModelList;
    private String userChoosenTask;

    private Uri image_selected_uri=null;

    private boolean isbukti;
    public static boolean isfree;

    private EditText tgl;
    private EditText an;
    private FButton Save;
    private ProgressBar progressBar;
    private String [] bankList;
    private  int bank;
    private String bank_str;
    private String selectedBank;
    private DatePickerDialog picker;
    private FirebaseUser currentUser;
    private StorageReference storageReference;
    private Dialog loadingDialog;

    private ConstraintLayout orderConfirmationLayout;
    private ConstraintLayout notaPaymentLayout;
    private ConstraintLayout input;

    private ImageButton continueShoppingBtn;
    private TextView orderID;
    private Button downloadNota;

    private TextView idPayment;
    private TextView totalBayar;
    private TextView waktuPesan;
    private TextView alamat;
    private TextView bank_tv;
    private TextView status;

    public static CartPaymentAdapter cartPaymentAdapter;
    public static RecyclerView recyclerViewItem;


    private boolean successResponse = false;
    public static boolean fromCart;

    private String dirpath;
    private String now;
    private static final int REQUEST = 112;

    public  static LinearLayoutManager linearLayoutManager;

    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //loading dialog
        loadingDialog = new Dialog(PaymentActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(PaymentActivity.this.getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        //loading dialog

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Upload Bukti");



        ivImage = (ImageView)findViewById(R.id.image_add_payment);
        bukti = (ImageView)findViewById(R.id.imageView12);
        tgl = findViewById(R.id.tgl);
        an = findViewById(R.id.a_n);
        progressBar = findViewById(R.id.progress_bar);
        Save = findViewById(R.id.save_btn);
        orderConfirmationLayout = findViewById(R.id.order_confirmation_layout);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);
        downloadNota = findViewById(R.id.download);
        orderID = findViewById(R.id.order_id);
        notaPaymentLayout = findViewById(R.id.nota);
        downloadNota = findViewById(R.id.download);
        recyclerViewItem = findViewById(R.id.item_recycleview);

        idPayment = findViewById(R.id.id);
        totalBayar = findViewById(R.id.totalbayar);
        waktuPesan = findViewById(R.id.waktupesan);
        alamat = findViewById(R.id.alamat);
        bank_tv = findViewById(R.id.bank);
        status = findViewById(R.id.status);

        input = findViewById(R.id.input);

        Save.setButtonColor(getResources().getColor(R.color.colorPrimary));
        Save.setTextColor(getResources().getColor(R.color.colorAccent));




         recyclerViewItem.setAdapter(cartPaymentAdapter);

        linearLayoutManager = new LinearLayoutManager(PaymentActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewItem.setLayoutManager(linearLayoutManager);

        cartPaymentAdapter.notifyDataSetChanged();




        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                bank = 1;
            } else {
                bank= extras.getInt("bank");
            }
        } else {
            bank= (int) savedInstanceState.getSerializable("bank");
        }

        if (bank==1){
           bank_str = "BRI";
        }else if (bank == 2){
            bank_str = "BNI";
        }else if(bank==3){
            bank_str = "BCA";
        }else {
            bank_str = "Mandiri";
        }


        isbukti = false;

        storageReference = FirebaseStorage.getInstance().getReference("bukti");
        firebaseFirestore = FirebaseFirestore.getInstance();


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                uploadFile();
            }
        });




        tgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(PaymentActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tgl.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        tgl.setFocusable(false);
        tgl.setFocusableInTouchMode(false);










        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile() {

        if (image_selected_uri!=null){
            final String namefile =System.currentTimeMillis()+ "." + getFileExtension(image_selected_uri);
            StorageReference fileReference = storageReference.child(namefile);

            fileReference.putFile(image_selected_uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.child(namefile).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {


                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setProgress(0);
                                        }
                                    },500);

                                    long dateInMillis = System.currentTimeMillis();
                                    String format = "dd-MM-yyyy HH:mm:ss";
                                    final SimpleDateFormat sdf = new SimpleDateFormat(format);

                                    final String dateString = sdf.format(new Date(dateInMillis));


                                    Date date= new Date();

                                    Toast.makeText(PaymentActivity.this,"Upload successful",Toast.LENGTH_LONG).show();
                                    final UploadBuktiModel uploadBuktiModel = new UploadBuktiModel(
                                            an.getText().toString().trim()
                                            ,currentUser.getUid()
                                            ,MainActivity.fullname.getText().toString()
                                            , uri.toString()
                                            ,bank_str,tgl.getText().toString().trim()
                                            , DBqueries.addressModelList.get(DBqueries.selectedAddress).getFullname()
                                            ,DBqueries.addressModelList.get(DBqueries.selectedAddress).getAddress()
                                            ,DBqueries.addressModelList.get(DBqueries.selectedAddress).getPhone()
                                            ,currentUser.getEmail()
                                            ,DBqueries.addressModelList.get(DBqueries.selectedAddress).getPincode()
                                            , date
                                            ,cartItemModelList.get(cartItemModelList.size()-1).getTotalAmount()
                                            ,cartItemModelList.get(cartItemModelList.size()-1).getTotalItems()
                                            ,cartItemModelList.get(cartItemModelList.size()-1).getTotalItemsPrice()
                                            ,cartItemModelList.get(cartItemModelList.size()-1).getSavedAmount()
                                            ,cartItemModelList.get(cartItemModelList.size()-1).getDeliveryPrice()
                                            ,false,false,false,false,false
                                            ,date,date,date,date,date
                                            ,"",""
                                            ,isfree
                                            ,false
                                    );


                                    firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_NOTA")
                                            .add(uploadBuktiModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()) {




                                                final String key = task.getResult().getId();

                                                idPayment.setText("Kode.Pesanan: " + key);
                                                totalBayar.setText("Rp."+cartItemModelList.get(cartItemModelList.size()-1).getTotalAmount()+"/-");
                                                waktuPesan.setText(dateString);
                                                status.setText("Belum Dikonfirmasi");
                                                alamat.setText(DBqueries.addressModelList.get(DBqueries.selectedAddress).getFullname() + " " + DBqueries.addressModelList.get(DBqueries.selectedAddress).getAddress()+" "+DBqueries.addressModelList.get(DBqueries.selectedAddress).getPincode());
                                                bank_tv.setText("Bank " + bank_str);


                                                successResponse = true;




                                                if (MainActivity.mainActivity != null) {
                                                    MainActivity.mainActivity.finish();
                                                    MainActivity.mainActivity = null;
                                                }
                                                if (!MainActivity.showCart) {
                                                    if (ProductDetailActivity.productDetailsActivity != null) {
                                                        ProductDetailActivity.productDetailsActivity.finish();
                                                        ProductDetailActivity.productDetailsActivity = null;
                                                    }
                                                }
                                                if (PaymentInfoActivity.paymentInfoActivity != null) {
                                                    PaymentInfoActivity.paymentInfoActivity.finish();
                                                    PaymentInfoActivity.paymentInfoActivity = null;
                                                }
                                                if (DeliveryActivity.deliveryActivity != null) {
                                                    DeliveryActivity.deliveryActivity.finish();
                                                    DeliveryActivity.deliveryActivity = null;
                                                }




                                                if (fromCart) {
                                                    loadingDialog.show();
                                                    Map<String, Object> updateCartlist = new HashMap<>();
                                                    long cartListSize = 0;
                                                    final List<Integer> indexList = new ArrayList<>();
                                                    for (int x = 0; x < DBqueries.cartlist.size()-1; x++) {
                                                        if (!DBqueries.cartItemModelList.get(x).getInStock()) {
                                                            updateCartlist.put("product_ID_" + cartListSize, DBqueries.cartItemModelList.get(x).getProductID());
                                                            cartListSize++;
                                                        } else {
                                                            indexList.add(x);
                                                        }
                                                    }
                                                    updateCartlist.put("list_size", cartListSize);

                                                    firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA")
                                                            .document("MY_CART")
                                                            .set(updateCartlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {


                                                                for (int x = indexList.size() - 1; x >= 0; x--) {
                                                                    Log.i("indexlist", Integer.toString(indexList.get(x).intValue()));
                                                                    DBqueries.cartlist.remove(indexList.get(x).intValue());
                                                                    DBqueries.cartItemModelList.remove(indexList.get(x).intValue());
                                                                    //DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size()-1);
                                                                }

                                                            } else {
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(PaymentActivity.this, error, Toast.LENGTH_SHORT).show();
                                                            }
                                                            loadingDialog.dismiss();
                                                        }
                                                    });

                                                }


                                                orderID.setText("Order ID " + key);
                                                downloadNota.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        layoutToImage();
                                                        try {
                                                            imageToPDF();

                                                        } catch (FileNotFoundException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                                continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {


                                                        if (MyCartFragment.mycartfragment != null) {
                                                            if (!MainActivity.showCart) {
                                                                MyCartFragment.mycartfragment.getActivity().finish();
                                                                Intent mainIntent = new Intent(PaymentActivity.this, MainActivity.class);
                                                                startActivity(mainIntent);
                                                            }
                                                            MainActivity.showCart = false;
                                                            MyCartFragment.mycartfragment = null;


                                                        }
                                                        finish();

                                                    }
                                                });

                                                for (int i = 0; i < cartItemModelList.size()-1; i++) {
                                                    cartItemModelList.get(i).setTotalItems(cartItemModelList.get(cartItemModelList.size()-1).getTotalItems());
                                                    cartItemModelList.get(i).setTotalItemsPrice(cartItemModelList.get(cartItemModelList.size()-1).getTotalItemsPrice());
                                                    cartItemModelList.get(i).setDeliveryPrice(cartItemModelList.get(cartItemModelList.size()-1).getDeliveryPrice());
                                                    cartItemModelList.get(i).setTotalAmount(cartItemModelList.get(cartItemModelList.size()-1).getTotalAmount());
                                                    cartItemModelList.get(i).setSavedAmount(cartItemModelList.get(cartItemModelList.size()-1).getSavedAmount());

                                                    Map<String ,Object> updateReward = new HashMap<>();
                                                    updateReward.put("already_used",true);

                                                    firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_REWARDS")
                                                            .document(cartItemModelList.get(i).getSelectedCouponId()).update(updateReward);

                                                    firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_NOTA").document(key).collection("ITEM").add(cartItemModelList.get(i));

                                                }
                                                Save.setEnabled(true);
                                                loadingDialog.dismiss();
                                                 notaPaymentLayout.setVisibility(View.VISIBLE);
                                                orderConfirmationLayout.setVisibility(View.VISIBLE);

                                                input.setVisibility(View.INVISIBLE);

                                                InputMethodManager imm = (InputMethodManager)
                                                        getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.showSoftInput(getCurrentFocus(), InputMethodManager.HIDE_IMPLICIT_ONLY);




                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(PaymentActivity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                        }});
                                }
                            });


                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PaymentActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                            Save.setEnabled(false);


                        }
                    });
        }else {
            Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA) {
                Bitmap bitmap = BitmapUtils.getBitmapFromGalerry(this,image_selected_uri,800, 800);
                isbukti = true;
                bukti.setImageBitmap(bitmap);
            }

        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                image_selected_uri= Objects.requireNonNull(data).getData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isbukti = true;
        bukti.setImageBitmap(bm);
    }


    private void openCamera() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted()){

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE,"New Pictures");
                            values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
                            image_selected_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_selected_uri);
                            startActivityForResult(cameraIntent,REQUEST_CAMERA);

                        }else {
                            Toast.makeText(PaymentActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(PaymentActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        openCamera();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
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

    @Override
    public void onBackPressed() {
        if (successResponse){
            if (MyCartFragment.mycartfragment != null) {
                if (!MainActivity.showCart) {
                    MyCartFragment.mycartfragment.getActivity().finish();
                    Intent mainIntent = new Intent(PaymentActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }
                MainActivity.showCart = false;
                MyCartFragment.mycartfragment = null;


            }
            finish();

            return;
        }
        super.onBackPressed();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do here
                    layoutToImage();
                    try {
                        imageToPDF();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(PaymentActivity.this, "The app was not allowed to read your store.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void layoutToImage() {
        // get view group using reference
        // convert view group to bitmap
        notaPaymentLayout.setDrawingCacheEnabled(true);
        notaPaymentLayout.buildDrawingCache();


        Bitmap bm = Bitmap.createBitmap(notaPaymentLayout.getWidth(), notaPaymentLayout.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bm);
        notaPaymentLayout.layout(0, 0, notaPaymentLayout.getMeasuredWidth(), notaPaymentLayout.getMeasuredHeight());
        notaPaymentLayout.draw(c);

        // Bitmap bm = detail.getDrawingCache(true);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(PaymentActivity.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) PaymentActivity.this, PERMISSIONS, REQUEST );
            } else {
                //do here
                now = Long.toString(System.currentTimeMillis());
                File f = new File(android.os.Environment.getExternalStorageDirectory().toString()+"/Download/"+now+"nota.jpg");
                try {

                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                notaPaymentLayout.setDrawingCacheEnabled(false);
            }
        } else {
            //do here
            now = Long.toString(System.currentTimeMillis());
            File f = new File(android.os.Environment.getExternalStorageDirectory().toString()+"/Download/"+now+ "nota.jpg");
            try {

                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            notaPaymentLayout.setDrawingCacheEnabled(false);
        }


    }
    public void imageToPDF() throws FileNotFoundException {
        try {
            Document document = new Document(PageSize.A4);
            dirpath = android.os.Environment.getExternalStorageDirectory().toString();
            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/Download/"+now+"nota.pdf")); //  Change pdf's name.
            document.open();
            Image img = Image.getInstance(dirpath +"/Download/" + now+"nota.jpg");
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / img.getHeight()) * 100;
            img.scalePercent(scaler);
            img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(img);
            document.close();
            Toast.makeText(this, "PDF telah tersimpan di memori internal", Toast.LENGTH_SHORT).show();
           // print.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



}
