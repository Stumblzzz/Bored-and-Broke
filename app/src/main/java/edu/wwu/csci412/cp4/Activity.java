package edu.wwu.csci412.cp4;

public class Activity {
    private int id;
    private String name;
    private String description;
    private User creator;
    private double latitude;
    private double longitude;
    private String address;

    public Activity(String name, String description, User creator, String address) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.address = address;
    }

    public Activity(String name, String description, User creator, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public User getCreator() {
        return creator;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }
}
