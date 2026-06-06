/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.atv.sleepbt;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidHost;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistent service that, while enabled, disconnects Bluetooth HID accessories
 * (game controllers / remotes) when the screen turns off (device sleeps) and
 * reconnects them on wake. HID only; audio and other profiles are left untouched.
 */
public class SleepBluetoothService extends Service {

    private static final String TAG = "SleepBluetooth";

    /** Settings.Global int key shared with the TvSettings toggle. 0 = off, 1 = on. */
    public static final String SETTING_KEY = "bluetooth_disconnect_on_sleep";

    private BluetoothAdapter mAdapter;
    private BluetoothHidHost mHidHost;
    private volatile boolean mEnabled;

    /** Devices we disconnected on the last sleep, to reconnect on wake. */
    private final List<BluetoothDevice> mDisconnected = new ArrayList<>();

    private final BluetoothProfile.ServiceListener mProfileListener =
            new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.HID_HOST) {
                mHidHost = (BluetoothHidHost) proxy;
                Log.i(TAG, "HID host proxy connected");
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HID_HOST) {
                mHidHost = null;
            }
        }
    };

    private final BroadcastReceiver mScreenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!mEnabled) {
                return;
            }
            final String action = intent.getAction();
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                disconnectHidDevices();
            } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
                reconnectHidDevices();
            }
        }
    };

    private ContentObserver mSettingObserver;

    @Override
    public void onCreate() {
        super.onCreate();

        final BluetoothManager bm = getSystemService(BluetoothManager.class);
        mAdapter = (bm != null) ? bm.getAdapter() : BluetoothAdapter.getDefaultAdapter();
        if (mAdapter != null) {
            mAdapter.getProfileProxy(this, mProfileListener, BluetoothProfile.HID_HOST);
        } else {
            Log.w(TAG, "No Bluetooth adapter; service is a no-op");
        }

        mEnabled = isFeatureEnabled();

        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mScreenReceiver, filter);

        mSettingObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChange) {
                mEnabled = isFeatureEnabled();
                Log.i(TAG, "Feature toggled, enabled=" + mEnabled);
            }
        };
        getContentResolver().registerContentObserver(
                Settings.Global.getUriFor(SETTING_KEY), false, mSettingObserver);

        Log.i(TAG, "Service created, enabled=" + mEnabled);
    }

    private boolean isFeatureEnabled() {
        return Settings.Global.getInt(getContentResolver(), SETTING_KEY, 0) != 0;
    }

    private void disconnectHidDevices() {
        if (mHidHost == null) {
            return;
        }
        mDisconnected.clear();
        for (BluetoothDevice device : mHidHost.getConnectedDevices()) {
            Log.i(TAG, "Disconnecting HID device on sleep: " + device);
            if (setHidConnection(device, false)) {
                mDisconnected.add(device);
            }
        }
    }

    private void reconnectHidDevices() {
        if (mHidHost == null) {
            return;
        }
        for (BluetoothDevice device : mDisconnected) {
            Log.i(TAG, "Reconnecting HID device on wake: " + device);
            setHidConnection(device, true);
        }
        mDisconnected.clear();
    }

    // connect()/disconnect() are @hide, so call them via reflection.
    // Not setConnectionPolicy: plain disconnect keeps the bond, so the pad can still wake us.
    private boolean setHidConnection(BluetoothDevice device, boolean connect) {
        try {
            final Method method = BluetoothHidHost.class.getMethod(
                    connect ? "connect" : "disconnect", BluetoothDevice.class);
            final Object result = method.invoke(mHidHost, device);
            return (result instanceof Boolean) ? (Boolean) result : true;
        } catch (ReflectiveOperationException e) {
            Log.w(TAG, "HID " + (connect ? "connect" : "disconnect") + " failed for "
                    + device, e);
            return false;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mScreenReceiver);
        } catch (Exception ignored) {
        }
        if (mSettingObserver != null) {
            getContentResolver().unregisterContentObserver(mSettingObserver);
        }
        if (mAdapter != null && mHidHost != null) {
            mAdapter.closeProfileProxy(BluetoothProfile.HID_HOST, mHidHost);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
