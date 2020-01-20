package com.babuland.babuland.model;

public class Childlist_newreg {

    String child_gender;
    String child_image;
    String child_name;
    String classs;
    String dob;
    String pre_branch;
    String school;

    public Childlist_newreg() {
    }

    public Childlist_newreg(String child_gender, String child_image, String child_name, String classs, String dob, String pre_branch, String school) {
        this.child_gender = child_gender;
        this.child_image = child_image;
        this.child_name = child_name;
        this.classs = classs;
        this.dob = dob;
        this.pre_branch = pre_branch;
        this.school = school;
    }

    public String getChild_gender() {
        return child_gender;
    }

    public void setChild_gender(String child_gender) {
        this.child_gender = child_gender;
    }

    public String getChild_image() {
        return child_image;
    }

    public void setChild_image(String child_image) {
        this.child_image = child_image;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    public String getClasss() {
        return classs;
    }

    public void setClasss(String classs) {
        this.classs = classs;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPre_branch() {
        return pre_branch;
    }

    public void setPre_branch(String pre_branch) {
        this.pre_branch = pre_branch;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }


}
