package com.reynagagroup.ryelloshopping.adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.reynagagroup.ryelloshopping.Activity.ProductDetailActivity;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.Activity.ViewAllActivity;
import com.reynagagroup.ryelloshopping.model.HomePageModel;
import com.reynagagroup.ryelloshopping.model.HorizontalProductScrollModel;
import com.reynagagroup.ryelloshopping.model.SliderModel;
import com.reynagagroup.ryelloshopping.model.WishlistModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {


    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private int lastPosition = -1;


    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.BANNER_SLIDER;

            case 1:
                return HomePageModel.STRIP_ADD_BANNER;
            case 2:
                return HomePageModel.HORISONTAL_PRODUCT_VIEW;
            case 3:
                return HomePageModel.GRID_PRODUCT_VIEW;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case HomePageModel.BANNER_SLIDER:
                View bannerSliderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sliding_ad_layout, viewGroup, false);
                return new BannerSliderViewHolder(bannerSliderView);
            case HomePageModel.STRIP_ADD_BANNER:
                View stripAdView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.strip_ad_layout, viewGroup, false);
                return new stripAdBannerSliderViewHolder(stripAdView);
            case HomePageModel.HORISONTAL_PRODUCT_VIEW:
                View horisontalProductView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_layout, viewGroup, false);
                return new HorisontalProductViewHolder(horisontalProductView);
            case HomePageModel.GRID_PRODUCT_VIEW:
                View gridProductView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_product_layout, viewGroup, false);
                return new GridProductViewholder(gridProductView);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.BANNER_SLIDER:
                List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((BannerSliderViewHolder) viewHolder).setBannerSliderViewPager(sliderModelList);
                break;
            case HomePageModel.STRIP_ADD_BANNER:
                 String resource = homePageModelList.get(position).getResource();

                String color = homePageModelList.get(position).getBackgroundColor();
                ((stripAdBannerSliderViewHolder)viewHolder).setStripAd(resource,color);
                break;
            case HomePageModel.HORISONTAL_PRODUCT_VIEW:
                String layoutcolor = homePageModelList.get(position).getBackgroundColor();
                String horisontalLayouttitle = homePageModelList.get(position).getTitle();
                List<WishlistModel> viewAllProductList = homePageModelList.get(position).getViewAllProductList();
                List<HorizontalProductScrollModel>horizontalProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                ((HorisontalProductViewHolder)viewHolder).setHorizontalProductLayout(horizontalProductScrollModelList,horisontalLayouttitle,layoutcolor,viewAllProductList);
                break;
            case HomePageModel.GRID_PRODUCT_VIEW:
                String gridLayoutTitle = homePageModelList.get(position).getTitle();
                String Gridlayoutcolor = homePageModelList.get(position).getBackgroundColor();
                List<HorizontalProductScrollModel>gridProductScrollModelList = homePageModelList.get(position).getHorizontalProductScrollModelList();
                ((GridProductViewholder)viewHolder).setGridProductLayout(gridProductScrollModelList,gridLayoutTitle,Gridlayoutcolor);
                break;
            default:
                return;

        }

        if (lastPosition <position) {
            Animation animation = AnimationUtils.loadAnimation(viewHolder.itemView.getContext(), R.anim.fade_in);
            viewHolder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {

        private ViewPager bannerSliderViewPager;
        private int currentPage;
        private Timer timer;
        final private long DELAY_TIME = 3000;
        final private long PERIOD_TIME = 3000;
        private List<SliderModel> arrangedList;


        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerSliderViewPager = itemView.findViewById(R.id.bannerslider_viewpager);

        }

        private void setBannerSliderViewPager(final List<SliderModel> sliderModelList) {

            currentPage = 0;
            if (timer!=null){
                timer.cancel();
            }
            arrangedList = new ArrayList<>();
            for (int x = 0;x<sliderModelList.size();x++){
                arrangedList.add(x,sliderModelList.get(x));
            }

            SliderAdapter sliderAdapter = new SliderAdapter(sliderModelList);
            bannerSliderViewPager.setAdapter(sliderAdapter);
            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);

            //bannerSliderViewPager.setCurrentItem(currentPage);

            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        //pageLooper(sliderModelList);
                    }
                }
            };
            bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);

            startbannerSlideShow(sliderModelList);

            bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //pageLooper(sliderModelList);
                    stopbannerSlideShow();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startbannerSlideShow(sliderModelList);
                    }
                    return false;
                }
            });
        }

        private void pageLooper(List<SliderModel> sliderModelList) {
            if (currentPage == sliderModelList.size() - 2) {
                currentPage = 2;
                bannerSliderViewPager.setCurrentItem(currentPage, false);
            }
            if (currentPage == 1) {
                currentPage = sliderModelList.size() - 3;
                bannerSliderViewPager.setCurrentItem(currentPage, false);
            }
        }

        private void startbannerSlideShow(final List<SliderModel> sliderModelList) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= sliderModelList.size()) {
                        currentPage = 1;
                    }
                    bannerSliderViewPager.setCurrentItem(currentPage++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DELAY_TIME, PERIOD_TIME);
        }

        private void stopbannerSlideShow() {
            timer.cancel();
        }
    }

    public class stripAdBannerSliderViewHolder extends RecyclerView.ViewHolder{

        private ImageView stripAdImage;
        private ConstraintLayout stripAdContainer;

        public stripAdBannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            stripAdImage = itemView.findViewById(R.id.strip_ad_image);
            stripAdContainer = itemView.findViewById(R.id.strip_ad_container);

        }
        private void setStripAd(String resource,String color){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.load)).into(stripAdImage);
            stripAdContainer.setBackgroundColor(Color.parseColor(color));
        }

    }

    public class HorisontalProductViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout constraintLayout;
        private TextView horizontalLayoutTitle;
        private Button horizontalLayoutViewAllBtn;
        private RecyclerView horizontalRecycleView;


        public HorisontalProductViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.container);
            horizontalLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalLayoutViewAllBtn = itemView.findViewById(R.id.horizontal_scroll_layout_button);
            horizontalRecycleView = itemView.findViewById(R.id.horizontal_scroll_layout_recycleview);
            horizontalRecycleView.setRecycledViewPool(recycledViewPool);

        }

        private  void setHorizontalProductLayout(List<HorizontalProductScrollModel> horizontalProductScrollModelList, final String title, String color, final List<WishlistModel> viewAllproductList){

            constraintLayout.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
            horizontalLayoutTitle.setText(title);
           if(horizontalProductScrollModelList.size()>8){
               horizontalLayoutViewAllBtn.setVisibility(View.VISIBLE);
               horizontalLayoutViewAllBtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       ViewAllActivity.wishlistModelList = viewAllproductList;
                       Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                       viewAllIntent.putExtra("layout_code",0);
                       viewAllIntent.putExtra("title",title);
                       itemView.getContext().startActivity(viewAllIntent);
                   }
               });

           }else {
               horizontalLayoutViewAllBtn.setVisibility(View.INVISIBLE);
           }
            HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalRecycleView.setLayoutManager(linearLayoutManager);
            horizontalRecycleView.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();

        }
    }

    public  class GridProductViewholder extends RecyclerView.ViewHolder{

        private ConstraintLayout container;
        private TextView gridLayoutTitle;
        private Button gridLayoutViewAllBtn;
        private GridLayout gridProductLayout;

        public GridProductViewholder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
             gridLayoutTitle = itemView.findViewById(R.id.grid_product_layout_title);
             gridLayoutViewAllBtn = itemView.findViewById(R.id.grid_product_viewall_layout_btn);
            gridProductLayout = itemView.findViewById(R.id.grid_layout);
        }

        private  void  setGridProductLayout(final List <HorizontalProductScrollModel>horizontalProductScrollModelList, final String title, String color){
            gridLayoutTitle.setText(title);

            container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));


            for (int x = 0;x<6;x++){

                ImageView productImage = gridProductLayout.getChildAt(x).findViewById(R.id.hs_product_image);
                TextView productTitle = gridProductLayout.getChildAt(x).findViewById(R.id.hs_product_title);
                TextView productDescription = gridProductLayout.getChildAt(x).findViewById(R.id.hs_product_description);
                TextView productPrice = gridProductLayout.getChildAt(x).findViewById(R.id.hs_product_price);


                Glide.with(itemView.getContext()).load(horizontalProductScrollModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.load)).into(productImage);
                productTitle.setText(horizontalProductScrollModelList.get(x).getProductTitle());
                productDescription.setText(horizontalProductScrollModelList.get(x).getProductDescription());
                if(!horizontalProductScrollModelList.get(x).getProducPrice().equals("")) {
                    productPrice.setText("Rp." + horizontalProductScrollModelList.get(x).getProducPrice() + "/-");
                }else {
                    productPrice.setText("");
                }

                gridProductLayout.getChildAt(x).setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorAccent));

                if (!title.equals("")) {
                    final int finalX = x;
                    gridProductLayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent productDetailsIntent = new Intent(itemView.getContext(), ProductDetailActivity.class);
                            productDetailsIntent.putExtra("productID",horizontalProductScrollModelList.get(finalX).getProductID());
                            itemView.getContext().startActivity(productDetailsIntent);
                        }
                    });
                }
            }

            if (!title.equals("")) {
                gridLayoutViewAllBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.horizontalProductScrollModelList = horizontalProductScrollModelList;
                        Intent viewAllIntent = new Intent(itemView.getContext(), ViewAllActivity.class);
                        viewAllIntent.putExtra("layout_code", 1);
                        viewAllIntent.putExtra("title", title);
                        itemView.getContext().startActivity(viewAllIntent);
                    }
                });
            }
        }
    }
}
