package com.radiolocus.beaconproberifaceexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.radiolocus.beaconproberiface.BeaconProberMain;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BeaconProberMain bpm = new BeaconProberMain();
            bpm.sendData();
    }
}
