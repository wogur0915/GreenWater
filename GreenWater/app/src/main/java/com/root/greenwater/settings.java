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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Set;
import java.util.UUID;

public class settings extends Fragment {

    private View view;
    private FirebaseAuth mFirebaseAuth;
    private Button mBtnBluetoothOn;
    private Button mBtnBluetoothOff;
    private Button mListPairedDevicesBtn;
    private Button mDiscoverBtn;
    private ListView mDevicesListView;
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;

    private Handler mHandler; // Our main handler that will receive callback notifications
//    private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // 다크모드 강제 비활성화

        mFirebaseAuth = FirebaseAuth.getInstance();
        mBtnBluetoothOn = view.findViewById(R.id.btnBluetooth_on);
        mBtnBluetoothOff = view.findViewById(R.id.btnBluetooth_off);
        mDiscoverBtn = view.findViewById(R.id.btn_discover);
        mListPairedDevicesBtn = view.findViewById(R.id.btn_pairedList);

        mBTArrayAdapter = new ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio

        mDevicesListView = view.findViewById(R.id.devicesListView);
        mDevicesListView.setAdapter(mBTArrayAdapter); // assign model to view
//        mDevicesListView.setOnItemClickListener(mDeviceClickListener);

        if (mBTArrayAdapter == null) {
            Toast.makeText(requireContext(),"Bluetooth device not found!",Toast.LENGTH_SHORT).show();
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
//
//            mListPairedDevicesBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v){
//                    listPairedDevices(v);
//                }
//            });
//
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
                requireActivity().finish();
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
            Toast.makeText(requireContext(),"블루투스를 켭니다.",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(requireContext(),"블루투스가 이미 켜져 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    // 블루투스 끄기
    private void bluetoothOff(View view){
        mBTAdapter.disable(); // turn off
        Toast.makeText(requireContext(),"블루투스를 껐습니다.", Toast.LENGTH_SHORT).show();
    }

    // 블루투스 페어링
    private void discover(View view){
        // Check if the device is already discovering
        if(mBTAdapter.isDiscovering()){
            mBTAdapter.cancelDiscovery();
            Toast.makeText(requireContext(),"Discovery stopped",Toast.LENGTH_SHORT).show();
        }
        else{
            if(mBTAdapter.isEnabled()) {
                mBTArrayAdapter.clear(); // clear items
                mBTAdapter.startDiscovery();
                Toast.makeText(requireContext(), "기기 검색을 시작합니다.", Toast.LENGTH_SHORT).show();
                requireActivity().registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
            else{
                Toast.makeText(requireContext(), "블루투스가 켜져있지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
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

}
