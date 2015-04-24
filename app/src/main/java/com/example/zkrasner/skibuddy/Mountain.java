package com.example.zkrasner.skibuddy;

import java.util.ArrayList;

/**
 * Created by pbatterman on 3/26/15.
 */
public class Mountain {
    String name;
    ArrayList<Trail> trails;
    ArrayList<Lift> lifts;

    public ArrayList<Lift> getLifts() {
        return lifts;
    }

    public ArrayList<Trail> getTrails() {
        return trails;
    }


    public Mountain (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addTrails(ArrayList<Trail> t){
        this.trails = t;
    }

    public void addLifts(ArrayList<Lift> t){
        this.lifts = t;
    }

    public void addLift(Lift l) {
        this.lifts.add(l);
    }
}
