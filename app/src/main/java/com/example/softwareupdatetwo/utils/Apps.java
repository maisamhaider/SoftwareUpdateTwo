package com.example.softwareupdatetwo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.example.softwareupdatetwo.annotations.MyAnnotation;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Apps {

    private Context context;
    public Apps(Context context)
    {
        this.context = context;
    }

    public Apps() {
    }

    public ArrayList<String> apkPackages(boolean sysApp) {

        ArrayList<String> ApkPackageName = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resolveInfoList) {

            ActivityInfo activityInfo = resolveInfo.activityInfo;

            if (sysApp)
            {
                if (isSystemPackage(resolveInfo)) {
                    if (!ApkPackageName.contains(activityInfo.applicationInfo.packageName)) {
                        ApkPackageName.add(activityInfo.applicationInfo.packageName);
                    }
                }
            }
            else {
                if (!isSystemPackage(resolveInfo)) {
                    if (!ApkPackageName.contains(activityInfo.applicationInfo.packageName)) {
                        ApkPackageName.add(activityInfo.applicationInfo.packageName);
                    }
                }
            }
        }
        return ApkPackageName;
    }

    public ArrayList<String> allAppPackages() {

        ArrayList<String> ApkPackageName = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);

        for (ResolveInfo resolveInfo : resolveInfoList) {

            ActivityInfo activityInfo = resolveInfo.activityInfo;

            if (!isSystemPackage(resolveInfo) || isSystemPackage(resolveInfo))
            {
                    if (!ApkPackageName.contains(activityInfo.applicationInfo.packageName)) {
                        ApkPackageName.add(activityInfo.applicationInfo.packageName);
                }
            }

        }
        return ApkPackageName;
    }

    public boolean isSystemPackage(ResolveInfo resolveInfo) {

        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
    public long appTimesInfo(String apkPackageName,String whatKind)
    {
        long timeStamp = 0 ;
        PackageManager pm = context.getPackageManager();

        try {
            if (MyAnnotation.INSTALLATION.matches(whatKind))
            {
                PackageInfo info = pm.getPackageInfo(apkPackageName, 0);
                Field field = PackageInfo.class.getField("firstInstallTime");
                timeStamp = field.getLong(info);
            }else {
                PackageInfo info = pm.getPackageInfo(apkPackageName, 0);
                Field field = PackageInfo.class.getField("lastUpdateTime");
                timeStamp = field.getLong(info);
            }

        } catch (PackageManager.NameNotFoundException | IllegalAccessException e1) {
             e1.printStackTrace();
        } catch (Exception e1) {
             e1.printStackTrace();
        }

        return timeStamp;
    }

    // return any different types base on condition..
    public <T> T appInformation(String apkPackageName, String whatThing) {

        T thingWithType = null;

        ApplicationInfo applicationInfo;

        PackageManager packageManager = context.getPackageManager();

        try {
            applicationInfo = packageManager.getApplicationInfo(apkPackageName, 0);

            if (applicationInfo != null) {

                if (whatThing.matches(MyAnnotation.APP_ICON))
                {
                    thingWithType = (T) packageManager.getApplicationIcon(applicationInfo); }
                else if (whatThing.matches(MyAnnotation.APP_NAME))
                {
                    thingWithType = (T) packageManager.getApplicationLabel(applicationInfo);
                }
                else if (whatThing.matches(MyAnnotation.APP_VERSION))
                {
                    PackageManager pm = context.getPackageManager();
                    PackageInfo packageInfo = pm.getPackageInfo(apkPackageName,0);
                    thingWithType = (T) packageInfo.versionName;
                }
            }

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();
        }
        return thingWithType;
    }


    public long apkSize(Context context, String packageName)
            throws PackageManager.NameNotFoundException {
        return new File(context.getPackageManager().getApplicationInfo(packageName, 0).publicSourceDir).length();
    }


}
