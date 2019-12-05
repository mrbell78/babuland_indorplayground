package com.babuland.babuland1.model;

public class Model_ticket {

    Integer total_amount;
    String time;
    String status;
    Integer orderid;
    String branch;
    String phone;


    public Model_ticket() {
    }

    public Model_ticket(Integer total_amount, String time, String status, Integer orderid, String branch, String phone) {
        this.total_amount = total_amount;
        this.time = time;
        this.status = status;
        this.orderid = orderid;
        this.branch = branch;
        this.phone = phone;
    }


    public Integer getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Integer total_amount) {
        this.total_amount = total_amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
