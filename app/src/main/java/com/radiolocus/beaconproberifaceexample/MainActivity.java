package com.radiolocus.beaconproberifaceexample;

import android.bluetooth.BluetoothManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.radiolocus.beaconproberiface.interfaces.RadioLocusBeaconScannerCallback;
import com.radiolocus.beaconproberiface.utility.RlAltBeacon;
import com.radiolocus.beaconproberiface.utility.RadioLocusBeaconScanner;

public class MainActivity extends AppCompatActivity implements   {
    BluetoothManager mBluetoothManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
           // startRadioLocusBeaconScan();

    }


    @Override
    public void startRadioLocusBeaconScan() {
        mBluetoothManager = (BluetoothManager) getApplicationContext().getSystemService(getApplicationContext().BLUETOOTH_SERVICE);
        BeaconScanner bpm = new BeaconScanner(getApplicationContext(),this,mBluetoothManager);
        bpm.StartScanning();
    }

    @Override
    public void stopRadioLocusBeaconScan() {

    }
}
