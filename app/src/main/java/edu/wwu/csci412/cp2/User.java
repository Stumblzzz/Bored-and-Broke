package edu.wwu.csci412.cp2;

//TODO: Use this object
public class User {
    private String username;
    private String profilePictureUrl;
    private String profileBackgroundPictureUrl;
    //TODO: Activity Object
    //TODO: Reviews Object

    public User(String username){
        this.username = username;
    }

    public void setUsername(String newUsername){
        username = newUsername;
    }

    public String getUsername(){
        return username;
    }
}
