package com.radiolocus.beaconproberifaceexample;

import android.bluetooth.BluetoothManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.radiolocus.beaconproberiface.BeaconProberMain;
import com.radiolocus.beaconproberiface.interfaces.BeaconScannerCallback;
import com.radiolocus.beaconproberiface.utility.AltBeacon;
import com.radiolocus.beaconproberiface.utility.BeaconScanner;

public class MainActivity extends AppCompatActivity implements BeaconScannerCallback {
    BluetoothManager mBluetoothManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothManager = (BluetoothManager) getApplicationContext().getSystemService(getApplicationContext().BLUETOOTH_SERVICE);
        BeaconScanner bpm = new BeaconScanner(getApplicationContext(),this,mBluetoothManager);

        bpm.StartScanning();

    }

    @Override
    public void BeaconDiscovered(AltBeacon altBeacon) {

    }

    @Override
    public void debugData(String s) {

    }
}
