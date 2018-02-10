package com.radiolocus.beaconproberifaceexample;

import android.bluetooth.BluetoothManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.radiolocus.beaconproberiface.interfaces.RadioLocusBeaconScannerCallback;
import com.radiolocus.beaconproberiface.utility.RlAltBeacon;
import com.radiolocus.beaconproberiface.utility.RadioLocusBeaconScanner;

public class MainActivity extends AppCompatActivity
            implements RadioLocusBeaconScannerCallback  {
    BluetoothManager mBluetoothManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    @Override
    public void startRadioLocusBeaconScan() {

    }

    @Override
    public void stopRadioLocusBeaconScan() {

    }
}
