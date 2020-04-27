/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.blu_main_test1.BLE_SCAN;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blu_main_test1.R;

import java.util.ArrayList;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends ListActivity {
    private LeDeviceListAdapter mLeDeviceListAdapter; //스캔 리스트 어댑터
    private BluetoothAdapter mBluetoothAdapter; //블루투스 어댑터
    private boolean mScanning; //스캐닝 확인 값
    private Handler mHandler; // 핸들러

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000; //스캔주기

    private String mDeviceAddress, mDeviceName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle(R.string.title_devices);
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        //블루투스가 BLE를 지원하는 지 검사하는 로직
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        // 블루투스 어댑터 생성. api 18 이상 가능.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (DeviceControlActivity.mConnected){
            Intent intent = new Intent(getApplicationContext(),DeviceControlActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        //블루투스가 사용가능 상태인지 체크하고 아니라면 그 기능을 키도록함.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); //사용자가 어떤 요청을 보내 왔는지 구분하기 위해
            }                      //첫번째 인자 : 실행할 데이터가 담긴 인텐트 객체, 두 번째 인자는 어떤 요청인지 구별하기 위한 상수.
        }

        // Initializes list view adapter.
        // list view adapter를 생성
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true); //스캔 시작.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { // 블루투스 사용 허용 다이얼로그 결과
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();           //인자 requestCode : startActivityForResult를 호출할 때 넘겨준 요청 상수가 담긴다. 이 값을 통해서 어떤 액티비티에서 온 값인지 판별.
            return;             // resultCode : 창에서 어떤 버튼을 눌렀는지에 대한 결과값이 담김. data: 새로운 창에서 인텐트를 보냈다면 그 인텐트가 이곳에 담김.
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    @Override                // l은 listView객체이고, v는 부모 객체, position은 클릭된 아이템의 위치, id는 그 아이템의 고유 번호.
    protected void onListItemClick(ListView l, View v, int position, long id) { //각 아이템을 클릭했을 때
        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position); // 그 위치의 디바이스
        if (device == null) return;
        final Intent intent = new Intent(this, DeviceControlActivity.class);
        //intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME, device.getName());
       // intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
        mDeviceName = device.getName();
        mDeviceAddress = device.getAddress();
        SharedPreferences autodevice = getSharedPreferences("autodevice", Activity.MODE_PRIVATE);
        SharedPreferences.Editor autoconnect = autodevice.edit();
        autoconnect.putString("address",mDeviceAddress);
        autoconnect.putString("devicename",mDeviceName);
        autoconnect.commit();



        if (mScanning) { //스캔 중지
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }
        startActivity(intent); //DeviceControlActivity 엶
        finish();

    }


    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            //스캔 주기가 지나면 스캔을 그만두게 함.
            mHandler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();  // onCreateOptionsMenu 메소드를 다시 호출해줌.
                }
            }, SCAN_PERIOD);
            // 스캔 시작
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback); //ble장치를 스캔해 그 결과를 mLeScanCallBack 콜백 함수에 넘겨주며 호출한다.
        } else {
            //스캔을 멈춤.
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }
    // Adapter for holding devices found through scanning.
    //스캐닝을 통해 찾은 디바이스를 담을 어댑터.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflater;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflater = DeviceScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();

            // General ListView optimization code.
            if (view == null) {
                view = (LinearLayout) mInflater.inflate(R.layout.listitem_device, null); //기존 : listitem_device.xml
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            //if (deviceName != null && deviceName.length() > 0) {
            if (deviceName != null && deviceName.equals("MEDIPRESSO")) {
                viewHolder.deviceName.setText(deviceName);
                viewHolder.deviceAddress.setText(device.getAddress().substring(12, 17));
            }
            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = //새로운 장치가 발견될때마다 onLeScan을 호출.
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLeDeviceListAdapter.addDevice(device);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }
}
