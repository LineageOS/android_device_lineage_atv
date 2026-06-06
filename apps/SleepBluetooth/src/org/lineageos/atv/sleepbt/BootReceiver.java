/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.atv.sleepbt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** Starts {@link SleepBluetoothService} once the device finishes booting. */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            context.startService(new Intent(context, SleepBluetoothService.class));
        }
    }
}
