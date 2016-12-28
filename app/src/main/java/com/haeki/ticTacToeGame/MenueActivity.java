package com.haeki.ticTacToeGame;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MenueActivity extends AppCompatActivity {

    public final static String PLAY_MODE = "com.example.myfirstapp.PLAY_MODE";
    private static final int REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menue);
    }

    public void startSinglePlayer(View view) {
        startGame(true);
    }

    public void startMultiPlayer(View view) {
        startGame(false);
    }

    public void showStats(View view) {
        SharedPreferences settings = getSharedPreferences("stats", MODE_PRIVATE);
        Toast toast = Toast.makeText(getApplicationContext(), Integer.toString(settings.getInt("gameCount", 0)), Toast.LENGTH_SHORT);
        toast.show();
    }

    public void connectBluetooth(View view) {
        System.out.println("Find Bluetooth Device");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            System.out.println("No Bluetooth support");
        }
        if (!bluetoothAdapter.isDiscovering()) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
        Intent settingsIntent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        startActivity(settingsIntent);

        final ArrayList<BluetoothDevice> devices = new ArrayList<>();
        // If there are paired devices
        if (bluetoothAdapter.getBondedDevices().size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
                // Add the name and address to an array adapter to show in a ListView
                devices.add(device);
            }
        }

        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    devices.add(device);
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        System.out.println("Found " + devices.size() + " devices!");

    }

    public void startGame(boolean singleplayer) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(PLAY_MODE, singleplayer);
        startActivity(intent);
    }
}
