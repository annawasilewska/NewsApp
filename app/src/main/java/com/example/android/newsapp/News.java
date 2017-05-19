package com.example.android.newsapp;

public class News {

    // Title
    private String mTitle;

    // Section
    private String mSection;

    // Date published
    private String mDate;

    // URL
    private String mWebPageURL;

    public News(String title, String section, String date, String webPageURL) {
        mTitle = title;
        mSection = section;
        mDate = date;
        mWebPageURL = webPageURL;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getDate() {
        return mDate;
    }

    public String getWebPageURL() {
        return mWebPageURL;
    }
}
