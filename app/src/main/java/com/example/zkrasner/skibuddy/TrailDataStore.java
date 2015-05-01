package com.example.zkrasner.skibuddy;

import java.util.ArrayList;

/**
 * Created by Lucas on 4/9/15.
 */
public class TrailDataStore {

    public static ArrayList<String> trails = new ArrayList<String>();
    public static ArrayList<Integer> observations = new ArrayList<Integer>();
    public static ArrayList<Integer> average_condition = new ArrayList<Integer>();
    public static ArrayList<Integer> average_rating = new ArrayList<Integer>();
    public static String[] conditions = {"Icy","Granular", "Groomed", "Packed Powder", "Powder"};
    public static boolean added_Once = false;


    // Inserts a trail into the list
    public static void insertTrail(String a){
        trails.add(a);
        observations.add(0);
        average_condition.add(0);
        average_rating.add(0);
    }

    // Gets the index for the trails
    public static int getTrailIndex(String a){
        for(int i = 0; i < trails.size(); i++){
            if(trails.get(i).equals(a)){
                return i;
            }


        }

        return -1;
    }

    // Inserts rating into running average
    public static void insertRating(String a, int condition, int rating){
        int index = getTrailIndex(a);
        int old_number = observations.get(index);
        observations.set(index,observations.get(index) + 1);

        average_condition.set(index, condition);


        int old_rating = average_rating.get(index);
        float new_rating = ((old_number * old_rating) + (rating)) / (old_number+1);
        int final_rating = Math.round(new_rating);
        average_rating.set(index,final_rating);


    }

    // Returns the average condition rating
    public static String returnCondition(String a){
        for(int i = 0; i < trails.size(); i++){
            if(trails.get(i).equals(a)){
                int con = average_condition.get(i);
                return conditions[con];
            }


        }

        return "no such trail";
    }

    // Returns the average rating
    public static String returnRating(String a){
        for(int i = 0; i < trails.size(); i++){
            if(trails.get(i).equals(a)){
                return average_rating.get(i).toString();
            }


        }

        return "no such trail";
    }


}
