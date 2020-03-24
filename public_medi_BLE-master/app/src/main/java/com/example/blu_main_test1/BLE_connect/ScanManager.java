package com.example.blu_main_test1.BLE_connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.List;

public class ScanManager {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBLEScanner;
    private boolean mScanning;
    private QuadroUart[] scanBuffer;
    private int scanBufferPoismScanningition = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScanManager()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled())
        {

        }
        else
        {
            if (mBLEScanner == null)
            {

            }
            scanBuffer = new QuadroUart[512];
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean Close()
    {
        try
        {
            if(mScanning)
            {
                mBLEScanner.stopScan(mScanCallback);
            }
            return true;

        }
        catch (NullPointerException e3)
        {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void ScanLeDevice(final boolean enable)
    {
        if (enable)
        {
            mScanning = true;
            mBLEScanner.startScan(mScanCallback);
        }
        else
        {
            mScanning = false;
            mBLEScanner.stopScan(mScanCallback);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private ScanCallback mScanCallback = new ScanCallback()
    {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onScanResult(int callbackType, ScanResult result)
        {
            processResult(result);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBatchScanResults(List<ScanResult> results)
        {
            for (ScanResult result : results)
            {
                processResult(result);
            }
        }
        @Override
        public void onScanFailed(int errorCode)
        {
            Log.d("RRR",String.valueOf(errorCode));
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void processResult(final ScanResult result)
        {
            BluetoothDevice device = result.getDevice();
            String device_address= device.getAddress();

                    QuadroUart choc = new QuadroUart(device,device_address);
                    scanBuffer[scanBufferPoismScanningition] = choc;
                    scanBufferPoismScanningition++;
        }
    };
    public QuadroUart[] GetScanBuffer()
    {
        QuadroUart[] encodedBytes = new QuadroUart[scanBufferPoismScanningition];
        System.arraycopy(scanBuffer, 0, encodedBytes, 0,
                encodedBytes.length);
        scanBufferPoismScanningition = 0;
        return encodedBytes;
    }

    public int CheckScan()
    {
        return scanBufferPoismScanningition;
    }

    public boolean ismScanning() {
        return mScanning;
    }


}
