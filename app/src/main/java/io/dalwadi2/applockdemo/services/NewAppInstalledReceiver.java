package io.dalwadi2.applockdemo.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import io.dalwadi2.applockdemo.LockPreference;

public class NewAppInstalledReceiver extends BroadcastReceiver {

    LockPreference lockPreference;

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, AppCheckServices.class));
        lockPreference = new LockPreference();

        if (!intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED) && intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)) {
            return;
        }

        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String[] a = intent.getDataString().split(":");
            String packageName = a[a.length - 1];
            if (lockPreference != null) {
                if (!lockPreference.getPassword(context).isEmpty()) {
                    showDialogToAskForNewAppInstalled(context, appName(context, packageName), packageName);
                }
            }
        }

        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String[] a = intent.getDataString().split(":");
            String packageName = a[a.length - 1];
            lockPreference.removeLocked(context, packageName);
        }
    }

    public String appName(Context context, String packageName) {
        try {
            ApplicationInfo app = context.getPackageManager().getApplicationInfo(packageName, 0);
            return context.getPackageManager().getApplicationLabel(app).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return "";
    }

    public void showDialogToAskForNewAppInstalled(final Context context, String appName, final String packageName) {
        lockPreference.addLocked(context, packageName);
        Toast.makeText(context, appName + " locked by Administrator.", Toast.LENGTH_SHORT).show();
    }


}
