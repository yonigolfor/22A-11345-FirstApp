package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

public class menuScreen extends AppCompatActivity {
    private Button btn_startGame;
    private Button btn_record;
    private RadioButton rbArrows;
    private RadioButton rbSensor;
    private boolean isArrowsChecked = true;
    private EditText et_userName;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setIds();




    }

    private void setIds() {
        btn_startGame = findViewById(R.id.btn_startGame);
        btn_record = findViewById(R.id.btn_record);

        btn_startGame.setOnClickListener((View v) -> {
            startGameHandler();
        });

        btn_record.setOnClickListener((View v) -> {
            btnRecordHandler();
        });

        RadioButton rbArrows = findViewById(R.id.rb_arrows);
        RadioButton rbSensor = findViewById(R.id.rb_sensor);

        et_userName = findViewById(R.id.et_userName);

    }

    private void btnRecordHandler() {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }

    private void startGameHandler() {
        String userName = et_userName.getText().toString();
        if(!userName.isEmpty()){

            Intent intent = new Intent(this, MainActivity.class);

            intent.putExtra("isArrows", getGameKind()); //data to pass
            intent.putExtra("userName", userName);


            startActivity(intent);
            finish();
        }


    }

    public boolean getGameKind() {
        if(isArrowsChecked)
            return true;
        return false;
    }

    public void onRbClick(View view){

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb_arrows:
                isArrowsChecked = true;
                break;
            case R.id.rb_sensor:
                isArrowsChecked = false;
                break;
        }

    }

}
