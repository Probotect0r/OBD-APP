package com.example.sagar.myapplication;


public class RecentDriveCard {

    private String date, duration;

    public RecentDriveCard (String dateInput, String durationInput){
        this.date = dateInput;
        this.duration = durationInput;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}
