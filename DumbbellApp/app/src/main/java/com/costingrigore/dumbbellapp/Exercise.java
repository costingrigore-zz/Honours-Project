package com.costingrigore.dumbbellapp;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

public class Exercise{
    public Integer exerciseID;
    public String name;
    public String difficulty;
    public String type;
    public String body_part;

    public Integer getIcon() {
        return exerciseID;
    }
    public void setIcon(Integer icon) {
        this.exerciseID = icon;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getBody_part() {
        return body_part;
    }
    public void setBody_part(String body_part) {
        this.body_part = body_part;
    }

}
