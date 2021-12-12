package com.example.firstapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.firstapp.ListFregments;
import com.example.firstapp.MapsFregments;
import com.example.firstapp.Callback_List;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {
    private ArrayList<Integer> distances;
    private ListFregments listFragment;
    private MapsFregments mapsFragment;
    private Callback_List callbackList;
    private FragmentManager fragmentManager;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        listFragment = new ListFregments();
        mapsFragment = new MapsFregments();
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, menuScreen.class);
            startActivity(intent);
            finish();
        });
        callbackList = new Callback_List() {
            @Override
            public void setMapLocation(double latitude, double longitude) {
                mapsFragment.changeMap(latitude, longitude);
            }
        };

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.listFrame, listFragment).commit();

        // the bug is here
        fragmentManager.beginTransaction().add(R.id.mapsFrame, mapsFragment).commit();


        listFragment.setActivity(this);
        System.out.println("Reached! 2");
        listFragment.setCallbackList(callbackList);
        System.out.println("Reached! 3");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}