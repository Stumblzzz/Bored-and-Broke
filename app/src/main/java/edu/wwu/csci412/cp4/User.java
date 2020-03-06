package edu.wwu.csci412.cp4;

import java.util.ArrayList;

//TODO: Use this object
public class User {
    private int id;
    private String username;
    private String profilePictureUrl;
    private String profileBackgroundPictureUrl;
    private ArrayList<Activity> activities;
    private ArrayList<Review> reviews;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
        this.activities = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public void addActivity(Activity activity) {
        if(!activities.contains(activity)) {
            activities.add(activity);
        }
    }

    public void addReview(Review review) {
        if(!reviews.contains(review)) {
            reviews.add(review);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getProfileBackgroundPictureUrl() {
        return profileBackgroundPictureUrl;
    }

    public void setProfileBackgroundPictureUrl(String profileBackgroundPictureUrl) {
        this.profileBackgroundPictureUrl = profileBackgroundPictureUrl;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
