package edu.wwu.csci412.cp4;

public class Review {
    private int id;
    private Activity activity;
    private double rating;
    private String title;
    private String description;
    private User creator;

    public Review(int id, Activity activity, double rating, String title, String description, User creator) {
        this.id = id;
        this.activity = activity;
        this.rating = rating;
        this.title = title;
        this.description = description;
        this.creator = creator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id == review.id &&
                Double.compare(review.rating, rating) == 0 &&
                activity.equals(review.activity) &&
                title.equals(review.title) &&
                description.equals(review.description) &&
                creator.equals(review.creator);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", activity=" + activity +
                ", rating=" + rating +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creator=" + creator +
                '}';
    }
}
