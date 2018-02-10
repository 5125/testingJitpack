package com.radiolocus.beaconproberiface.utility;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.provider.Settings;
import android.util.Log;
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
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Build;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.radiolocus.beaconproberiface.BeaconProberMain;
import com.radiolocus.beaconproberiface.androidcore.AndroidDeviceOps;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.radiolocus.beaconproberiface.interfaces.BeaconScannerCallback;

/**
 * Created by root on 10/2/18.
 */


@TargetApi(18)
@SuppressLint("NewApi")

public class BeaconScanner {

    // variables for tupleInstance.
    private final String TAG = "BeaconScanner";
    private final BeaconScanner that = this;
    private final BeaconScannerCallback mCallback;
    private final BluetoothAdapter mBluetoothAdapter;
    private final Handler mHandler;
    private static final String urlVal = "http://test.radiolocus.com:8000";
    private RequestQueue queue;
    private static Context mCtxScanner;
    String mDevice, macID, androidID, beaconTupleInstance;
    long currentTimeStamp;
    String testName = "rltest123";
    int rssid=0;
    //lollipop specific stuff
    private final BluetoothLeScanner scanner;
    private final CountDownTimer reTryStartScanningTimer = new CountDownTimer(5000, 1000) {
        public void onTick(long millisUntilFinished) {}
        public void onFinish () {
            Log.i(TAG, "re-try starting scanning");
            StartScanning();
        }
    };

    public BeaconScanner(Context Context, BeaconScannerCallback CallBack, BluetoothManager Manager) {
        this.mCallback = CallBack;
        this.mBluetoothAdapter = Manager.getAdapter();
        this.mHandler = new Handler(Context.getMainLooper());
        mCtxScanner=Context;
        //With lollipop & newer we can use the new API
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.scanner = this.mBluetoothAdapter.getBluetoothLeScanner();
        }else{
            this.scanner = null;
        }
    }

    public void Start() {
        Stop();
        StartScanning();
    }

    public void StartScanning() {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (that.scanner != null) {
                    Log.i(TAG, "Start Lollipop scanner now");
                    ScanSettings settings = new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                            .build();

                    List<ScanFilter> filters = new ArrayList<ScanFilter>();
                    doSetScanCallback();
                    scanner.startScan(filters, settings, mScanCallback);
                } else { //KitKat
                    boolean retValue = that.mBluetoothAdapter.startLeScan(that.leScanCallback);
                    Log.i(TAG, "start KitKat scanner now : " + retValue);
                    if (!retValue) {
                        //that.mCallback.debugData("SCANNER reTry");
                        reTryStartScanningTimer.start();
                    }
                }
            }
        });
    }

    public void Stop() {
        Log.i(TAG, "stop now");
        reTryStartScanningTimer.cancel();
        if (that.scanner != null) {
            scanner.stopScan(mScanCallback);
        } else { //KitKat
            mBluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    public void reStartScanning() {
        Log.i(TAG, "reStartScanning now");
        Stop();
        StartScanning();
    }




    private void foundBeacon(final BluetoothDevice device, final int rssi, final byte[] scanRecord){

        if (device == null || scanRecord == null) {
            return;
        }

        //not ours (length is not enough, or type byte is wrong)
        if (!AltBeaconFactory.isLengthAndTypeOk(scanRecord)) {
            return;
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
               // that.mCallback.BeaconDiscovered(AltBeaconFactory.getBeaconFromScanrecord(device, scanRecord, rssi));
            }
        });
    }

    private final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            that.foundBeacon(device, rssi, scanRecord);
        }
    };

    ScanCallback mScanCallback = null;





    private void doSetScanCallback() {
        mScanCallback = new ScanCallback() {
            public void onScanResult(int callbackType, ScanResult result) {


                Log.v("ScanScan",result.toString());
                rssid=result.getRssi();
                // double getTxPower=result.getTxPower();
                Date dt = new Date();
                currentTimeStamp = dt.getTime();
               // BeaconActivity ba=new BeaconActivity();

                mDevice=result.getDevice().toString();
                macID = AndroidDeviceOps.getMacAddr();
                androidID=getAndroidId();
                beaconTupleInstance="rlScannerInstance: [ "+testName+ ","+macID+ ","+ androidID+ ","+currentTimeStamp+ ","+mDevice+ ","+rssid+ "]";
                Log.v("=========>",beaconTupleInstance);


                sendTuplesVolley(beaconTupleInstance,mCtxScanner);

                if (result !=null && result.getScanRecord() != null) {
                    that.foundBeacon(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes());
                }
            }

            public void onBatchScanResults(List<ScanResult> results) {


                for (ScanResult result : results) {
                    if (result != null && result.getScanRecord() != null) {
                        that.foundBeacon(result.getDevice(), result.getRssi(), result.getScanRecord().getBytes());
                    }
                }
            }

            public void onScanFailed(int errorCode) {

                final int errorCodeTmp = errorCode;

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                      //  that.mCallback.debugData("onScanFailed : " + errorCodeTmp);
                    }
                });
                Log.i(TAG, "onScanFailed : " + errorCode);
                reStartScanning();
            }
        };
    }

    public String getAndroidId() {
        String androidId = Settings.System.getString(mCtxScanner.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;

    }

    private void sendTuplesVolley(String res, Context ctx) {
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

}
