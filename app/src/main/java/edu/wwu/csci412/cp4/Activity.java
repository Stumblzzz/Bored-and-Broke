package edu.wwu.csci412.cp4;

public class Activity {
    private int id;
    private String name;
    private String description;
    private User creator;
    private Location location;

    public Activity(int id, String name, String description, User creator, Location location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.location = location;
    }

    public Activity(String name, String description, User creator, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.location = new Location(latitude, longitude);
    }

    public Activity(String name, String description, User creator, String address) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.location = new Location(address);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creator=" + creator +
                ", location=" + location +
                '}';
    }
}
