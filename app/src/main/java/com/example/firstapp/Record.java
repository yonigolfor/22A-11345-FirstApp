package com.example.firstapp;

import java.io.Serializable;

public class Record implements Comparable {
    private String name;
    private int points;
    private double longitude;
    private double latitude;


    public Record(int points, String name, double latitude, double longitude) {
        this.points = points;
        this.name = name;
        this.latitude = latitude;
        this. longitude = longitude;
    }

    public int getPoints() {
        return points;
    }
    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {
        return latitude;
    }

    @Override
    public int compareTo(Object rec) {
        return points - ((Record)rec).getPoints();
    }

    public void setLat(double v) {
        this.latitude = v;
    }
    public void setLon(double v) {
        this.longitude = v;
    }

}