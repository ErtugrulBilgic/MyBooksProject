package com.examples.mybooksproject;

import android.graphics.Bitmap;

public class Bridge {
    private String name;
    private String authorName;
    private String description;
    private Bitmap pic;

    public Bridge(String name, String authorName, String description, Bitmap pic) {
        this.name = name;
        this.authorName = authorName;
        this.description = description;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }
}
