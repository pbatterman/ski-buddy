package com.example.zkrasner.skibuddy;

import java.util.ArrayList;

/**
 * Created by Lucas on 4/1/15.
 */
public class WeightedTimeAverage {


    private ArrayList<Integer> measurements;
    private ArrayList<Long> times;

    public WeightedTimeAverage(){
        measurements = new ArrayList<Integer>();
        times = new ArrayList<Long>();

    }

    public void shoveThatDirtyMeasurementInMyArrayList(int input){
        measurements.add(input);
        times.add(System.currentTimeMillis());

    }

    public double returnMovingAverageToMyWaitingBrain(){
        if(measurements.size() == 0){
            return 0.0;
        }
        int count = 0;
        long total = 0;
        // need error handling here
        for(int i = 0; i < measurements.size(); i++){
            int curr_meas = measurements.get(i);
            long timestamp = times.get(i);
            if(System.currentTimeMillis() - timestamp  < 1.8E6){
                count++;
                total += curr_meas;
            }

        }

        return (double) count / total;
    }

}
