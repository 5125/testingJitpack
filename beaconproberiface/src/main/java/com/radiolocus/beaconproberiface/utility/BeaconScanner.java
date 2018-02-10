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
    String[] blue1=new String[100];
    String[] blue2=new String[100];
    String[] blue3=new String[100];
    private final String TAG = "BeaconScanner";
    private final BeaconScanner that = this;
    private final BeaconScannerCallback mCallback;
    private final BluetoothAdapter mBluetoothAdapter;
    private final Handler mHandler;
    private static Context ctx;
    private static int device1Count=0;
    private static int volleyCounter=0;
    StringBuffer sb=new StringBuffer();

    String testName = "rltest123";

    private static int device2Count=0;
    private static int device3Count=0;
    private static int device4Count=0;
    private static int device5Count=0;
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
        ctx=Context;
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
                        that.mCallback.debugData("SCANNER reTry");
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
                that.mCallback.BeaconDiscovered(AltBeaconFactory.getBeaconFromScanrecord(device, scanRecord, rssi));
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

                String s=result.toString();
                Log.v("ScanScan",result.toString());
                int rssid=result.getRssi();
                // double getTxPower=result.getTxPower();
                Date dt = new Date();
                long timeStamp = dt.getTime();
               // BeaconActivity ba=new BeaconActivity();

                String mDevice=result.getDevice().toString();
                AndroidDeviceOps ado = new AndroidDeviceOps();
                String macID=ado.getMacAddr();
                String androidID=getAndroidId();
                String data=testName+","+macID+","+androidID+","+timeStamp+","+mDevice+","+rssid;
                Log.v("=========>",data);
                BeaconProberMain ba = new BeaconProberMain();
                ba.sendData(data,ctx);

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
                        that.mCallback.debugData("onScanFailed : " + errorCodeTmp);
                    }
                });
                Log.i(TAG, "onScanFailed : " + errorCode);
                reStartScanning();
            }
        };
    }

    public String getAndroidId() {
        String androidId = Settings.System.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;

    }


}
