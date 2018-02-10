package com.radiolocus.beaconproberifaceexample;

import android.bluetooth.BluetoothManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.radiolocus.beaconproberiface.interfaces.IRadioLocusBeaconScannerCallback;
import com.radiolocus.beaconproberiface.utility.RlAltBeacon;
import com.radiolocus.beaconproberiface.utility.RadioLocusBeaconScanner;

public class MainActivity extends AppCompatActivity
            implements IRadioLocusBeaconScannerCallback  {
    BluetoothManager mBluetoothManager = null;
    RadioLocusBeaconScanner bpm = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothManager = (BluetoothManager) getApplicationContext().getSystemService(getApplicationContext().BLUETOOTH_SERVICE);
        bpm = new RadioLocusBeaconScanner(getApplicationContext(),this,mBluetoothManager);
        startRadioLocusBeaconScan();
    }


    @Override
    public void startRadioLocusBeaconScan() {

        bpm.StartScanning();
      }

    @Override
    public void stopRadioLocusBeaconScan() {
       // RadioLocusBeaconScanner bpm = new RadioLocusBeaconScanner(getApplicationContext(),this,mBluetoothManager);
        bpm.Stop();

    }

    @Override
    public void restartRadioLocusBeaconScan() {
        //RadioLocusBeaconScanner bpm = new RadioLocusBeaconScanner(getApplicationContext(),this,mBluetoothManager);
        bpm.reStartScanning();

    }
}
