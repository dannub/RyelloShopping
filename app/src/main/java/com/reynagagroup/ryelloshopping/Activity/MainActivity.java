package com.reynagagroup.ryelloshopping.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.reynagagroup.ryelloshopping.DBqueries;
import com.reynagagroup.ryelloshopping.Interface.IOnBackPressed;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.fragment.SignInFragment;
import com.reynagagroup.ryelloshopping.fragment.SignUpFragment;
import com.reynagagroup.ryelloshopping.fragment.ui.HomeFragment;
import com.reynagagroup.ryelloshopping.fragment.ui.MyAcountFragment;
import com.reynagagroup.ryelloshopping.fragment.ui.MyCartFragment;
import com.reynagagroup.ryelloshopping.fragment.ui.MyOrdersFragment;
import com.reynagagroup.ryelloshopping.fragment.ui.MyRewardsFragment;
import com.reynagagroup.ryelloshopping.fragment.ui.MyWishlistFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import de.hdodenhof.circleimageview.CircleImageView;

import static com.reynagagroup.ryelloshopping.Activity.RegisterActivity.setSignUpFragment;

public class MainActivity extends AppCompatActivity {




    public static int currentFragment= -1;
    private static Window window;
    private static Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout frameLayout;

    public   static final  int HOME_FRAGMENT = 0;
    private  static final  int CART_FRAGMENT = 1;
    private  static final  int ORDER_FRAGMENT = 2;
    private  static final  int WISHLIST_FRAGMENT = 3;
    private  static final  int REWARD_FRAGMENT = 4;
    private  static final  int MYACCOUNT_FRAGMENT = 5;
    public static Boolean showCart;
    public static Activity mainActivity;


    private FirebaseFirestore firebaseFirestore;


    private Dialog signInDialog;

    private ImageView actionBarLogo;


    public static Dialog loadingDialog;

   private FirebaseUser currentUser;

   private AppBarLayout.LayoutParams params;

    //drawer
    public static TextView fullnameText;
    public static TextView email;
   public static CircleImageView imageAcc;
    public static DrawerLayout drawer;
    public static ImageView imageAdd;
    //drawer

    private  int scrollFlags;


    public static TextView badgeCount;

    private  MenuItem menuItem;
    private NavigationView navigationView;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = findViewById(R.id.toolbar);
        actionBarLogo = findViewById(R.id.actionbar_logo);
        setSupportActionBar(toolbar);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        scrollFlags = params.getScrollFlags();

        frameLayout = findViewById(R.id.main_framelayout);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //loading dialog
        loadingDialog = new Dialog(MainActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        //loading dialog




        if (showCart) {
             mainActivity = this;
            drawer.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);
        } else {

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.getMenu().getItem(0).setChecked(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            setFragment(new HomeFragment(), HOME_FRAGMENT);

            invalidateOptionsMenu();




            //NavController navController = Navigation.findNavController(this , R.id.nav_host_fragment);
            navigationView
                    .addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

                            navigationView.removeOnLayoutChangeListener( this );

                            fullnameText = findViewById(R.id.main_fullname);
                            email = findViewById(R.id.main_email);
                            imageAcc = findViewById(R.id.profile_image);
                            imageAdd = findViewById(R.id.image_add_profile);


                            if (currentUser!=null) {



                                loadingDialog.show();
                                loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {

                                    }
                                });

                                firebaseFirestore = FirebaseFirestore.getInstance();

                                firebaseFirestore.collection("USERS")
                                        .document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {

                                            DBqueries.fullname = task.getResult().get("fullname").toString();
                                            DBqueries.email = currentUser.getEmail();
                                            DBqueries.profile = task.getResult().get("profile").toString();


                                            fullnameText.setText(DBqueries.fullname);
                                            email.setText(DBqueries.email);
                                            if (DBqueries.profile.equals("")){
                                                // imageAdd.setVisibility(View.INVISIBLE);
                                            }else {
                                                // imageAdd.setVisibility(View.INVISIBLE);
                                                Glide.with(MainActivity.this).load(DBqueries.profile).apply(new RequestOptions().placeholder(R.drawable.account)).into(imageAcc);
                                            }
                                            loadingDialog.dismiss();

                                        } else {
                                            loadingDialog.dismiss();
                                            String error = task.getException().getMessage();
                                            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });



                            }else {
                                imageAcc.setImageDrawable(getResources().getDrawable(R.drawable.account));
                                fullnameText.setText("Not Sign In");
                                email.setText("");
                            }

                        }
                    });
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                    final DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);





                    menuItem = item;
                    if (currentUser !=null) {

                        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                            @Override
                            public void onDrawerClosed(View drawerView) {
                                super.onDrawerClosed(drawerView);



                                int id = menuItem.getItemId();
                                if (id == R.id.nav_home) {
                                    //todo:Home
                                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                                    actionBarLogo.setVisibility(View.VISIBLE);
                                    invalidateOptionsMenu();
                                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                                } else if (id == R.id.nav_orders) {
                                    gotoFragment("My Orders", new MyOrdersFragment(), ORDER_FRAGMENT);
                                } else if (id == R.id.nav_rewards) {
                                    gotoFragment("My Rewards", new MyRewardsFragment(), REWARD_FRAGMENT);
                                } else if (id == R.id.nav_chart) {
                                    mainActivity = MainActivity.this;
                                    gotoFragment("My Cart", new MyCartFragment(), CART_FRAGMENT);

                                } else if (id == R.id.nav_wishlist) {
                                    gotoFragment("My Wishlist", new MyWishlistFragment(), WISHLIST_FRAGMENT);

                                } else if (id == R.id.nav_myaccount) {
                                    gotoFragment("My Account", new MyAcountFragment(), MYACCOUNT_FRAGMENT);

                                } else if (id == R.id.nav_about) {

                                } else if (id == R.id.nav_signout) {
                                    FirebaseAuth.getInstance().signOut();
                                    DBqueries.clearData();
                                    drawer.closeDrawer(GravityCompat.START);
                                    MainActivity.mainActivity = null;
                                    MainActivity.currentFragment = -1;
                                    Toast.makeText(MainActivity.this,"Logged Out successfully!",Toast.LENGTH_SHORT).show();
                                    Intent mainActivityIntent = new Intent(MainActivity.this, MainActivity.class);
                                    MainActivity.showCart = false;
                                    startActivity(mainActivityIntent);
                                    finish();
                                }
                                drawer.removeDrawerListener(this);
                            }
                        });

                        return true;
                    }else {
                        signInDialog.show();

                        return false;
                    }
                }
            });



            signInDialog = new Dialog(MainActivity.this);
            signInDialog.setContentView(R.layout.sign_in_dialog);
            signInDialog.setCancelable(true);
            signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
            Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);

            final Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);

            dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SignInFragment.disableCloseBtn = false;
                    SignUpFragment.disableCloseBtn = false;
                    signInDialog.dismiss();
                    setSignUpFragment = false;
                    startActivity(registerIntent);
                }
            });
            dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SignInFragment.disableCloseBtn = false;
                    SignUpFragment.disableCloseBtn = false;
                    signInDialog.dismiss();
                    setSignUpFragment = true;
                    startActivity(registerIntent);
                }
            });

        }








//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_orders, R.id.nav_rewards,
//                R.id.nav_chart, R.id.nav_wishlist, R.id.nav_myaccount,
//                R.id.nav_about,R.id.nav_signout)
//                .setDrawerLayout(drawer)
//                .build();
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//
//        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void gotoFragment(String title,Fragment fragment,int fragmentNo) {


        setFragment(fragment,fragmentNo);
        getSupportActionBar().setTitle(title);

        invalidateOptionsMenu();
        actionBarLogo.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        if(fragmentNo ==CART_FRAGMENT || showCart) {
            navigationView.getMenu().getItem(3).setChecked(true);
            params.setScrollFlags(0);
        }else {
            params.setScrollFlags(scrollFlags);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser ==null){
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(false);
        }else {

                FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid()).update("Last seen", FieldValue.serverTimestamp())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    MainActivity.showCart =false;
                                }else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(MainActivity.this,error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(true);

            FirebaseFirestore.getInstance().collection("USERS")
                    .document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {

                        DBqueries.fullname = task.getResult().get("fullname").toString();
                        DBqueries.email = currentUser.getEmail();
                        DBqueries.profile = task.getResult().get("profile").toString();




                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }





        if (currentUser!=null) {
            DBqueries.checkNotifications(MainActivity.this,false, null);
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (currentUser!=null) {
            DBqueries.checkNotifications(MainActivity.this,true, null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == HOME_FRAGMENT){

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getMenuInflater().inflate(R.menu.main,menu);

            MenuItem cartItem = menu.findItem(R.id.main_chart_icon);
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.shop);


            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser ==null) {
                        signInDialog.show();
                    }else {
                        gotoFragment("My Cart", new MyCartFragment(), 1);
                    } }
            });

            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);
            if (currentUser !=null){
                DBqueries.loadCartList(MainActivity.this, new Dialog(MainActivity.this),false,badgeCount,new TextView(MainActivity.this),false,null);

            }

            MenuItem notificationItem = menu.findItem(R.id.main_notification_icon);
            notificationItem.setActionView(R.layout.badge_layout);
            ImageView notifyIcon = notificationItem.getActionView().findViewById(R.id.badge_icon);
            notifyIcon.setImageResource(R.drawable.notification);
            TextView notifyCount = notificationItem.getActionView().findViewById(R.id.badge_count);
            if (currentUser!= null){
                DBqueries.checkNotifications(MainActivity.this,false,notifyCount);
            }

            notificationItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent notificationIntent = new Intent(MainActivity.this,NotificationActivity.class);
                    startActivity(notificationIntent);
                }
            });



        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //DrawerLayout drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);


        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            if (currentFragment==HOME_FRAGMENT){
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                currentFragment = -1;
                super.onBackPressed();

            }else if (currentFragment==CART_FRAGMENT){
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_framelayout);

                if ((fragment instanceof IOnBackPressed) || ((IOnBackPressed) fragment).onBackPressed()) {
                    if (showCart){
                        mainActivity = null;
                        showCart = false;
                        getSupportActionBar().setDisplayShowTitleEnabled(true);
                        currentFragment = -1;
                        finish();
                    }else {

                        actionBarLogo.setVisibility(View.VISIBLE);
                        invalidateOptionsMenu();
                        setFragment(new HomeFragment(), HOME_FRAGMENT);
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        navigationView.getMenu().getItem(0).setChecked(true);
                    }
                }
            } else {
                if (showCart){
                    mainActivity = null;
                    showCart = false;
                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                    currentFragment = -1;
                    finish();
                    super.onBackPressed();
                }

                else {
                    actionBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }

        }



    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.search_icon){
            Intent searchIntent = new Intent(this,SearchActivity.class);
            startActivity(searchIntent);

            return true;
        }else if(id ==R.id.main_notification_icon){
            Intent notificationIntent = new Intent(this,NotificationActivity.class);
            startActivity(notificationIntent);
            return true;

        }else if(id ==R.id.main_chart_icon){
            if (currentUser ==null) {
                signInDialog.show();
            }else {
                mainActivity = MainActivity.this;
                gotoFragment("My Cart", new MyCartFragment(), 1);
            }
            return true;
        }else  if (id == android.R.id.home){
            MyCartFragment.mycartfragment = null;


            if(drawer.isDrawerOpen(GravityCompat.START)){
                drawer.closeDrawer(GravityCompat.START);
            }else {
                if (currentFragment==HOME_FRAGMENT){
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                    currentFragment = -1;
                    super.onBackPressed();

                }else if (currentFragment==CART_FRAGMENT){

                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_framelayout);

                    if ((fragment instanceof IOnBackPressed) || ((IOnBackPressed) fragment).onBackPressed()) {
                        if (showCart){
                            mainActivity = null;
                            showCart = false;
                            MyCartFragment.mycartfragment = null;
                            getSupportActionBar().setDisplayShowTitleEnabled(true);
                            currentFragment = -1;
                            finish();
                        }else {
                            actionBarLogo.setVisibility(View.VISIBLE);
                            invalidateOptionsMenu();
                            setFragment(new HomeFragment(), HOME_FRAGMENT);
                            getSupportActionBar().setDisplayShowTitleEnabled(false);
                            navigationView.getMenu().getItem(0).setChecked(true);
                        }
                    }
                } else {
                    if (showCart){
                        MyCartFragment.mycartfragment = null;
                        mainActivity = null;
                        showCart = false;
                        getSupportActionBar().setDisplayShowTitleEnabled(true);
                        currentFragment = -1;
                        finish();
                        super.onBackPressed();
                    }

                    else {
                        actionBarLogo.setVisibility(View.VISIBLE);
                        invalidateOptionsMenu();
                        setFragment(new HomeFragment(), HOME_FRAGMENT);
                        getSupportActionBar().setDisplayShowTitleEnabled(false);
                        navigationView.getMenu().getItem(0).setChecked(true);
                    }
                }

            }



//          if (showCart){
////                mainActivity = null;
////                showCart = false;
////                finish();
////                return true;
//            }
        }

        return super.onOptionsItemSelected(item);
    }







    private void setFragment(Fragment fragment, int fragmentNo){
        if (fragmentNo!=currentFragment){
            if ( fragmentNo== MYACCOUNT_FRAGMENT){
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }else {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            MainActivity.currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(),fragment);
            fragmentTransaction.commit();
        }

    }


}
