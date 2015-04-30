package com.example.zkrasner.skibuddy;

import java.util.ArrayList;

/**
 * Created by pbatterman on 4/24/15.
 */
public class User {
    public String username;
    private ArrayList<User> friends;
    private ArrayList<String> favoriteSlopes;


    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    public void addFriend(User friend){
       if (!this.friends.contains(friend)) {
           this.friends.add(friend);
           friend.addFriend(this);
       }
    }

    public void removeFriend(User friend) {
        if (this.friends.contains(friend)) {
            this.friends.remove(friend);
        }
    }

    public ArrayList<String> getFavoriteSlopes() {
        return favoriteSlopes;
    }

    public void setFavoriteSlopes(ArrayList<String> favoriteSlopes) {
        this.favoriteSlopes = favoriteSlopes;
    }

    public void addFavoriteSlope(String slope){
        if (!this.favoriteSlopes.contains(slope)) {
            this.favoriteSlopes.add(slope);
        }
    }

    public void removeFavoriteSlope(String slope){
        if (this.favoriteSlopes.contains(slope)) {
            this.favoriteSlopes.remove(slope);
        }
    }

}
