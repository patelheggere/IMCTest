package com.patelheggere.imctest.models;

public class UserDetails {
    private String fname;
    private String email;
    private String lat;
    private String lon;
    private String pwd;
    private String lname;

    public UserDetails() {
    }

    public UserDetails(String fname, String interest, String email, String lat, String pwd, String lname, String lon) {
        this.fname = fname;
        this.email = email;
        this.lat = lat;
        this.pwd = pwd;
        this.lname = lname;
        this.lon = lon;
    }

    public String getName() {
        return fname;
    }

    public void setName(String fname) {
        this.fname = fname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getFcm_token() {
        return lat;
    }

    public void setFcm_token(String lat) {
        this.lat = lat;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
}
