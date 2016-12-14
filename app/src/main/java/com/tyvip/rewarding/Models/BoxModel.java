package com.tyvip.rewarding.Models;

/**
 * Created by bryden on 12/8/16.
 */

public class BoxModel {
    private String imgURL;
    private String title;
    private String description;

    private int id;
    public BoxModel(String description, String imgURL, String title) {
        this.description = description;
        this.imgURL = imgURL;
        this.title = title;
    }

    public BoxModel() {
    }

    public BoxModel(String imgURL, String title, String description, int id) {
        this.imgURL = imgURL;
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
