package com.example.blu_main_test1.BLE_connect;

import android.bluetooth.BluetoothDevice;

public class QuadroUart {

    private BluetoothDevice device;
    private String uuid;
    private int scanCount;

    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public BluetoothDevice getDevice()
    {
        return device;
    }
    public void setDevice(BluetoothDevice device)
    {
        this.device = device;
    }

    public int getScanCount() {
        return scanCount;
    }
    public void setScanCount(int scanCount) {
        this.scanCount = scanCount;
    }

    public void CountScanCount()
    {
        scanCount += 1;
    }

    public QuadroUart(BluetoothDevice device, String uuid) {
        this.device = device;
        this.uuid = uuid;
        scanCount = 0;
    }

}
