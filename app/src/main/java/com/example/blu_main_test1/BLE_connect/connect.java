package com.example.blu_main_test1.BLE_connect;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blu_main_test1.Main_page.MainActivity;
import com.example.blu_main_test1.Main_page.Main_page2.product_amount;
import com.example.blu_main_test1.Main_page.Main_view_pager;
import com.example.blu_main_test1.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class connect extends AppCompatActivity {

    QuadroListViewAdapter la;
    private ListView listView;
    private TextView nameView;
    private Button btnConnect;
    private Button btnDelete;
    private String uuid;
    private String name;
    Context activityContext;
    private readTask task;
    private File file;
    private connect thisActivity;
    ScanManager m_Scan;
    UartService m_UartService;
    public static boolean IsConnect=false;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        listView = findViewById(R.id.list);
        nameView = findViewById(R.id.name);
        btnConnect = findViewById(R.id.btnConnect);
        btnDelete = findViewById(R.id.btnDelete);

        uuid = "";
        name = "";
        activityContext = this;

        task = new readTask();


           task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        file = new File(getFilesDir(), "uuiddata.dat");
        if(file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                int i = 0;
                while (true) {
                    String line = br.readLine();
                    if (line == null) break;
                    if(!line.equals("")) {
                        if(i == 0) uuid = line;
                        else if(i == 1) name = line;
                        else break;
                        i++;
                    }
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        la = new QuadroListViewAdapter(activityContext,R.layout.connect_text,new ArrayList<QuadroUart>());
        listView.setAdapter(la);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adpterView, View view, int position, long id) {
                selectDevice(position);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSavedDevice();
            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<QuadroUart> qu = la.getData();

                if(IsConnect==true)
                {
                    Toast.makeText(connect.this,"이미 연결중입니다.", Toast.LENGTH_SHORT).show();
                    return ;
                }
               else
                IsConnect = false;


               for(int i = 0; i < qu.size();i++)
                {
                    if(qu.get(i).getUuid().equals(uuid))
                    {
                        String add = qu.get(i).getDevice().getAddress();
                        IsConnect = m_UartService.connect(add);
                        break;
                    }
                }
                if(IsConnect)
                {
                    Intent intent = new Intent(getApplicationContext(), Main_view_pager.class);
                    intent.putExtra("name",nameView.getText());
                    startActivity(intent);


                }
            }
        });
        nameView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    name = nameView.getText().toString();
                    try {
                        FileWriter fw = new FileWriter(file);
                        fw.write(uuid);
                        fw.append("\r\n");
                        fw.append(name);
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    nameView.clearFocus();
                    InputMethodManager inputManager =
                            (InputMethodManager) activityContext.
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            new View(thisActivity).getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE를 지원하지 않는 기종입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck== PackageManager.PERMISSION_DENIED)
        {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    200);
        }else{
            service_init();
            m_Scan = new ScanManager();
            m_Scan.ScanLeDevice(true);
        }
        uiChange(-1);

    }


    private void selectDevice(int po)
    {
        String newUuid = ((QuadroListViewAdapter) listView.getAdapter()).getItem(po);
        nameView.setText(newUuid);
        uuid = newUuid;
        name = newUuid;
        btnConnect.setEnabled(true);
        btnDelete.setEnabled(true);
        try {
            FileWriter fw = new FileWriter(file);
            String data = newUuid;
            fw.write(data);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteSavedDevice()
    {
        nameView.setText(" ");
        uuid = "";
        name = "";
        btnConnect.setEnabled(false);
        btnDelete.setEnabled(false);
        try {
            FileWriter fw = new FileWriter(file);
            String data = "";
            fw.write(data);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //비동기 실행
    private class readTask extends AsyncTask<Void, Integer, Boolean>
    {
        readTask()
        {

        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            int scanSecondCount = 0;
            while (!isCancelled()) {
                if (m_Scan != null) {
                    if (m_Scan.ismScanning()) {
                        scanSecondCount++;
                        if (scanSecondCount >= 10) {
                            ArrayList<QuadroUart> currentChocList = la.getData();
                            for (int i = 0; i < currentChocList.size(); i++) {
                                if (uuid.equals(la.getItem(i))) {
                                    if (currentChocList.get(i).getScanCount() == 0) {
                                        publishProgress(-2);
                                    }
                                }
                            }
                            la.UpdateList(1);
                            publishProgress(-1);
                            scanSecondCount = 0;
                        }
                        ArrayList<QuadroUart> currentChocList = la.getData();
                        if (m_Scan.CheckScan() != 0) {
                            QuadroUart[] readChocArray = m_Scan.GetScanBuffer();
                            if (readChocArray.length > 0) {
                                for (int i = 0; i < readChocArray.length; i++) {
                                    boolean alreadyIncluded = false;
                                    int deviceNo = -1;
                                    for (int j = 0; j < currentChocList.size(); j++) {
                                        if (currentChocList.get(j).getUuid().equals(readChocArray[i].getUuid())) {
                                            currentChocList.get(j).CountScanCount();
                                            alreadyIncluded = true;
                                            deviceNo = j;
                                            break;
                                        }
                                    }
                                    if (!alreadyIncluded) {
                                        currentChocList.add(readChocArray[i]);
                                        deviceNo = currentChocList.size() - 1;
                                        currentChocList.get(deviceNo).CountScanCount();
                                        publishProgress(-1);
                                    }
                                }
                            }
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
            return true;
        }
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            for(int i = 0;i < values.length;i++)
                uiChange(values[i]);
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(Boolean isSucess)
        {
            super.onPostExecute(isSucess);

            if (!isSucess)
            {
                m_Scan.Close();
            }
        }
    }

    private void uiChange(int i)
    {
        la.notifyDataSetChanged();
        //listView.setAdapter(la);

        if(name.equals(""))
        {
            nameView.setText(uuid);
        }
        else
        {
            nameView.setText(name);
        }
        btnDelete.setEnabled(!uuid.equals(""));
        btnConnect.setEnabled(!uuid.equals(""));
    }

    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            m_UartService = ((UartService.LocalBinder) rawBinder).getService();

            //Log.d(TAG, "onServiceConnected m_UartService= " + m_UartService);
            if (!m_UartService.initialize()) {
                //Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void onServiceDisconnected(ComponentName classname) {
            if(m_UartService != null) {
                //m_UartService.disconnect(m_Device);
                m_UartService.disconnect();
                //m_UartService = null;
            }
            //m_UartService = null;
        }
    };

    private void service_init() {
        Intent bindIntent = new Intent(connect.this, UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }


    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            final Intent mIntent = intent;
            //*********************//
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
                        Calendar cal = Calendar.getInstance();
                        String time_str = dateFormat.format(cal.getTime());
                        /*TestActivity TA=new TestActivity();
                        TA.connect_result.setText("연결됨");*/

                        MainActivity.connect_result.setText("연결됨");
                        Main_view_pager.draw_connect.setText("연결됨");

                        //Log.d(TAG, "ACTION_GATT_CONNECTED " + time_str);
/*
                         listAdapter.add("["+currentDateTimeString+"] Connected to: "+ m_Device.getName());
                         messageListView.smoothScrollToPosition(listAdapter.getCount() - 1);
*/

                        //연결 완료  - 맥어드레스 저장
                        //업데이트
                        //SharedPreferences.Editor editor = pref.edit();
                        //editor.putString("MacAddr",m_Device.getAddress());
                        //editor.commit();

                        //m_State = UART_PROFILE_CONNECTED;
                    }
                });

            }

            //*********************//
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
                        //Log.d(TAG, "ACTION_GATT_DISCONNECTED");
                        btnConnect.setText("Connect");
                      /*  TestActivity TA=new TestActivity();
                        TA.connect_result.setText("연결안됨");*/
                        MainActivity.connect_result.setText("연결안됨");
                        Main_view_pager.draw_connect.setText("연결안됨");
/*
                             listAdapter.add("["+currentDateTimeString+"] Disconnected to: "+ m_Device.getName());
*/
                        //m_State = UART_PROFILE_DISCONNECTED;
                        m_UartService.close();
                    }
                });
            }


            //*********************//
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                m_UartService.enableTXNotification();
                //Log.d(TAG, "ACTION_GATT_SERVICES_DISCOVERED");
            }

            //-----------------------------------------------------
            // HERE RECEIVE RAW BLE DATA AND PARSE
            //-----------------------------------------------------

            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {

            }
            //*********************//
            if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)){
                //showMessage("Device doesn't support UART. Disconnecting");
                m_UartService.disconnect();
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);

        //      gap messages
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        //intentFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);

        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

        return intentFilter;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.d(TAG, "onDestroy()");

        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore) {
            //Log.e(TAG, ignore.toString());
        }
        if (m_UartService != null) {
            unbindService(mServiceConnection);
            m_UartService.stopSelf();
            m_UartService = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
     //   new readTask().execute();
        if(m_Scan != null)
        {
            m_Scan.ScanLeDevice(true);
        }
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    service_init();
                    m_Scan = new ScanManager();
                    m_Scan.ScanLeDevice(true);

                } else {

                    Toast.makeText(this,">BLE 기능을 사용하기 위해 위치 정보가 필요합니다", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }


}
