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

public class NetflixService extends BroadcastReceiver {
    private static final String TAG = "NetflixBroadcastReceiver";

    private static final String NRDP_PLATFORM_CAP        = "nrdp_platform_capabilities";
    private static final String NRDP_PLATFORM_CONFIG_DIR = "/vendor/etc/";
    private static final String SAVED_BUILD_DATE         = "saved_build_date";

    @Override
    public void onReceive(Context context, Intent intent) {
        String buildDate = SystemProperties.get("ro.build.version.incremental", "");
        boolean needUpdate = !buildDate.equals(getSavedBuildDate());

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
        editor.commit();
    }

    private void setNrdpCapabilitesIfNeed(Context cts, String capName, boolean needUpdate) {
        String cap = Settings.Global.getString(ctx.getContentResolver(), capName);
        Log.i(TAG, capName + ": " + cap);
        if (!needUpdate && !TextUtils.isEmpty(cap)) {
            return;
        }

        try (Scanner scanner = new Scanner(new File(NRDP_PLATFORM_CONFIG_DIR + capName + ".json"))) {
            StringBuffer sb = new StringBuffer();

            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
                sb.append('\n');
            }

            Settings.Global.putString(ctx.getContentResolver(), capName, sb.toString());
            scanner.close();
        } catch (java.io.FileNotFoundException e) {
            Log.d(TAG, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
