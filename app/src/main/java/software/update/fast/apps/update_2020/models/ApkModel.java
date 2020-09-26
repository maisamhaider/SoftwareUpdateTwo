package software.update.fast.apps.update_2020.models;

public class ApkModel {
    private String packageName;
    private String newVersion;
    private String currentVersion;
    private String appName;


    public ApkModel(String packageName, String appName, String currentVersion, String newVersion) {
        this.packageName = packageName;
        this.newVersion = newVersion;
        this.currentVersion = currentVersion;
        this.appName = appName;

    }

    public String getPackageName() {
        return packageName;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public String getAppName() {
        return appName;
    }

}
