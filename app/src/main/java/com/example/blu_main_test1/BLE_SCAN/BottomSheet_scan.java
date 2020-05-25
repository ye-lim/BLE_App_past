package com.example.blu_main_test1.BLE_SCAN;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.blu_main_test1.R;
import com.example.blu_main_test1.main_before.Machine_main;

import java.util.ArrayList;
import java.util.List;


public class BottomSheet_scan extends BottomSheetDialogFragment {
    private ListView ble_scan_item;
    private LeDeviceListAdapter mLeDeviceListAdapter; //스캔 리스트 어댑터
    private BluetoothAdapter mBluetoothAdapter; //블루투스 어댑터
    private boolean mScanning; //스캐닝 확인 값
    private Handler mHandler; // 핸들러
    private String mDeviceAddress, mDeviceName;
    private TextView textView;
    private ImageView start_device_scanning;
    private Context context = null;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 5000; //스캔주기

    public BottomSheet_scan() {

    }
    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        Button scan = (Button) getView().findViewById(R.id.menu_scan);
        ble_scan_item = (ListView)getView().findViewById(R.id.ble_scan_item);
        textView = (TextView)getView().findViewById(R.id.find_text);
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        ble_scan_item.setAdapter(mLeDeviceListAdapter);

        ble_scan_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position); // 그 위치의 디바이스
                if (device == null) return;
                final Intent intent = new Intent(getContext(), DeviceControlActivity.class);
                mDeviceName = device.getName();
                mDeviceAddress = device.getAddress();
                SharedPreferences autodevice = getContext().getSharedPreferences("autodevice", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autoconnect = autodevice.edit();
                autoconnect.putString("address",mDeviceAddress);
                autoconnect.putString("devicename",mDeviceName);
                autoconnect.commit();

                if (mScanning) { //스캔 중지
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }
                startActivity(intent);
                getActivity().finish();
            }
        });


        mHandler = new Handler();

        //블루투스가 BLE를 지원하는 지 검사하는 로직
        if (!getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getContext(), R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        // 블루투스 어댑터 생성. api 18 이상 가능.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);

        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(getContext(), R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }

        if (DeviceControlActivity.mConnected){
            Intent intent = new Intent(getContext(),DeviceControlActivity.class);
            startActivity(intent);
        }
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanLeDevice(false);
                dismiss();
                //scanLeDevice(false);
            }
        });

        start_device_scanning = (ImageView)getView().findViewById(R.id.bluetooth_finding);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(start_device_scanning);
        Glide.with(this).load(R.raw.bluetooth_finding).into(gifImage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device) && device.getName() != null){
                if (device.getName().equals("MEDIPRESSO")) {
                    mLeDevices.add(device);
                }
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
            View v = View.inflate(getContext(),R.layout.listitem_device,null);

            BluetoothDevice device = mLeDevices.get(i);

            TextView deviceAddress = v.findViewById(R.id.device_address);
            TextView deviceName = v.findViewById(R.id.device_name);
            deviceName.setText(device.getName());
            deviceAddress.setText(device.getAddress().substring(12,17));

            return v;
        }
    }


    @Override
    public void onResume() {
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
        scanLeDevice(true); //start scanning
    }

    @Override
    public void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }



    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            //스캔 주기가 지나면 스캔을 그만두게 함.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    if(getContext() != null)
                        Glide.with(getContext()).load(R.drawable.ble_mark).into(start_device_scanning);
                        textView.setText("사용하실 머신을 눌러주세요.");


                    if(mLeDeviceListAdapter.getCount() == 0 && getContext() != null){
                        Toast.makeText(getContext(), "연결 가능한 머신이 없습니다.", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    if(mLeDeviceListAdapter.getCount() == 1 && getContext() != null){
                        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(0); // 그 위치의 디바이스
                        final Intent intent = new Intent(getContext(), DeviceControlActivity.class);
                        mDeviceName = device.getName();
                        mDeviceAddress = device.getAddress();
                        SharedPreferences autodevice = getContext().getSharedPreferences("autodevice", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoconnect = autodevice.edit();
                        autoconnect.putString("address",mDeviceAddress);
                        autoconnect.putString("devicename",mDeviceName);
                        autoconnect.commit();
                        startActivity(intent);
                        getActivity().finish();
                    }

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
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = //새로운 장치가 발견될때마다 onLeScan을 호출.
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();


                }
            };


}

