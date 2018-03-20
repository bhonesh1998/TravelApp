package com.example.bhonesh.railwayapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread background = new Thread() {
            public void run() {

                try {

                    sleep(2000);


                    Intent intent = new Intent(MainActivity.this, Display.class);
                    startActivity(intent);

                    finish();

                } catch (Exception e) {

                }
            }
        };


        background.start();
    }
}
