package com.reynagagroup.ryelloshopping;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reynagagroup.ryelloshopping.fragment.SignInFragment;
import com.reynagagroup.ryelloshopping.fragment.SignUpFragment;
import com.reynagagroup.ryelloshopping.ui.HomeFragment;
import com.reynagagroup.ryelloshopping.ui.MyAcountFragment;
import com.reynagagroup.ryelloshopping.ui.MyCartFragment;
import com.reynagagroup.ryelloshopping.ui.MyOrdersFragment;
import com.reynagagroup.ryelloshopping.ui.MyRewardsFragment;
import com.reynagagroup.ryelloshopping.ui.MyWishlistFragment;

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
import android.widget.Toast;


import static com.reynagagroup.ryelloshopping.RegisterActivity.setSignUpFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout frameLayout;

    private  static final  int HOME_FRAGMENT = 0;
    private  static final  int CART_FRAGMENT = 1;
    private  static final  int ORDER_FRAGMENT = 2;
    private  static final  int WISHLIST_FRAGMENT = 3;
    private  static final  int REWARD_FRAGMENT = 4;
    private  static final  int MYACCOUNT_FRAGMENT = 5;
    public static Boolean showCart =false;

    private Dialog signInDialog;

    private Window window;

    private  int currentFragment = -1;
    private ImageView actionBarLogo;

    private Toolbar toolbar;

    private Dialog loadingDialog;

   private FirebaseUser currentUser;


    public static DrawerLayout drawer;


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
            drawer.setDrawerLockMode(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            gotoFragment("My Cart", new MyCartFragment(), -2);
        } else {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.getMenu().getItem(0).setChecked(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            setFragment(new HomeFragment(), HOME_FRAGMENT);
        }






        //NavController navController = Navigation.findNavController(this , R.id.nav_host_fragment);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (currentUser !=null) {
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
                        gotoFragment("My Chart", new MyCartFragment(), CART_FRAGMENT);
                    } else if (id == R.id.nav_wishlist) {
                        gotoFragment("My Wishlist", new MyWishlistFragment(), WISHLIST_FRAGMENT);

                    } else if (id == R.id.nav_myaccount) {
                        gotoFragment("My Account", new MyAcountFragment(), MYACCOUNT_FRAGMENT);

                    } else if (id == R.id.nav_about) {

                    } else if (id == R.id.nav_signout) {
                        loadingDialog.show();
                        FirebaseAuth.getInstance().signOut();
                        DBqueries.clearData();
                        drawer.closeDrawer(GravityCompat.START);
                        Toast.makeText(MainActivity.this,"Logged Out successfully!",Toast.LENGTH_SHORT).show();
                        Intent mainActivityIntent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(mainActivityIntent);
                        finish();
                    }
                    drawer.closeDrawer(GravityCompat.START);
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

        if(fragmentNo ==CART_FRAGMENT) {
            navigationView.getMenu().getItem(3).setChecked(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser ==null){
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(false);
        }else {
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == HOME_FRAGMENT){
            getMenuInflater().inflate(R.menu.main,menu);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            if (currentFragment==HOME_FRAGMENT){
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                currentFragment = -1;
                super.onBackPressed();

            }else {
                if (showCart){
                    showCart = false;
                    finish();
                }else {
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
            //todo: Search
            return true;
        }else if(id ==R.id.main_notification_icon){
            //todo: Notification
            return true;

        }else if(id ==R.id.main_chart_icon){
            if (currentUser ==null) {
                signInDialog.show();
            }else {
                gotoFragment("My Chart", new MyCartFragment(), 1);
            }
            return true;
        }else  if (id == android.R.id.home){
            if (showCart){
                showCart = false;
                finish();
                return true;
            }
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
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(),fragment);
            fragmentTransaction.commit();
        }

    }
}
