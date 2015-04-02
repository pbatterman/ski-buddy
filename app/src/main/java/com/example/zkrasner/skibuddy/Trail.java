package com.example.zkrasner.skibuddy;

/**
 * Created by pbatterman on 3/26/15.
 */
public class Trail {
    private String name;
    private String condition;
    private String rating;
    private String difficulty;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }


    public Trail(String condition, String rating, String difficulty) {
        this.condition = condition;
        this.rating = rating;
        this.difficulty = difficulty;
    }

    public String getRating() {
        return rating;
    }
    public String getCondition(){
        return condition;
    }
    public String getDifficulty(){
        return difficulty;
    }
}