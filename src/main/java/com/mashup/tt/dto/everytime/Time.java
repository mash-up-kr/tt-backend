package com.mashup.tt.dto.everytime;

public class Time {

    private String displayTime;
    private int day;
    private int startTime;
    private int endTime;
    private String place;

    public Time(String displayTime, int day, int startTime, int endTime, String place) {
        this.displayTime = displayTime;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
    }

    public String getDisplayTime() {
        return displayTime;
    }

    public int getDay() {
        return day;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public String getPlace() {
        return place;
    }
}
