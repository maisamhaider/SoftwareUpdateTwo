package com.example.softwareupdatetwo.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.softwareupdatetwo.R;

import java.util.List;

public class HomeFragment extends Fragment {

   private View view;
    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_home, container, false);
       Button softwareUpdate_btn = view.findViewById(R.id.softwareUpdate_btn);
       Button appsUpdate_btn = view.findViewById(R.id.appsUpdate_btn);
       Button apps_btn = view.findViewById(R.id.apps_btn);
        softwareUpdate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.Se);
//                ResolveInfo resolveInfo = getActivity().getPackageManager().resolveActivity(intent, 0);
//                if (resolveInfo != null) {
//                    startActivity(intent);
//                }

                softwareUpdaterCheckerFun(getContext(),"android.settings.SYSTEM_UPDATE_SETTINGS");
             }
        });
        return view;
    }

    public void softwareUpdaterCheckerFun(Context context, String setting) {
        Intent intent = new Intent(setting, null);

        final boolean isAndroidJellyBean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;

        if (isAndroidJellyBean || !areSettingsAvailable(context, intent)) {
            intent = new Intent(Settings.ACTION_SETTINGS);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public Boolean areSettingsAvailable(Context context, Intent intent) {
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return activities != null && activities.size() > 0;
    }
    private void ramAndStorageFun(){

//for Ram
        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        float totalMemory = mi.totalMem;
        float freeMemory = mi.availMem;
        float usedMemory = totalMemory - freeMemory;
//        @SuppressLint("DefaultLocale") String ramUsagePercentage = String.format("%.1f", mathCalculationsUtil.getPercentageFloat(totalMemory, usedMemory)) + "%";
//
//        TextView systemAndAppsSize_tv = view.findViewById(R.id.systemAndAppsSize_tv);
//        TextView availableRamSize_tv = view.findViewById(R.id.availableRamSize_tv);
//        TextView totalRamSize_tv = view.findViewById(R.id.totalRamSize_tv);
//        TextView homeRamPercentage_tv = view.findViewById(R.id.homeRamPercentage_tv);
//        homeRam_cpb = view.findViewById(R.id.homeRam_cpb);
//
//        systemAndAppsSize_tv.setText(mathCalculationsUtil.getCalculatedDataSizeWithPrefix(usedMemory));
//        availableRamSize_tv.setText(mathCalculationsUtil.getCalculatedDataSizeWithPrefix(freeMemory));
//        totalRamSize_tv.setText(mathCalculationsUtil.getCalculatedDataSizeWithPrefix(totalMemory));
//        homeRamPercentage_tv.setText(ramUsagePercentage);
//
//        homeRam_cpb.setMaxProgress(totalMemory);
//        ValueAnimator animator1 = ValueAnimator.ofFloat(0, usedMemory);
//        animator1.setDuration(1500);
//        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                homeRam_cpb.setProgress(Float.parseFloat(animation.getAnimatedValue().toString()), totalMemory);
//            }
//        });
//        animator1.start();
//        homeRam_cpb.setProgress(usedMemory, totalMemory);
        }
}