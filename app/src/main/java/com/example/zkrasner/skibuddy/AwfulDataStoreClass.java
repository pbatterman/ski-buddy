package com.example.zkrasner.skibuddy;

import java.util.ArrayList;

/**
 * Created by Lucas on 4/9/15.
 */
public class AwfulDataStoreClass {

    public static ArrayList<String> trails = new ArrayList<String>();
    public static ArrayList<Integer> observations = new ArrayList<Integer>();
    public static ArrayList<Integer> average_condition = new ArrayList<Integer>();
    public static ArrayList<Integer> average_rating = new ArrayList<Integer>();
    public static String[] conditions = {"Icy", "Powder", "Groomed"};
    public static boolean added_Once = false;


    public static void insertTrail(String a){
        trails.add(a);
        observations.add(0);
        average_condition.add(0);
        average_rating.add(0);
    }

    public static int getTrailIndex(String a){
        for(int i = 0; i < trails.size(); i++){
            if(trails.get(i).equals(a)){
                return i;
            }


        }

        return -1;
    }

    public static void InsertRating(String a, int condition, int rating){
        int index = getTrailIndex(a);
        int old_number = observations.get(index);
        observations.set(index,observations.get(index) + 1);

        int old_condition = average_condition.get(index);
        float new_condition = ((old_number * old_condition) + (condition)) / (old_number+1);
        int final_condition = Math.round(new_condition);
        average_condition.set(index,final_condition);


        int old_rating = average_rating.get(index);
        float new_rating = ((old_number * old_rating) + (rating)) / (old_number+1);
        int final_rating = Math.round(new_rating);
        average_rating.set(index,final_rating);


    }


    public static String returnCondition(String a){
        for(int i = 0; i < trails.size(); i++){
            if(trails.get(i).equals(a)){
                int con = average_condition.get(i);
                return conditions[con];
            }


        }

        return "no such trail";
    }


}
