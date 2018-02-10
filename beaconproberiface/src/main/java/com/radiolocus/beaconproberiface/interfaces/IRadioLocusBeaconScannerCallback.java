package com.radiolocus.beaconproberiface.interfaces;

/**
 * Created by root on 10/2/18.
 */

public interface IRadioLocusBeaconScannerCallback {

        void startRadioLocusBeaconScan();
        void stopRadioLocusBeaconScan();
        void restartRadioLocusBeaconScan();
}
