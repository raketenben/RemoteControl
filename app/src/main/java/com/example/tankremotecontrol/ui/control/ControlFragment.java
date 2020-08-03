package com.example.tankremotecontrol.ui.control;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.tankremotecontrol.MainActivity;
import com.example.tankremotecontrol.R;

import java.io.IOException;
import java.io.OutputStream;

public class ControlFragment extends Fragment {

    private ControlViewModel controlViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controlViewModel =
                ViewModelProviders.of(this).get(ControlViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        final MainActivity appState = ((MainActivity) getParentFragment().getActivity());

        final LinearLayout list = root.findViewById(R.id.controls);

        if(appState.getBluetoothSocket() != null){

            TextView conn = new TextView(root.getContext());
            conn.setText("Verbunden mit '"+appState.getBluetoothSocket().getRemoteDevice().getName()+"'");
            conn.setGravity(Gravity.CENTER);
            list.addView(conn);


            final Button button = new Button(root.getContext());
            button.setText("/\\");

            final Button button1 = new Button(root.getContext());
            button1.setText("\\/");

            list.addView(button);
            list.addView(button1);

            try {
                final OutputStream stream = appState.getBluetoothSocket().getOutputStream();

                button.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            try {
                                stream.write(201);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if(event.getAction() == MotionEvent.ACTION_UP){
                            try {
                                stream.write(202);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return false;
                    }
                });

                button1.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            try {
                                stream.write(203);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if(event.getAction() == MotionEvent.ACTION_UP){
                            try {
                                stream.write(204);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return false;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            final TextView text = new TextView(root.getContext());
            text.setText("Kein verbundenes Gerät");
            text.setGravity(Gravity.CENTER);

            list.addView(text);
        }

        return root;
    }
}