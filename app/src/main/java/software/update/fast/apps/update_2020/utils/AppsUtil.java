package software.update.fast.apps.update_2020.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppsUtil {
    private static final String SYSTEM_PACKAGE_NAME = "android";
    private PackageManager mPackageManager = null;

    public AppsUtil(Context context) {
        mPackageManager = (PackageManager) context.getPackageManager();
    }
    public boolean isSystemApp(String packageName) {
        try {
            // Get packageinfo for target application
            PackageInfo targetPkgInfo = mPackageManager.getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            // Get packageinfo for system package
            PackageInfo sys = mPackageManager.getPackageInfo(
                    SYSTEM_PACKAGE_NAME, PackageManager.GET_SIGNATURES);
            // Match both packageinfo for there signatures
            return (targetPkgInfo != null && targetPkgInfo.signatures != null && sys.signatures[0]
                    .equals(targetPkgInfo.signatures[0]));
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public boolean isAppPreLoaded(String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("Package name can not be null");
        }
        try {
            ApplicationInfo ai = mPackageManager.getApplicationInfo(
                    packageName, 0);
            // First check if it is preloaded.
            // If yes then check if it is System app or not.
            if (ai != null
                    && (ai.flags & (ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0) {
                // Check if signature matches
                if (isSystemApp(packageName)) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
