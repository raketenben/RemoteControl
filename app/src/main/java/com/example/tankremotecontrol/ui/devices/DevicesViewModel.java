package com.example.tankremotecontrol.ui.devices;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DevicesViewModel extends ViewModel {

    private MutableLiveData<ArrayList<BluetoothDevice>> bluetoothDeviceList;

    public DevicesViewModel() {
        bluetoothDeviceList = new MutableLiveData<>();

        bluetoothDeviceList.setValue(new ArrayList<BluetoothDevice>());

        updateDeviceList();
    }

    private void updateDeviceList(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        List<String> s = new ArrayList<String>();
        for(BluetoothDevice bt : pairedDevices)
            bluetoothDeviceList.getValue().add(bt);

        //setListAdapter(new ArrayAdapter<String>(this, R.layout.list, s));
    }

    public LiveData<ArrayList<BluetoothDevice>> getBluetoothDevices() { return bluetoothDeviceList; }
}