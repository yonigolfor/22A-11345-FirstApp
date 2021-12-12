package com.example.firstapp;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard extends Application {
    private ArrayList<Record> recordsArray;
    private static Leaderboard leaderboard;

    public Leaderboard() {
        leaderboard = this;
        this.recordsArray = new ArrayList<Record>();
    }

    public void setRecordsArray(ArrayList<Record> recordsArray) {
        this.recordsArray = recordsArray;
    }


    public void addRecordToRecordsArray(Record record) {


        // save top 5 players
        if (recordsArray.size() < 5) {
            recordsArray.add(record);
            Collections.sort((recordsArray));

        } else {
            recordsArray.remove(recordsArray.size() - 1);
            recordsArray.add(record);
            //sort Array
            Collections.sort((recordsArray));
        }

    }



    public boolean isRecordCanAddLeaderboard(Record record) {
        if (recordsArray.size() < 10) {
            return true;
        } else {
            for (Record recordInArray : recordsArray) {
                if (record.getPoints() > recordInArray.getPoints()) {
                    return true;
                }
            }
        }
        return false;
    }


    public ArrayList<Record> getRecordsArray() {
        return recordsArray;
    }


    @Override
    public String toString() {
        String str = "";
        for (int i =0; i< recordsArray.size(); i++)
            str+=recordsArray.get(i).getPoints()+"- " +recordsArray.get(i).getName()+", ";

        return str;
    }

    public static Leaderboard getInstance() {
        if (leaderboard == null) {
            leaderboard = new Leaderboard();
        }
        return leaderboard;
    }




}