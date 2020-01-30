package com.reynagagroup.ryelloshopping.model;

public class YoutubeVideoModel implements Comparable<YoutubeVideoModel>  {
    private String title;
    private String id;
    private String videoId;
    private String imageUrl;

    public YoutubeVideoModel() {
    }

    public YoutubeVideoModel(String title, String id, String videoId, String imageUrl) {
        this.title = title;
        this.id = id;
        this.videoId = videoId;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getVideoId() {
        return videoId;
    }
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    @Override
    public int compareTo(YoutubeVideoModel o) {
        if (getTitle() == null || o.getTitle() == null)
            return 0;
        return getTitle().compareTo(o.getTitle());
    }
}
