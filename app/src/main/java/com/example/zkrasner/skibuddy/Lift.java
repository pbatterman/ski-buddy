package com.example.zkrasner.skibuddy;

/**
 * Created by pbatterman on 3/26/15.
 */
public class Lift {
    private String name;
    private double waitTime;
    private double duration;
    private int capacity;

    public Lift(){

    }


    public void setCapacity(int capacity) {
      this.capacity = capacity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWaitTime(double waitTime) {
        this.waitTime = waitTime;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }


    public String getName() {
        return name;
    }

    public double getDuration() {
        return duration;
    }

    public int getCapacity() {
        return capacity;
    }

}
