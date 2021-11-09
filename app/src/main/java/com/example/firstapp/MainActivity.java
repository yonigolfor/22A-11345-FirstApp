package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView car_image_left;
    private ImageView car_image_center;
    private ImageView car_image_right;

    // know if the car is on the sides so get it back to the center
    private boolean carIsLeft = false;
    private boolean carIsRight = false;

    private ImageButton btnLeft;
    private ImageButton btnRight;

    // setting rocks
    private ImageView rock00;
    private ImageView rock01;
    private ImageView rock02;
    private ImageView rock10;
    private ImageView rock11;
    private ImageView rock12;
    private ImageView rock20;
    private ImageView rock21;
    private ImageView rock22;
    private ImageView rock30;
    private ImageView rock31;
    private ImageView rock32;
    private ImageView rock40;
    private ImageView rock41;
    private ImageView rock42;

    private ImageView[][]imgs = {
            {rock00,rock01, rock02},
            {rock10, rock11, rock12},
            {rock20, rock21, rock22},
            {rock30, rock31, rock32},
            {rock40, rock41, rock42}
    };


    // responsible for generatin num 0/1/2
    int randomCol;

    private int lifeLast;

    private ImageView star_3_life;
    private ImageView star_2_life;
    private ImageView star_1_life;

    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set images
        setBoard();

        // set rocks
        setRocks();

        //every 2 sec take rock step down + send new rock
        rocksControl();

    }

    private void rocksControl() {

        putFirstLineRock();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(()->{

                    // rock's next step
                    rocksNextStep();
                    // send new Rock
                    putFirstLineRock();

                });
            }
        }, 1000, 1000);


    }
                                                // סדר הבדיקה של מטריצת האבנים
    private void rocksNextStep() {                      //789
        for (int i = imgs.length - 1; i >= 0; i--){     //456
            for (int j = 0; j < imgs[i].length; j++){   //123
                // check every rock and if is visible -> advance him

                if (imgs[i][j].getVisibility() == View.VISIBLE) {
                    // case: last line
                    if (i == imgs.length - 1)
                        lastLineCase(j);

                    // case: regular line
                    else
                        imgs[i + 1][j].setVisibility(View.VISIBLE);

                   disapearRock(i, j);
                }
            }
        }
    }

    private void lastLineCase(int col) {
        // check crash on sides
        if (carIsLeft && (col == 0) ||
            carIsRight && (col == 2)) {
            //crash
            crash();
        }
        else
            if(!carIsLeft && !carIsRight && (col == 1)) {
                //crash in center
                crash();
            }
    }

    private void disapearRock(int row, int col) {
        imgs[row][col].setVisibility(View.INVISIBLE);

    }

    private void crash() {
        switch (lifeLast){
            case 3:
                star_3_life.setVisibility(View.INVISIBLE);
                lifeLast --;
                break;
            case 2:
                star_2_life.setVisibility(View.INVISIBLE);
                lifeLast --;
                break;
            case 1:
                loseGame();
                star_3_life.setVisibility(View.VISIBLE);
                star_2_life.setVisibility(View.VISIBLE);
                lifeLast = 3;
                break;
        }

    }


    private void loseGame() {
        // Toast msg
        toastMsg();
        // Vibrate screen
        vibrateScrn();
    }

    private void vibrateScrn() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 1000 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(1000);
        }
    }

    private void toastMsg() {
        Toast.makeText(this, "You lose!", Toast.LENGTH_LONG).show();
    }

    private void setRocks() {

        rock00 = findViewById(R.id.rock00);
        rock01 = findViewById(R.id.rock01);
        rock02 = findViewById(R.id.rock02);
        rock10 = findViewById(R.id.rock10);
        rock11 = findViewById(R.id.rock11);
        rock12 = findViewById(R.id.rock12);
        rock20 = findViewById(R.id.rock20);
        rock21 = findViewById(R.id.rock21);
        rock22 = findViewById(R.id.rock22);
        rock30 = findViewById(R.id.rock30);
        rock31 = findViewById(R.id.rock31);
        rock32 = findViewById(R.id.rock32);
        rock40 = findViewById(R.id.rock40);
        rock41 = findViewById(R.id.rock41);
        rock42 = findViewById(R.id.rock42);

        imgs[0][0] = rock00;
        imgs[0][1] = rock01;
        imgs[0][2] = rock02;
        imgs[1][0] = rock10;
        imgs[1][1] = rock11;
        imgs[1][2] = rock12;
        imgs[2][0] = rock20;
        imgs[2][1] = rock21;
        imgs[2][2] = rock22;
        imgs[3][0] = rock30;
        imgs[3][1] = rock31;
        imgs[3][2] = rock32;
        imgs[4][0] = rock40;
        imgs[4][1] = rock41;
        imgs[4][2] = rock42;


        // invisible all
        rock00.setVisibility(View.INVISIBLE);
        rock01.setVisibility(View.INVISIBLE);
        rock02.setVisibility(View.INVISIBLE);
        rock10.setVisibility(View.INVISIBLE);
        rock11.setVisibility(View.INVISIBLE);
        rock12.setVisibility(View.INVISIBLE);
        rock20.setVisibility(View.INVISIBLE);
        rock21.setVisibility(View.INVISIBLE);
        rock22.setVisibility(View.INVISIBLE);
        rock30.setVisibility(View.INVISIBLE);
        rock31.setVisibility(View.INVISIBLE);
        rock32.setVisibility(View.INVISIBLE);
        rock40.setVisibility(View.INVISIBLE);
        rock41.setVisibility(View.INVISIBLE);
        rock42.setVisibility(View.INVISIBLE);

    }

    private void putFirstLineRock() {
        // get random starting col
        randomCol = new Random().nextInt(3); // generate num 0\1\2

        imgs[0][randomCol].setVisibility(View.VISIBLE);

    }


    private void setBoard() {

        car_image_center = findViewById(R.id.car_image_center);
        car_image_left = findViewById(R.id.car_image_left);
        car_image_right = findViewById(R.id.car_image_right);

        // disapear left and right images
        car_image_right.setVisibility(View.INVISIBLE);
        car_image_left.setVisibility(View.INVISIBLE);
        car_image_center.setVisibility(View.VISIBLE);

        //set buttons
        btnRight = (ImageButton) findViewById(R.id.btn_right);
        btnLeft = (ImageButton) findViewById(R.id.btn_left);

        // set listeners
        btnRight.setOnClickListener((View v) -> {
        // case: car is at the right already => cant be double right
            if (carIsRight);

            // case: car is in the left side => move to center
            else if (carIsLeft) {
                car_image_center.setVisibility(View.VISIBLE);
                car_image_left.setVisibility(View.INVISIBLE);
                carIsLeft = false;
            }
            // case: car is at center => move right
            else {
                car_image_center.setVisibility(View.INVISIBLE);
                car_image_right.setVisibility(View.VISIBLE);
                carIsRight = true;
            }

        });

        // left btn listener
        btnLeft.setOnClickListener((View v) -> {
            // case: car is at the left already => cant be double left
            if (carIsLeft);

            // case: car is in the right side => move to center
            else if (carIsRight) {
                car_image_center.setVisibility(View.VISIBLE);
                car_image_right.setVisibility(View.INVISIBLE);
                carIsRight = false;
            }
            // case: car is at center => move left
            else {
                car_image_center.setVisibility(View.INVISIBLE);
                car_image_left.setVisibility(View.VISIBLE);
                carIsLeft = true;
            }

        });

        lifeLast = 3;

        star_3_life = findViewById(R.id.star_3_life);
        star_2_life = findViewById(R.id.star_2_life);
        star_1_life = findViewById(R.id.star_1_life);
    }


}