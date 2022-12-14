package com.root.greenwater;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Set;
import java.util.UUID;

public class Settings extends Fragment {

    private View view;
    private FirebaseAuth mFirebaseAuth;
    private Button mBtnBluetoothOn;
    private Button mBtnBluetoothOff;
    private Button mListPairedDevicesBtn;
    private Button mDiscoverBtn;
    private TextView mBluetoothStatus;
    private ListView mDevicesListView;
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;
    private Switch mSwitch;
    public static int alerFlag = 1;

    private Object mLock;
    private Thread t;
    private Handler mHandler; // Our main handler that will receive callback notifications
    private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        view = inflater.inflate(R.layout.settings, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 강제 비활성화

        mFirebaseAuth = FirebaseAuth.getInstance();
        mBtnBluetoothOn = view.findViewById(R.id.btnBluetooth_on);
        mBtnBluetoothOff = view.findViewById(R.id.btnBluetooth_off);
        mDiscoverBtn = view.findViewById(R.id.btn_discover);
        mListPairedDevicesBtn = view.findViewById(R.id.btn_pairedList);
        mBluetoothStatus = view.findViewById(R.id.bluetoothStatus);
        mSwitch = view.findViewById(R.id.alert_switch);

        mBTArrayAdapter = new ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio

        mDevicesListView = view.findViewById(R.id.devicesListView);
        mDevicesListView.setAdapter(mBTArrayAdapter); // assign model to view
        mDevicesListView.setOnItemClickListener(mDeviceClickListener);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사
                if (isChecked){
                    mSwitch.setText("알림 ON");
                    alerFlag = 1;
                }else{
                    mSwitch.setText("알림 OFF");
                    alerFlag = 0;
                    // 알림 해제
                }
            }
        });

        mLock = new Object();

        mHandler = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what == MESSAGE_READ){
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    }
                    catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Wateringplant.displayReceivedData(readMessage);
                }
                if(msg.what == CONNECTING_STATUS){
                    if(msg.arg1 == 1)
                        mBluetoothStatus.setText("연결된 장치 : " + (String)(msg.obj));
                    else
                        mBluetoothStatus.setText("연결 실패");
                }
            }
        };

        if (mBTArrayAdapter == null) {
            mBluetoothStatus.setText("블루투스 기기 검색 실패");
            Toast.makeText(requireContext(),"블루투스 기기를 찾지 못했습니다.",Toast.LENGTH_SHORT).show();
        }
        else {
            mBtnBluetoothOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bluetoothOn(v);
                }
            });

            mBtnBluetoothOff.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) { bluetoothOff(v); }
            });

            mListPairedDevicesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    listPairedDevices(v);
                }
            });

            mDiscoverBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    discover(v);
                }
            });
        }

        // 로그아웃 버튼 액션
        Button btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 로그아웃 하기
                mFirebaseAuth.signOut();
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                Toast.makeText(requireActivity(),"로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
                Plantlist.arrayList.clear();
                startActivity(intent);
            }
        });

        return view;
    }
    // 블루투스 켜기
    private void bluetoothOn(View view){
        if (!mBTAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            mBluetoothStatus.setText("블루투스 ON");
            Toast.makeText(requireContext(),"블루투스를 켭니다.",Toast.LENGTH_SHORT).show();
        }
        else{
            mBluetoothStatus.setText("블루투스 ON");
            Toast.makeText(requireContext(),"블루투스가 이미 켜져 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    // 블루투스 끄기
    private void bluetoothOff(View view){
        mBTAdapter.disable(); // turn off
        mBluetoothStatus.setText("블루투스 OFF");
        Toast.makeText(requireContext(),"블루투스를 껐습니다.", Toast.LENGTH_SHORT).show();
    }

    // 블루투스 검색
    private void discover(View view){
        // Check if the device is already discovering
        if(mBTAdapter.isDiscovering()){
            mBTAdapter.cancelDiscovery();
            mBluetoothStatus.setText("블루투스 검색 중지");
            Toast.makeText(requireContext(),"기기 검색을 중단했습니다.",Toast.LENGTH_SHORT).show();
        }
        else{
            if(mBTAdapter.isEnabled()) {
                mBTArrayAdapter.clear(); // clear items
                mBTAdapter.startDiscovery();
                mBluetoothStatus.setText("블루투스 검색중...");
                Toast.makeText(requireContext(), "기기 검색을 시작합니다.", Toast.LENGTH_SHORT).show();
                requireActivity().registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
            else{
                Toast.makeText(requireContext(), "블루투스가 켜져 있지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 페어링 기기 목록 불러오기
    private void listPairedDevices(View view){
        mPairedDevices = mBTAdapter.getBondedDevices();
        if(mBTAdapter.isEnabled()) {
            // put it's one to the adapter
            mBTArrayAdapter.clear(); // clear items
            for (BluetoothDevice device : mPairedDevices)
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            mBluetoothStatus.setText("기기 목록 로드");
            Toast.makeText(requireContext(), "페어링 된 기기 목록을 불러왔습니다.", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(requireContext(), "블루투스가 켜져 있지 않습니다.", Toast.LENGTH_SHORT).show();
    }

    // 블루투스 어댑터 정보를 가져오기
    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            if(!mBTAdapter.isEnabled()) {
                Toast.makeText(getActivity().getBaseContext(), "블루투스가 켜져있지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            mBluetoothStatus.setText("연결중...");
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0,info.length() - 17);

            // Spawn a new thread to avoid blocking the GUI one
            t = new Thread()
            {
                public void run() {
                    boolean fail = false;

                    BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                    try {
                        mBTSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getActivity().getBaseContext(), "소켓 생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                    // Establish the Bluetooth socket connection.
                    try {
                        mBTSocket.connect();
                    } catch (IOException e) {
                        try {
                            fail = true;
                            mBTSocket.close();
                            mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                    .sendToTarget();
                        } catch (IOException e2) {
                            //insert code to deal with this
                            Toast.makeText(getActivity().getBaseContext(), "소켓 생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(fail == false) {
                        mConnectedThread = new ConnectedThread(mBTSocket);
                        mConnectedThread.start();

                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                                .sendToTarget();
                    }
                }
            };
            t.start();
        }
    };

    @Override
    public void onDestroy() {
        Log.d("settings", "onDestroy!!");
        super.onDestroy();
        if(t != null) {
            t.interrupt();
        }
        if(mConnectedThread != null) {
            synchronized (mLock) {
                mConnectedThread.interrupt();
                Wateringplant.count = 0;
            }
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (!Thread.currentThread().isInterrupted()) {
                synchronized (mLock) {
                    try {
                        // Read from the InputStream
                        bytes = mmInStream.available();
                        if(bytes != 0) {
                            SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                            bytes = mmInStream.available(); // how many bytes are ready to be read?
                            bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                            mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                    .sendToTarget(); // Send the obtained bytes to the UI activity
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                        break;
                    }
                }
            }
        }
    }
}
