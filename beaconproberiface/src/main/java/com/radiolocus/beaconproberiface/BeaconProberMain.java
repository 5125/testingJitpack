package com.radiolocus.beaconproberiface;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.radiolocus.beaconproberiface.interfaces.BeaconScannerCallback;
import com.radiolocus.beaconproberiface.utility.AltBeacon;
import com.radiolocus.beaconproberiface.utility.BeaconScanner;
/**
 * Created by root on 10/2/18.
 */

public class BeaconProberMain  extends
        Application implements BeaconScannerCallback {

    private final String TAG = "BeaconScannerView";
    private BluetoothManager mBluetoothManager = null;
    private BeaconScanner mBeaconScanner = null;
    private final String TUPLES="MAXTUPLES";
    private static String macAddress;
    private static String androidId;
    private static int tupleCount=0;
    private static Context mContext;
    private static final String urlVal = "http://test.radiolocus.com:8000";
    private RequestQueue queue;
    Activity act;

    private void StopScanner() {
        BeaconScanner tmpScanner = mBeaconScanner;
        mBeaconScanner = null;
        if(tmpScanner != null){
            tmpScanner.Stop();
        }


    }

    private void StartScanner() {

        BeaconScanner tmpBeaconScanner = new BeaconScanner(getApplicationContext(), this, this.mBluetoothManager);
        tmpBeaconScanner.Start();
        mBeaconScanner = tmpBeaconScanner;
    }


    public void sendData(String res, Context ctx) {
        final String blueTuple =res;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlVal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rltest123", blueTuple);
                return params;
            }

        };
        queue = Volley.newRequestQueue(ctx);
        queue.add(stringRequest);
        //Log.d(TAG, r/es);

    }


    @Override
    public void BeaconDiscovered(AltBeacon beacon) {

    }

    @Override
    public void debugData(String data) {

    }
}
