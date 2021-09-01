package org.lineage.netflixconfig;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Process;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.StringBuffer;
import java.util.List;
import java.util.Scanner;

public class NetflixBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "NetflixBroadcastReceiver";

    private static final String SAVED_BUILD_DATE = "saved_build_date";

    @Override
    public void onReceive(Context context, Intent intent) {
        String buildDate = SystemProperties.get("ro.build.version.incremental", "");
        boolean needUpdate = !buildDate.equals(getSavedBuildDate(context));

        setNrdpCapabilitesIfNeed(context, NRDP_PLATFORM_CAP, needUpdate);
        if (needUpdate) {
            setSavedBuildDate(context, buildDate);
        }
    }

    private static String getSavedBuildDate(Context ctx) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sp.getString(SAVED_BUILD_DATE, "");
    }

    private static void setSavedBuildDate(Context ctx, String date) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SAVED_BUILD_DATE, date);
        editor.apply();
    }

    private void setNrdpCapabilitesIfNeed(Context cts, String capName, boolean needUpdate) {
        String cap = Settings.Global.getString(ctx.getContentResolver(), capName);
        Log.i(TAG, capName + ": " + cap);
        if (!needUpdate && !TextUtils.isEmpty(cap)) {
            return;
        }

        Settings.Global.putString(ctx.getContentResolver(), capName, getString(R.string.nrdp_capabilities));
    }
}
