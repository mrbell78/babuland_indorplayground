package com.babuland.babuland.model;

public class Model_childlist {

    String child_image;
    String dob;
    String child_name;
    String child_gender;

    public Model_childlist() {
    }

    public Model_childlist(String child_image, String dob, String child_name, String child_gender) {
        this.child_image = child_image;
        this.dob = dob;
        this.child_name = child_name;
        this.child_gender = child_gender;
    }


    public String getChild_image() {
        return child_image;
    }

    public void setChild_image(String child_image) {
        this.child_image = child_image;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    public String getChild_gender() {
        return child_gender;
    }

    public void setChild_gender(String child_gender) {
        this.child_gender = child_gender;
    }
}
