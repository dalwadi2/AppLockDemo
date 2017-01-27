package io.dalwadi2.applockdemo.services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;

import java.util.List;

import io.dalwadi2.applockdemo.LockPreference;

public class CheckRunningActivity extends Thread {
    private static final String TAG = "CheckRunningActivity";
    ActivityManager am = null;
    Context context = null;
    LockPreference lockPreference;

    public CheckRunningActivity(Context con) {
        context = con;
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        lockPreference = new LockPreference();

//        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
//        for (int i = 0; i < packList.size(); i++) {
//            PackageInfo packInfo = packList.get(i);
//            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
//                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
//                Log.e("App № " + Integer.toString(i), appName);
//            }
//        }

    }

    public void run() {
        Looper.prepare();

//        while(true){
        // Return a list of the tasks that are currently running,
        // with the most recent being first and older ones after in order.
        // Taken 1 inside getRunningTasks method means want to take only
        // top activity from stack and forgot the olders.
        List<ActivityManager.RunningAppProcessInfo> taskInfo = am.getRunningAppProcesses();

        for (int i = 0; i < taskInfo.size(); i++) {
            Log.e("tasks", "run: " + taskInfo.get(i).processName);
        }
//        String currentRunningActivityName = taskInfo.get(0).topActivity.getClassName();

//            if (currentRunningActivityName.equals("PACKAGE_NAME.ACTIVITY_NAME")) {
//                // show your activity here on top of PACKAGE_NAME.ACTIVITY_NAME
//            }
//        }
        final PackageManager pm = context.getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {

                if (packageInfo.packageName != context.getPackageName()) {
                    lockPreference.addLocked(context, packageInfo.packageName);

                    Log.e("test", "Installed package :" + packageInfo.packageName);
                    Log.e("test", "Source dir : " + packageInfo.sourceDir);
                    Log.e("test", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
                    Log.e("test", "============== ");
                }
            }

        }
        Looper.loop();
    }
}
