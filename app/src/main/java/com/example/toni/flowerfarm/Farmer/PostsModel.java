package com.example.toni.flowerfarm.Farmer;

/**
 * Created by toni on 3/26/17.
 */

public class PostsModel {

    private String image;
    private String name;
    private String comment;
    private String date;
    private String feedback;
    private String mobile_number;

    public PostsModel() {
    }

    public PostsModel(String image, String name, String comment, String date, String feedback, String mobile_number) {
        this.image = image;
        this.name = name;
        this.comment = comment;
        this.date = date;
        this.feedback = feedback;
        this.mobile_number = mobile_number;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
