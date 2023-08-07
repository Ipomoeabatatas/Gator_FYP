package com.example.teamgator;

public class RatingData {
    private float question1Rating;

    public RatingData() {
        // Required default constructor for Firebase
    }

    public float getQuestion1Rating() {
        return question1Rating;
    }

    public void setQuestion1Rating(float question1Rating) {
        this.question1Rating = question1Rating;
    }

}
