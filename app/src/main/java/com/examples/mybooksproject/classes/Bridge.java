package com.examples.mybooksproject.classes;

import android.graphics.Bitmap;

public class Bridge {
    private String name;
    private String authorName;
    private String description;
    private Bitmap pic;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bridge(String name, String authorName, String description, Bitmap pic, int id) {
        this.name = name;
        this.authorName = authorName;
        this.description = description;
        this.pic = pic;
        this.id = id;
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
