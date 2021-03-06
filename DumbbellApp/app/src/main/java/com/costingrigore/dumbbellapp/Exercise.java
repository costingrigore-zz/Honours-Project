package com.costingrigore.dumbbellapp;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

/**
 * This class is used to create exercise objects
 */
public class Exercise {
    /**
     * Field used to store the exercise's ID used to find the exercise's image from the application's internal storage
     */
    public Integer exerciseID;
    /**
     * String storing the exercise's name
     */
    public String name;
    /**
     * String storing the exercise's difficulty
     */
    public String difficulty;
    /**
     * String storing the exercise's type (cardio, strength or flexibility)
     */
    public String type;
    /**
     * String storing the exercise's body part target (lower body, upper body, total body or core)
     */
    public String body_part;

    /**
     * This method returns the exercise ID, used to get the exercise's image/icon
     *
     * @return Returns exercise ID
     */
    public Integer getIcon() {
        return exerciseID;
    }

    /**
     * This method sets the exercise ID, used to get the exercise's image/icon
     *
     * @param icon Exercise ID
     */
    public void setIcon(Integer icon) {
        this.exerciseID = icon;
    }

    /**
     * This method returns the exercise's name
     *
     * @return Returns exercise name
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the exercise's name
     *
     * @param name Exercise name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns the exercise's difficulty
     *
     * @return Returns exercise difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * This method sets the exercise's difficulty
     *
     * @param difficulty Exercise difficulty
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * This method returns the exercise's type
     *
     * @return Returns exercise type
     */
    public String getType() {
        return type;
    }

    /**
     * This method sets the exercise's type
     *
     * @param type Exercise type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * This method returns the exercise's body part
     *
     * @return Returns exercise body part
     */
    public String getBody_part() {
        return body_part;
    }

    /**
     * This method sets the exercise's body part
     *
     * @param body_part Exercise body part
     */
    public void setBody_part(String body_part) {
        this.body_part = body_part;
    }
}
