package com.radiolocus.beaconproberiface.interfaces;

import com.radiolocus.beaconproberiface.utility.AltBeacon;

/**
 * Created by root on 10/2/18.
 */

public interface BeaconScannerCallback {

        void BeaconDiscovered(AltBeacon beacon);
        void debugData(String data);

}
