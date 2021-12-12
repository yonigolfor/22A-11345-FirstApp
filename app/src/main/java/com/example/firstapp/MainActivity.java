package com.example.firstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity {

    boolean isArrowsPassed;
    private TextView distanceCounter;
    private String userName = "";
    //private Leaderboard leaderboard;


    private double longitude;
    private double latitude;
    private LocationManager lm;
    private LocationListener locationListener;

    private ImageView car_image_0;
    private ImageView car_image_1;
    private ImageView car_image_2;
    private ImageView car_image_3;
    private ImageView car_image_4;

    private int carPlaceIndex = 2; // starting point at center

    private ImageButton btnLeft;
    private ImageButton btnRight;

    // setting rocks
    private ImageView rock00;
    private ImageView rock01;
    private ImageView rock02;
    private ImageView rock03;
    private ImageView rock04;
    private ImageView rock10;
    private ImageView rock11;
    private ImageView rock12;
    private ImageView rock13;
    private ImageView rock14;
    private ImageView rock20;
    private ImageView rock21;
    private ImageView rock22;
    private ImageView rock23;
    private ImageView rock24;
    private ImageView rock30;
    private ImageView rock31;
    private ImageView rock32;
    private ImageView rock33;
    private ImageView rock34;
    private ImageView rock40;
    private ImageView rock41;
    private ImageView rock42;
    private ImageView rock43;
    private ImageView rock44;
    private ImageView rock50;
    private ImageView rock51;
    private ImageView rock52;
    private ImageView rock53;
    private ImageView rock54;


    private ImageView[][]imgs = {
            {rock00, rock01, rock02, rock03, rock04},
            {rock10, rock11, rock12, rock13, rock14},
            {rock20, rock21, rock22, rock23, rock24},
            {rock30, rock31, rock32, rock33, rock34},
            {rock40, rock41, rock42, rock43, rock44},
            {rock50, rock51, rock52, rock53, rock54}
    };
    private ImageView[][]coins = new ImageView[6][5];


    int randomCol;
    int randomImg;

    private int lifeLast;

    private ImageView star_3_life;
    private ImageView star_2_life;
    private ImageView star_1_life;

    private Handler handler1;
    private Handler handler2;
    private Runnable runnable1;
    private Runnable runnable2;

    private SensorManager sensorManager;
    private Sensor mSensor;

    private boolean isTiltBack = false;
    private float xAxisStarter = -1;
    private float xAxisCurrent;

    private MediaPlayer crashRing;
    private MediaPlayer coinRing;
    private MediaPlayer loseRing;

    private int stopGame = 1;

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (isArrowsPassed);
            else{
                float x = sensorEvent.values[0];
                if(xAxisStarter == -1)
                    xAxisStarter = x;
                xAxisCurrent = x;

            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void handleCarMovement(float xAxis) {
        float x = xAxis - xAxisStarter;
        if(x >  1.5){ // turn left
            handleLeftClick();
        }
        else if (x < -1.5){ // turn right
            handleRightClick();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // set images
        setBoard();

        initLocation();

        // set rocks
        setRocks();



        //every 2 sec take rock step down + send new rock
        setHandlers();
        //rocksControl();

    }

    private void initLocation() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        System.out.println("lat in init location");

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                System.out.println("lat: " + latitude + " lon: " + longitude);
            }
        };

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        } else {
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
//        }


        handleLoc();
    }

    private void handleLoc() {
        boolean gps_enabled = false;
        boolean network_enabled = false;
        Location gps_loc = null, net_loc = null;

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
        }

        if (gps_enabled)
            gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (network_enabled)
            net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        latitude = gps_loc.getLatitude();
        longitude = gps_loc.getLongitude();

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
            }
        }

    }

    private void rocksControl() {
    // rock's next step
    rocksNextStep();
    coinsNextStep();

    // send new Rock
    putFirstLineImg();

    //add points to player
     addPoints(0);

    }

    private void coinsNextStep() {
        for (int i = coins.length - 1; i >= 0; i--) {
            for (int j = 0; j < coins[i].length; j++) {
                // check every coin and if is visible -> advance him
                if (coins[i][j].getVisibility() == View.VISIBLE) {
                    // case: last line
                    if (i == coins.length - 1)
                        lastLineCoinCase(j);

                        // case: regular line
                    else
                        coins[i + 1][j].setVisibility(View.VISIBLE);

                    disapearCoin(i, j);
                }
            }
        }
    }

    private void disapearCoin(int row, int col) {
        coins[row][col].setVisibility(View.INVISIBLE);
    }

    private void lastLineCoinCase(int col) {
        if(carPlaceIndex == col)
            addPoints(20);
    }

    private void addPoints(int num) {
        int distance;

        distance = Integer.parseInt(distanceCounter.getText().toString());
        if(num == 0)
            distance += 10;
        else {
            coinRing.start();
            distance += num;
        }
        distanceCounter.setText(String.valueOf(distance));
    }

    // סדר הבדיקה של מטריצת האבנים
    private void rocksNextStep() {                      //789...
        for (int i = imgs.length - 1; i >= 0; i--){     //456...
            for (int j = 0; j < imgs[i].length; j++){   //123...
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
        if (carPlaceIndex == col)
            crash();
    }

    private void disapearRock(int row, int col) {
        imgs[row][col].setVisibility(View.INVISIBLE);

    }

    private void crash() {



        switch (lifeLast){
            case 3:
                star_3_life.setVisibility(View.INVISIBLE);
                lifeLast --;
                crashSound();
                break;
            case 2:
                star_2_life.setVisibility(View.INVISIBLE);
                lifeLast --;
                crashSound();
                break;
            case 1:
                loseGame();
                star_3_life.setVisibility(View.VISIBLE);
                star_2_life.setVisibility(View.VISIBLE);
                lifeLast = 3;
                break;
        }

    }

    private void crashSound() {
        crashRing.start();
    }


    private void loseGame() {
        loseRing.start();
        // Toast msg
        toastMsg();
        // Vibrate screen
        vibrateScrn();
        //invisibleAll();
        stopGame = 1;

        int points = Integer.parseInt(distanceCounter.getText().toString());
        // handle record
        handleRecord(userName, points);

        lm.removeUpdates(locationListener);

        Intent intent = new Intent(this, menuScreen.class);

        intent.putExtra("points", points); //data to pass
        intent.putExtra("userName", userName);

        startActivity(intent);

        // finish activity
        finish();

    }

    private void handleRecord(String userName, int points) {
        Record record = new Record(points, userName, latitude, longitude);
        boolean isHighRec;
        // check leaderboard table
        isHighRec = Leaderboard.getInstance().isRecordCanAddLeaderboard(record);
        if(!isHighRec)
            return;

        // add to leaderboard
        Leaderboard.getInstance().addRecordToRecordsArray(record);
    }

    private void vibrateScrn() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 1000 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(800);
        }
    }

    private void toastMsg() {
        Toast.makeText(this, "You lose!", Toast.LENGTH_LONG).show();
    }

    private void setRocks() {

        rock00 = findViewById(R.id.rock00);
        rock01 = findViewById(R.id.rock01);
        rock02 = findViewById(R.id.rock02);
        rock03 = findViewById(R.id.rock03);
        rock04 = findViewById(R.id.rock04);
        rock10 = findViewById(R.id.rock10);
        rock11 = findViewById(R.id.rock11);
        rock12 = findViewById(R.id.rock12);
        rock13 = findViewById(R.id.rock13);
        rock14 = findViewById(R.id.rock14);
        rock20 = findViewById(R.id.rock20);
        rock21 = findViewById(R.id.rock21);
        rock22 = findViewById(R.id.rock22);
        rock23 = findViewById(R.id.rock23);
        rock24 = findViewById(R.id.rock24);
        rock30 = findViewById(R.id.rock30);
        rock31 = findViewById(R.id.rock31);
        rock32 = findViewById(R.id.rock32);
        rock33 = findViewById(R.id.rock33);
        rock34 = findViewById(R.id.rock34);
        rock40 = findViewById(R.id.rock40);
        rock41 = findViewById(R.id.rock41);
        rock42 = findViewById(R.id.rock42);
        rock43 = findViewById(R.id.rock43);
        rock44 = findViewById(R.id.rock44);
        rock50 = findViewById(R.id.rock50);
        rock51 = findViewById(R.id.rock51);
        rock52 = findViewById(R.id.rock52);
        rock53 = findViewById(R.id.rock53);
        rock54 = findViewById(R.id.rock54);

        imgs[0][0] = rock00;
        imgs[0][1] = rock01;
        imgs[0][2] = rock02;
        imgs[0][3] = rock03;
        imgs[0][4] = rock04;
        imgs[1][0] = rock10;
        imgs[1][1] = rock11;
        imgs[1][2] = rock12;
        imgs[1][3] = rock13;
        imgs[1][4] = rock14;
        imgs[2][0] = rock20;
        imgs[2][1] = rock21;
        imgs[2][2] = rock22;
        imgs[2][3] = rock23;
        imgs[2][4] = rock24;
        imgs[3][0] = rock30;
        imgs[3][1] = rock31;
        imgs[3][2] = rock32;
        imgs[3][3] = rock33;
        imgs[3][4] = rock34;
        imgs[4][0] = rock40;
        imgs[4][1] = rock41;
        imgs[4][2] = rock42;
        imgs[4][3] = rock43;
        imgs[4][4] = rock44;
        imgs[5][0] = rock50;
        imgs[5][1] = rock51;
        imgs[5][2] = rock52;
        imgs[5][3] = rock53;
        imgs[5][4] = rock54;

        coins[0][0] = findViewById(R.id.coin00);
        coins[0][1] = findViewById(R.id.coin01);
        coins[0][2] = findViewById(R.id.coin02);
        coins[0][3] = findViewById(R.id.coin03);
        coins[0][4] = findViewById(R.id.coin04);
        coins[1][0] = findViewById(R.id.coin10);
        coins[1][1] = findViewById(R.id.coin11);
        coins[1][2] = findViewById(R.id.coin12);
        coins[1][3] = findViewById(R.id.coin13);
        coins[1][4] = findViewById(R.id.coin14);
        coins[2][0] = findViewById(R.id.coin20);
        coins[2][1] = findViewById(R.id.coin21);
        coins[2][2] = findViewById(R.id.coin22);
        coins[2][3] = findViewById(R.id.coin23);
        coins[2][4] = findViewById(R.id.coin24);
        coins[3][0] = findViewById(R.id.coin30);
        coins[3][1] = findViewById(R.id.coin31);
        coins[3][2] = findViewById(R.id.coin32);
        coins[3][3] = findViewById(R.id.coin33);
        coins[3][4] = findViewById(R.id.coin34);
        coins[4][0] = findViewById(R.id.coin40);
        coins[4][1] = findViewById(R.id.coin41);
        coins[4][2] = findViewById(R.id.coin42);
        coins[4][3] = findViewById(R.id.coin43);
        coins[4][4] = findViewById(R.id.coin44);
        coins[5][0] = findViewById(R.id.coin50);
        coins[5][1] = findViewById(R.id.coin51);
        coins[5][2] = findViewById(R.id.coin52);
        coins[5][3] = findViewById(R.id.coin53);
        coins[5][4] = findViewById(R.id.coin54);

        // invisible all
        invisibleAll();

    }

    private void invisibleAll() {
        rock00.setVisibility(View.INVISIBLE);
        rock01.setVisibility(View.INVISIBLE);
        rock02.setVisibility(View.INVISIBLE);
        rock03.setVisibility(View.INVISIBLE);
        rock04.setVisibility(View.INVISIBLE);
        rock10.setVisibility(View.INVISIBLE);
        rock11.setVisibility(View.INVISIBLE);
        rock12.setVisibility(View.INVISIBLE);
        rock13.setVisibility(View.INVISIBLE);
        rock14.setVisibility(View.INVISIBLE);
        rock20.setVisibility(View.INVISIBLE);
        rock21.setVisibility(View.INVISIBLE);
        rock22.setVisibility(View.INVISIBLE);
        rock23.setVisibility(View.INVISIBLE);
        rock24.setVisibility(View.INVISIBLE);
        rock30.setVisibility(View.INVISIBLE);
        rock31.setVisibility(View.INVISIBLE);
        rock32.setVisibility(View.INVISIBLE);
        rock33.setVisibility(View.INVISIBLE);
        rock34.setVisibility(View.INVISIBLE);
        rock40.setVisibility(View.INVISIBLE);
        rock41.setVisibility(View.INVISIBLE);
        rock42.setVisibility(View.INVISIBLE);
        rock43.setVisibility(View.INVISIBLE);
        rock44.setVisibility(View.INVISIBLE);
        rock50.setVisibility(View.INVISIBLE);
        rock51.setVisibility(View.INVISIBLE);
        rock52.setVisibility(View.INVISIBLE);
        rock53.setVisibility(View.INVISIBLE);
        rock54.setVisibility(View.INVISIBLE);

        coins[0][0].setVisibility(View.INVISIBLE);
        coins[0][1].setVisibility(View.INVISIBLE);
        coins[0][2].setVisibility(View.INVISIBLE);
        coins[0][3].setVisibility(View.INVISIBLE);
        coins[0][4].setVisibility(View.INVISIBLE);
        coins[1][0].setVisibility(View.INVISIBLE);
        coins[1][1].setVisibility(View.INVISIBLE);
        coins[1][2].setVisibility(View.INVISIBLE);
        coins[1][3].setVisibility(View.INVISIBLE);
        coins[1][4].setVisibility(View.INVISIBLE);
        coins[2][0].setVisibility(View.INVISIBLE);
        coins[2][1].setVisibility(View.INVISIBLE);
        coins[2][2].setVisibility(View.INVISIBLE);
        coins[2][3].setVisibility(View.INVISIBLE);
        coins[2][4].setVisibility(View.INVISIBLE);
        coins[3][0].setVisibility(View.INVISIBLE);
        coins[3][1].setVisibility(View.INVISIBLE);
        coins[3][2].setVisibility(View.INVISIBLE);
        coins[3][3].setVisibility(View.INVISIBLE);
        coins[3][4].setVisibility(View.INVISIBLE);
        coins[4][0].setVisibility(View.INVISIBLE);
        coins[4][1].setVisibility(View.INVISIBLE);
        coins[4][2].setVisibility(View.INVISIBLE);
        coins[4][3].setVisibility(View.INVISIBLE);
        coins[4][4].setVisibility(View.INVISIBLE);
        coins[5][0].setVisibility(View.INVISIBLE);
        coins[5][1].setVisibility(View.INVISIBLE);
        coins[5][2].setVisibility(View.INVISIBLE);
        coins[5][3].setVisibility(View.INVISIBLE);
        coins[5][4].setVisibility(View.INVISIBLE);
    }

    private void putFirstLineImg() {
        // get random starting col
        randomCol = new Random().nextInt(5); // generate num 0\1\2\3\
        // generate random rock/coin
        randomImg = new Random().nextInt(4); // 0/1/2/3

        if(randomImg == 3)
            coins[0][randomCol].setVisibility(View.VISIBLE);
        else
            imgs[0][randomCol].setVisibility(View.VISIBLE);

    }


    private void setBoard() {
        stopGame = 0;
        //set sounds
        crashRing = MediaPlayer.create(MainActivity.this,R.raw.crash);
        coinRing = MediaPlayer.create(MainActivity.this, R.raw.coin);
        loseRing = MediaPlayer.create(MainActivity.this, R.raw.lose);

        distanceCounter = findViewById(R.id.distanceCounter_LBL);

        Bundle b = getIntent().getExtras(); // get data
        isArrowsPassed = b.getBoolean("isArrows");
        userName = b.getString("userName");

        //leaderboard = new Leaderboard();


        car_image_0 = findViewById(R.id.car_image_0);
        car_image_1 = findViewById(R.id.car_image_1);
        car_image_2 = findViewById(R.id.car_image_2);
        car_image_3 = findViewById(R.id.car_image_3);
        car_image_4 = findViewById(R.id.car_image_4);

        // disapear left and right car images
        car_image_0.setVisibility(View.INVISIBLE);
        car_image_1.setVisibility(View.INVISIBLE);
        car_image_2.setVisibility(View.VISIBLE);
        car_image_3.setVisibility(View.INVISIBLE);
        car_image_4.setVisibility(View.INVISIBLE);

        //set buttons
        btnRight = (ImageButton) findViewById(R.id.btn_right);
        btnLeft = (ImageButton) findViewById(R.id.btn_left);

        initSensors();

        System.out.println("Data passed: " + isArrowsPassed);

        if(isArrowsPassed) {
            System.out.println("IN ARROWS");
            // set listeners
            btnRight.setOnClickListener((View v) -> {
                handleRightClick();
            });

            // left btn listener
            btnLeft.setOnClickListener((View v) -> {
                handleLeftClick();
            });
        }
        else { // activate sensors mode
            System.out.println("IN SENSORS");

            removeArrows();



        }

        lifeLast = 3;

        star_3_life = findViewById(R.id.star_3_life);
        star_2_life = findViewById(R.id.star_2_life);
        star_1_life = findViewById(R.id.star_1_life);


    }

    private void setHandlers() {
        handler1 = new Handler();
        runnable1 = new Runnable() {
            @Override
            public void run() {

                if(stopGame == 0){
                    //putFirstLineImg();
                    handler1.postDelayed(this, 700);
                    rocksControl();
                }else
                    handler1.removeCallbacks(runnable1);
            }
        };
        handler1.post(runnable1);
        if(!isArrowsPassed){
            handler2 = new Handler();
            runnable2 = new Runnable() {
                @Override
                public void run() {
                    handleCarMovement(xAxisCurrent);
                    handler2.postDelayed(this,250);
                }
            };

            handler2.post(runnable2);
        }
        if (stopGame == 1){
            //handler1.removeCallbacks(runnable1);
            handler2.removeCallbacks(runnable2);
        }

    }

    private void removeArrows() {
        btnRight.setVisibility(View.INVISIBLE);
        btnLeft.setVisibility(View.INVISIBLE);
    }

    private void initSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    private void handleLeftClick() {
        // case: car is at the left already => cant be double left
        if (carPlaceIndex == 0);

            // case: car is in the left side => move to center
        else {
            switch(carPlaceIndex){
                case 1:
                    car_image_0.setVisibility(View.VISIBLE);
                    car_image_1.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    car_image_1.setVisibility(View.VISIBLE);
                    car_image_2.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    car_image_2.setVisibility(View.VISIBLE);
                    car_image_3.setVisibility(View.INVISIBLE);
                    break;
                case 4:
                    car_image_3.setVisibility(View.VISIBLE);
                    car_image_4.setVisibility(View.INVISIBLE);
                    break;
            }
            carPlaceIndex--;

        }

    }

    private void handleRightClick() {
        // case: car is at the right already => cant be double right
        if (carPlaceIndex == 4);

            // case: car is in the left side => move to center
        else {
            switch(carPlaceIndex){
                case 0:
                    car_image_0.setVisibility(View.INVISIBLE);
                    car_image_1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    car_image_1.setVisibility(View.INVISIBLE);
                    car_image_2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    car_image_2.setVisibility(View.INVISIBLE);
                    car_image_3.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    car_image_3.setVisibility(View.INVISIBLE);
                    car_image_4.setVisibility(View.VISIBLE);
                    break;
            }
            carPlaceIndex++;
        }



    }


}