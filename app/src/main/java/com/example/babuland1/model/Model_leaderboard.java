package com.example.babuland1.model;

public class Model_leaderboard {

    String name;
    String image;

    public Model_leaderboard() {
    }

    public Model_leaderboard(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
