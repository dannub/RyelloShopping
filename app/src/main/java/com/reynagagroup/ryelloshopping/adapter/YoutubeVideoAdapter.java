package com.reynagagroup.ryelloshopping.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.reynagagroup.ryelloshopping.R;
import com.reynagagroup.ryelloshopping.fragment.ui.VideoFragment;
import com.reynagagroup.ryelloshopping.model.YoutubeVideoModel;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YoutubeVideoAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final int VIEW_TYPE_NORMAL = 1;
    private List<YoutubeVideoModel> mYoutubeVideos;
    DisplayMetrics displayMetrics = new DisplayMetrics();

    public YoutubeVideoAdapter(List<YoutubeVideoModel> youtubeVideos) {
        mYoutubeVideos = youtubeVideos;
    }
    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layout, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            holder.onBind(position);
            }
    @Override
    public int getItemViewType(int position) {
            return VIEW_TYPE_NORMAL;
            }
    @Override
    public int getItemCount() {
            if (mYoutubeVideos != null && mYoutubeVideos.size() > 0) {
            return mYoutubeVideos.size();
            } else {
            return 1;
            }
            }
    public void setItems(List<YoutubeVideoModel> youtubeVideos) {
            mYoutubeVideos = youtubeVideos;
            notifyDataSetChanged();
    }
    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.textViewTitle)
        TextView textWaveTitle;
        @BindView(R.id.btnPlay)
        ImageView playButton;
        @BindView(R.id.imageViewItem)
        ImageView imageViewItems;
        @BindView(R.id.youtube_view)
        YouTubePlayerView youTubePlayerView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            final YoutubeVideoModel mYoutubeVideo = mYoutubeVideos.get(position);
            ((Activity) itemView.getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            if (mYoutubeVideo.getTitle() != null) {
                textWaveTitle.setText(mYoutubeVideo.getTitle());
            }
            if (mYoutubeVideo.getImageUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(mYoutubeVideo.getImageUrl()).
                        apply(new RequestOptions().override(width - 36, 200))
                        .into(imageViewItems);
            }
            imageViewItems.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.VISIBLE);
            youTubePlayerView.setVisibility(View.GONE);
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    imageViewItems.setVisibility(View.GONE);
                    youTubePlayerView.setVisibility(View.VISIBLE);
                    playButton.setVisibility(View.GONE);

                    youTubePlayerView.release();


                    youTubePlayerView.setEnableAutomaticInitialization(false);


                    if (VideoFragment.youTubePlayerku!=null) {
                        Log.i("akhir","hhjhjk");
                        VideoFragment.video = mYoutubeVideo.getVideoId();

                        try {




                            VideoFragment.youTubePlayerku.pause();
                            VideoFragment.youTubePlayerku.cueVideo(VideoFragment.video, 0);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        Log.i("awal","hhjhjk");
                        VideoFragment.video = mYoutubeVideo.getVideoId();


                        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {


                            @Override
                            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                VideoFragment.youTubePlayerku = youTubePlayer;

                                try {
                                    VideoFragment.youTubePlayerku.cueVideo(VideoFragment.video, 0); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo

                                } catch (Exception e) {
                                    Toast.makeText(itemView.getContext(), "Somrthing Went Wrong with this video ", Toast.LENGTH_SHORT).show();

                                }


                            }

                        });

                    }


                }


            });
        }

    }
}
