package com.example.bottleneck.sithub.SubscribedList;

/**
 * Created by priyankpatel on 10/7/16.
 */
public class Listchild {

    private String mShowName;
    private String mShowImage;
    private String mShowTime;
    private String mApiId;

    public String getName() {
        return mShowName;
    }

    public void setName(String name) {
        this.mShowName = name;
    }

    public String getImage() {
        return mShowImage;
    }

    public void setImage(String image) {
        this.mShowImage = image;
    }

    public String getShowTime() {
        return mShowTime;
    }

    public void setShowTime(String mShowTime) {
        this.mShowTime = mShowTime;
    }

    public String getApiId() {
        return mApiId;
    }

    public void setApiId(String mApiId) {
        this.mApiId = mApiId;
    }
}
