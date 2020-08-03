package com.example.tankremotecontrol.ui.devices;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.tankremotecontrol.MainActivity;
import com.example.tankremotecontrol.R;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class DevicesFragment extends Fragment {

    private DevicesViewModel devicesViewModel;
    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        devicesViewModel = ViewModelProviders.of(this).get(DevicesViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        final MainActivity appState = ((MainActivity) getParentFragment().getActivity());

        final LinearLayout listLayout = root.findViewById(R.id.device_list);
        devicesViewModel.getBluetoothDevices().observe(getViewLifecycleOwner(), new Observer<ArrayList<BluetoothDevice>>() {

            @Override
            public void onChanged(ArrayList<BluetoothDevice> devices) {

                for (final BluetoothDevice device : devices){
                    LinearLayout split = new LinearLayout(root.getContext());
                    split.setOrientation(LinearLayout.HORIZONTAL);

                    final Button button = new Button(root.getContext());
                    button.setText("Connect");

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            button.setText("PENDING");
                            BluetoothSocket bts;
                            try {
                                appState.setBluetoothSocket((BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1));
                                bluetoothAdapter.cancelDiscovery();
                                appState.getBluetoothSocket().connect();
                                button.setText("TRENNEN");

                                appState.getBluetoothSocket().getOutputStream().write(100);
                                appState.getNavController().navigate(R.id.navigation_dashboard);

                            } catch (IOException | NoSuchMethodException e) {
                                button.setText("FAILED");
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    if(appState.getBluetoothSocket() != null){
                        if(appState.getBluetoothSocket().isConnected() && appState.getBluetoothSocket().getRemoteDevice().getAddress().compareTo(device.getAddress()) == 0) {
                            button.setText("TRENNEN");
                        }
                    }

                    split.addView(button);

                    TextView text = new TextView(root.getContext());
                    text.setText(device.getName());
                    split.addView(text);

                    listLayout.addView(split);
                }
            }
        });


        return root;
    }

}