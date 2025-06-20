package com.sehanw.technopulse.models;

import java.io.Serializable;

public class NewsItem implements Serializable {
    private String category;
    private String title;
    private String date;
    private int imageResource;

    public NewsItem(String category, String title, String date, int imageResource) {
        this.category = category;
        this.title = title;
        this.date = date;
        this.imageResource = imageResource;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}