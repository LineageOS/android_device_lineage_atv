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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Process;
import android.os.SystemProperties;
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

public class NetflixService extends Service {
    private static final String TAG = "NetflixService";

    private static final String NRDP_PLATFORM_CAP           = "nrdp_platform_capabilities";
    private static final String NRDP_PLATFORM_CONFIG_DIR    = "/vendor/etc/";

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        String buildDate = SystemProperties.get("ro.build.version.incremental", "");
        boolean needUpdate = !buildDate.equals(SettingsPref.getSavedBuildDate(mContext));

        setNrdpCapabilitesIfNeed(NRDP_PLATFORM_CAP, needUpdate);
        if (needUpdate) {
            SettingsPref.setSavedBuildDate(mContext, buildDate);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setNrdpCapabilitesIfNeed(String capName, boolean needUpdate) {
        String cap = Settings.Global.getString(getContentResolver(), capName);
        Log.i(TAG, capName + ":\n" + cap);
        if (!needUpdate && !TextUtils.isEmpty(cap)) {
            return;
        }

        try {
            Scanner scanner = new Scanner(new File(NRDP_PLATFORM_CONFIG_DIR + capName + ".json"));
            StringBuffer sb = new StringBuffer();

            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
                sb.append('\n');
            }

            Settings.Global.putString(getContentResolver(), capName, sb.toString());
            scanner.close();
        } catch (java.io.FileNotFoundException e) {
            Log.d(TAG, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

