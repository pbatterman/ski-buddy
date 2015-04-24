package com.example.zkrasner.skibuddy;

/**
 * Created by pbatterman on 3/26/15.
 */
public class Lift {
    private String name;
    private double waitTime;
    private double duration;
    private int capacity;
    private WeightedTimeAverage timeAverage;

    public Lift(){
        timeAverage = new WeightedTimeAverage();
    }


    public void setCapacity(int capacity) {
      this.capacity = capacity;
    }

    public void insertWaitTime(int a) {
        timeAverage.inputMeasurement(a);
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

    public double getWaitTime() {
        return timeAverage.calculateMovingAverage();
    }

    public double getDuration() {
        return duration;
    }

    public int getCapacity() {
        return capacity;
    }

}
