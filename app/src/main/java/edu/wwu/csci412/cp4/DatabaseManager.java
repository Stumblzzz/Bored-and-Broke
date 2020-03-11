package edu.wwu.csci412.cp4;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String DATABASE_NAME = "boredandbroke";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ACTIVITIES = "activities";
    private static final String[] COLUMNS_ACTIVITIES = {"id", "activityName", "description", "name", "latitude", "longitude"};
    private static final String TABLE_REVIEWS = "reviews";
    private static final String[] COLUMNS_REVIEWS = {"id", "activityId", "rating", "title", "description", "userId"};
    private static final String TABLE_USERS = "users";
    private static final String[] COLUMNS_USERS = {"id", "username", "profilePictureUrl", "profileBackgroundUrl"};

    private SQL_Utils utils;

    /* TODO:
     * Should we create "User_Activities" and "User_Reviews" tables to store (userId, activityId)
     * and (userId, reviewId) pairs?
     */

    public DatabaseManager() {
        utils = new SQL_Utils();
    }

    public boolean insertActivity(Activity activity) {
        int id = activity.getId();
        String name = activity.getName();
        String description = activity.getDescription();
        int userId = activity.getCreator().getId();
        String username = activity.getCreator().getUsername();
        double latitude = activity.getLocation().getLatitude();
        double longitude = activity.getLocation().getLongitude();

        String[] values = {String.valueOf(id), name, description, username, String.valueOf(latitude), String.valueOf(longitude)};

        return utils.sqlInsert(TABLE_ACTIVITIES, COLUMNS_ACTIVITIES, values);
    }

    public boolean insertReview(Review review) {
        int id = review.getId();
        int activityId = review.getActivity().getId();
        double rating = review.getRating();
        String title = review.getTitle();
        String description = review.getDescription();
        int userId = review.getCreator().getId();

        String[] values = {String.valueOf(id), String.valueOf(activityId), String.valueOf(rating), title, description, String.valueOf(userId)};

        return utils.sqlInsert(TABLE_REVIEWS, COLUMNS_REVIEWS, values);
    }

    public boolean insertUser(User user) {
        int id = user.getId();
        String username = user.getUsername();
        String profilePictureUrl = user.getProfilePictureUrl();
        String profileBackgroundPictureUrl = user.getProfileBackgroundPictureUrl();

        String[] values = {String.valueOf(id), username, profilePictureUrl, profileBackgroundPictureUrl};

        return utils.sqlInsert(TABLE_USERS, COLUMNS_USERS, values);
    }

    public boolean deleteActivityById(int id) {
        String condition = "id=" + String.valueOf(id);

        return utils.sqlDelete(TABLE_ACTIVITIES, condition);
    }

    public boolean deleteReviewById(int id) {
        String condition = "id=" + String.valueOf(id);

        return utils.sqlDelete(TABLE_REVIEWS, condition);
    }

    public boolean deleteUserById(int id) {
        String condition = "id=" + String.valueOf(id);

        return utils.sqlDelete(TABLE_USERS, condition);
    }

    public void updateActivityById(int id, String name, String description, User creator, Location location) {

    }

    public void updateReviewById(int id, Activity activity, double rating, String title, String description, User creator) {

    }

    public void updateUserById(int id, String username, String profilePictureUrl, String profileBackgroundPictureUrl, ArrayList<Activity> activities, ArrayList<Review> reviews) {

    }

    public ArrayList<Activity> selectAllActivities() {
        ArrayList<Activity> activities = new ArrayList<>();
        int id;
        String name;
        String description;
        User creator;
        Location location;

        ResultSet results = utils.sqlSelect(TABLE_ACTIVITIES, "1=1");

        try {
            while(results.next()) {
                id = results.getInt(0);
                name = results.getString(1);
                description = results.getString(2);
                // Get creator here?
                location = new Location(results.getDouble(4), results.getDouble(5));

                activities.add(new Activity(id, name, description, location));
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        return activities;
    }

    public ArrayList<Review> selectAllReviews() {
        ArrayList<Review> reviews = new ArrayList<>();
        int id;
        int activityId;
        double rating;
        String title;
        String description;
        int userId;
        Activity activity;
        User creator;

        ResultSet results = utils.sqlSelect(TABLE_REVIEWS, "1=1");

        try {
            while(results.next()) {
                id = results.getInt(0);
                activityId = results.getInt(1);
                rating = results.getDouble(2);
                title = results.getString(3);
                description = results.getString(4);
                userId = results.getInt(5);
                // Get activity here?
                // Get creator here?

                reviews.add(new Review(id, rating, title, description));
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        return reviews;
    }

    public ArrayList<User> selectAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        int id;
        String username;
        String profilePictureUrl;
        String profileBackgroundPictureUrl;

        ResultSet results = utils.sqlSelect(TABLE_USERS, "1=1");

        try {
            while(results.next()) {
                id = results.getInt(0);
                username = results.getString(1);
                profilePictureUrl = results.getString(2);
                profileBackgroundPictureUrl = results.getString(3);

                User user = new User(id, username);
                user.setProfilePictureUrl(profilePictureUrl);
                user.setProfileBackgroundPictureUrl(profileBackgroundPictureUrl);

                users.add(user);
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        return users;
    }

    public Activity selectActivityById(int id) {
        Activity activity = null;
        Location location;
        String name;
        String description;

        String condition = "id=" + id;

        ResultSet result = utils.sqlSelect(TABLE_ACTIVITIES, condition);

        try {
            if(result.next()) {
                name = result.getString(1);
                description = result.getString(2);

                location = new Location(result.getDouble(4), result.getDouble(5));
                activity = new Activity(id, name, description, location);
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        return activity;
    }

    public Review selectReviewById(int id) {
        Review review = null;
        int activityId;
        double rating;
        String title;
        String description;
        int userId;
        Activity activity;
        User creator;

        String condition = "id=" + id;

        ResultSet results = utils.sqlSelect(TABLE_REVIEWS, condition);

        try {
            if(results.next()) {
                activityId = results.getInt(1);
                rating = results.getDouble(2);
                title = results.getString(3);
                description = results.getString(4);
                userId = results.getInt(5);
                // Get activity here?
                // Get creator here?

                review = new Review(id, rating, title, description);
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        return review;
    }

    public User selectUserById(int id) {
        User user = null;
        String username;
        String profilePictureUrl;
        String profileBackgroundPictureUrl;

        String condition = "id=" + id;

        ResultSet results = utils.sqlSelect(TABLE_USERS, condition);

        try {
            if(results.next()) {
                username = results.getString(1);
                profilePictureUrl = results.getString(2);
                profileBackgroundPictureUrl = results.getString(3);

                user = new User(id, username);
                user.setProfilePictureUrl(profilePictureUrl);
                user.setProfileBackgroundPictureUrl(profileBackgroundPictureUrl);
            }
        } catch(SQLException sqle) {
            sqle.printStackTrace();
        }

        return user;
    }
}
