package software.update.fast.apps.update_2020.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import software.update.fast.apps.update_2020.R;
import software.update.fast.apps.update_2020.annotations.MyAnnotation;
import software.update.fast.apps.update_2020.utils.Apps;
import software.update.fast.apps.update_2020.utils.AppsUtil;
import software.update.fast.apps.update_2020.utils.MathSolver;
import software.update.fast.apps.update_2020.utils.MyTime;

public class AppInfoActivity extends AppCompatActivity {
         PackageManager packageManager ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Apps apps = new Apps(AppInfoActivity.this);
        MyTime myTime = new MyTime();
        MathSolver mathSolver = new MathSolver();
        packageManager = getPackageManager();
        AppsUtil appsUtil = new AppsUtil(AppInfoActivity.this);
        TextView detailAppName_tv = findViewById(R.id.detailAppName_tv);
        TextView currentVersion_tv = findViewById(R.id.currentVersion_tv);
        TextView appSize_tv = findViewById(R.id.appSize_tv);
        TextView installTime_tv = findViewById(R.id.installTime_tv);
        TextView lastUpdateTime_tv = findViewById(R.id.lastUpdateTime_tv);

        ImageView app_detail_iv = findViewById(R.id.app_detail_iv);
        ConstraintLayout uninstallApp_btn = findViewById(R.id.uninstallApp_cl);
        ConstraintLayout launchApp_cl = findViewById(R.id.launchApp_cl);
        ConstraintLayout openInSettings_cl = findViewById(R.id.openInSettings_cl);
        ConstraintLayout openInPlayStorage = findViewById(R.id.openInPlayStorage);

        String pkg = getIntent().getStringExtra("appPackage");

        String appName = apps.appInformation(pkg, MyAnnotation.APP_NAME);
        detailAppName_tv.setText(appName);
        String currentVersion = apps.appInformation(pkg, MyAnnotation.APP_VERSION);
        currentVersion_tv.setText("Current version: "+currentVersion);
        try {
            float appSize = apps.apkSize(this, pkg);
            String s = mathSolver.getCalculatedDataSizeWithPrefix(appSize);
            appSize_tv.setText("App size: "+s);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String installTime = myTime.appInstallTime(apps.appTimesInfo(pkg, MyAnnotation.INSTALLATION));
        String lastUpdateTime = myTime.appInstallTime(apps.appTimesInfo(pkg, MyAnnotation.UPDATE));
        installTime_tv.setText("Installation time: "+installTime);
        lastUpdateTime_tv.setText("Last update time: "+lastUpdateTime);
        Glide.with(this).load((Drawable) apps.appInformation(pkg, MyAnnotation.APP_ICON)).into(app_detail_iv);
        uninstallApp_btn.setOnClickListener(v ->
        {

            Intent intent = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
            } else {
                intent = new Intent(Intent.ACTION_DELETE);
            }
            if (appsUtil.isSystemApp(pkg)) {
                Toast.makeText(this, "Can not Uninstall system's application", Toast.LENGTH_SHORT).show();
            } else if (appsUtil.isAppPreLoaded(pkg)) {

            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.fromParts("package", pkg, null));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        launchApp_cl.setOnClickListener(v ->
        {

            PackageManager packageManager = this.getPackageManager();
            Intent intent = null;
            if (pkg != null) {
                intent = packageManager.getLaunchIntentForPackage(pkg);
            }
            if (intent != null) {
                startActivity(intent);
            }

        });
        openInPlayStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appsUtil.isSystemApp(pkg)) {
                    Toast.makeText(AppInfoActivity.this, "Play Store doesn't contain system apps", Toast.LENGTH_SHORT).show();
                }else if (isSystemPreloaded(pkg))
                {
                    Toast.makeText(AppInfoActivity.this, "Play Store doesn't contain system apps", Toast.LENGTH_SHORT).show();

                }else if (isSystemSigned(pkg))
                {
                    Toast.makeText(AppInfoActivity.this, "Play Store doesn't contain system apps", Toast.LENGTH_SHORT).show();

                }else if (isSystemAppByFLAG(pkg))
                {
                    Toast.makeText(AppInfoActivity.this, "Play Store doesn't contain system apps", Toast.LENGTH_SHORT).show();

                }
                else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkg)));
                }
            }
        });
        openInSettings_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + pkg));
                startActivity(intent);
            }
        });
    }

    public boolean isSystemAppByPM(String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("Package name cannot be null");
        }
        ProcessBuilder builder = new ProcessBuilder("pm", "list", "packages", "-s");
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
             return false;
        }

        InputStream in = process.getInputStream();
        Scanner scanner = new Scanner(in);
        Pattern pattern = Pattern.compile("^package:.+");
        int skip = "package:".length();

        Set<String> systemApps = new HashSet<String>();
        while (scanner.hasNext(pattern)) {
            String pckg = scanner.next().substring(skip);
            systemApps.add(pckg);
        }

        scanner.close();
        process.destroy();

        if (systemApps.contains(packageName)) {
            return true;
        }
        return false;
    }


    public boolean isSystemPreloaded(String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("Package name cannot be null");
        }
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(
                    packageName, 0);
            if (ai.sourceDir.startsWith("/system/app/") || ai.sourceDir.startsWith("/system/priv-app/")) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
         }
        return false;
    }

    public boolean isSystemSigned(String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("Package name cannot be null"); }
        try {
            // Get packageinfo for target application
            PackageInfo targetPkgInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            // Get packageinfo for system package
            PackageInfo sys = packageManager.getPackageInfo(
                    "android", PackageManager.GET_SIGNATURES);
            // Match both packageinfo for there signatures
            return (targetPkgInfo != null && targetPkgInfo.signatures != null && sys.signatures[0]
                    .equals(targetPkgInfo.signatures[0]));
        } catch (PackageManager.NameNotFoundException ignored) {
         }
        return false;
    }

    public boolean isSystemAppByFLAG(String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("Package name cannot be null");
        }
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 0);
            // Check if FLAG_SYSTEM or FLAG_UPDATED_SYSTEM_APP are set.
            if ((ai.flags & (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException ignored) {
         }
        return false;
    }

}
