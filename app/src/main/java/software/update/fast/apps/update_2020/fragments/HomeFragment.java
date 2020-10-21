package software.update.fast.apps.update_2020.fragments;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.List;

import software.update.fast.apps.update_2020.R;
import software.update.fast.apps.update_2020.activities.AppsActivity;
import software.update.fast.apps.update_2020.activities.BaseActivity;
import software.update.fast.apps.update_2020.activities.ScanningActivity;
import software.update.fast.apps.update_2020.utils.MathSolver;
import software.update.fast.apps.update_2020.utils.MyInternetCon;

public class HomeFragment extends Fragment {

    private View view;
    MathSolver mathSolver;
    RoundCornerProgressBar ram_pb;

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
        ((BaseActivity) getActivity()).showInterstitial();

        mathSolver = new MathSolver(getContext());
        ram_pb = view.findViewById(R.id.ram_pb);
        view.findViewById(R.id.softwareUpdate_cl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                softwareUpdaterCheckerFun(getContext(), "android.settings.SYSTEM_UPDATE_SETTINGS");
            }
        });

        view.findViewById(R.id.appsUpdate_cl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyInternetCon.checkConnection(getContext())) {
                    getContext().startActivity(new Intent(getActivity(), ScanningActivity.class));
                } else {
                    Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        view.findViewById(R.id.allApps_cl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getActivity(), AppsActivity.class));

            }
        });
        ramAndStorageFun();
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
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return activities != null && activities.size() > 0;
    }

    private void ramAndStorageFun() {

//for Ram
        ActivityManager am = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        float totalMemory = mi.totalMem;
        float freeMemory = mi.availMem;
        float usedMemory = totalMemory - freeMemory;
        @SuppressLint("DefaultLocale") String ramUsagePercentage = String.format("%.1f",
                mathSolver.getPercentageFloat(totalMemory, usedMemory)) + "%";
//
//        TextView systemAndAppsSize_tv = view.findViewById(R.id.systemAndAppsSize_tv);
        TextView freeRam_tv = view.findViewById(R.id.freeRam_tv);
        TextView totalRam_tv = view.findViewById(R.id.totalRam_tv);
        TextView ramPercent_tv = view.findViewById(R.id.ramPercent_tv);
//        homeRam_cpb = view.findViewById(R.id.homeRam_cpb);
//
//        systemAndAppsSize_tv.setText(mathCalculationsUtil.getCalculatedDataSizeWithPrefix(usedMemory));
        freeRam_tv.setText(mathSolver.getCalculatedDataSizeWithPrefix(freeMemory));
        totalRam_tv.setText(mathSolver.getCalculatedDataSizeWithPrefix(totalMemory));
        ramPercent_tv.setText(ramUsagePercentage);
//
        ram_pb.setMax(totalMemory);
        ValueAnimator animator1 = ValueAnimator.ofFloat(0, usedMemory);
        animator1.setDuration(1500);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ram_pb.setProgress(Float.parseFloat(animation.getAnimatedValue().toString()));
            }
        });
        animator1.start();
        ram_pb.setProgress(usedMemory);
    }
}