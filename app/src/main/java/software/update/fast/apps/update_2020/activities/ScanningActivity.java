package software.update.fast.apps.update_2020.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import software.update.fast.apps.update_2020.R;
import software.update.fast.apps.update_2020.adapters.SAppsAdapter;
import software.update.fast.apps.update_2020.annotations.MyAnnotation;
import software.update.fast.apps.update_2020.utils.AppVersion;
import software.update.fast.apps.update_2020.utils.Apps;
import software.update.fast.apps.update_2020.utils.MyInternetCon;

public class ScanningActivity extends BaseActivity {
    Apps apps;
    List<String> appsList;
    private Context context;
    private boolean isCanceled = false;
    private ArrayList<String> updateApksList;
    private TextView scanningApk_tv, countedApk_tv, scannedAppsPkgs_tv;
    private int appsCounter = 0;
    private int counterTwo = 0;
    private boolean isStart = false;
    SAppsAdapter SAppsAdapter;
    String nVersion;
    RecyclerView scanningApk_RV;
    LinearLayoutManager layoutManager;
    MaterialProgressBar materialProgressBar;
    AlertDialog isStartDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = getApplicationContext();
        showInterstitial();
        apps = new Apps(this);
        updateApksList = new ArrayList<>();
        appsList = apps.allAppPackages();
        isCanceled = false;
        scanningApk_tv = findViewById(R.id.scanningApk_tv);
        countedApk_tv = findViewById(R.id.countedApk_tv);
        countedApk_tv.setText("Update Count:    " + "0");
        scanningApk_tv.setText("Scanned Apps:    " + "0");
        TextView totalApk_tv = findViewById(R.id.totalApk_tv);
        totalApk_tv.setText(totalApk_tv.getText().toString() + "    " + appsList.size());
        scanningApk_RV = findViewById(R.id.scanningApk_RV);
        materialProgressBar = findViewById(R.id.materialProgressBar);
        scannedAppsPkgs_tv = findViewById(R.id.scannedAppsPkgs_tv);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        scanningApk_RV.setLayoutManager(layoutManager);
        SAppsAdapter = new SAppsAdapter(this);
        materialProgressBar.setMax(appsList.size());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please wait a moment").setCancelable(false);
        isStartDialog = builder.create();

        if (!isStart) {
            isStartDialog.show();
        }
        new GetVersion().execute();


    }

    @SuppressLint("StaticFieldLeak")
    class GetVersion extends AsyncTask<Void, Integer, String> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (!isCanceled) {
                if (MyInternetCon.checkConnection(context)) {
                    for (final String packageName : appsList) {
                        if (!isCanceled) {
                            if (MyInternetCon.checkConnection(context)) {
                                final String currentVersion = apps.appInformation(packageName, MyAnnotation.APP_VERSION);
                                final String appName = apps.appInformation(packageName, MyAnnotation.APP_NAME);
                                counterTwo = counterTwo + 1;
                                boolean isLive = isAppLive(packageName);
                                if (isLive) {
                                    try {
                                        Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packageName + "&hl=en")
                                                .timeout(30000)
                                                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                                .referrer("http://www.google.com")
                                                .ignoreHttpErrors(true)
                                                .get();

                                        if (doc != null) {
                                            Elements element = doc.getElementsContainingOwnText("Current Version");
                                            for (Element elem : element) {
                                                if (elem.siblingElements() != null) {
                                                    Elements sibElemets = elem.siblingElements();
                                                    for (Element sibElemet : sibElemets) {
                                                        nVersion = sibElemet.text();
                                                    }
                                                }
                                            }
                                        }
                                        Log.i("nVersion", "nVersion" + nVersion);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            publishProgress(counterTwo);
                                            scanningApk_tv.setText("Scanned Apps:    " + counterTwo);
                                            scannedAppsPkgs_tv.setText(packageName);
                                            updateAppsAdapter(packageName, appName, currentVersion, nVersion);
                                            checkUpdate(nVersion, packageName, currentVersion);
                                        }
                                    });
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mToast("No Internet Connection");
                                        finish();
                                    }
                                });
                            }
                        }
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mToast("No Internet Connection");
                            finish();
                        }
                    });

                }

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            materialProgressBar.setProgress(values[0]);
            materialProgressBar.setSecondaryProgress(values[0] + 3);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            launchAct();
        }
    }

    @SuppressLint("SetTextI18n")
    private void checkUpdate(String onlineVersion, String packageName, String currentVersion) {
        if (!isCanceled) {
            isStart = true;
            isStartDialog.dismiss();

            try {

                if (onlineVersion != null && !onlineVersion.isEmpty()) {

                    AppVersion versionCurrent = new AppVersion(currentVersion);
                    AppVersion versionNew = new AppVersion(onlineVersion);
                    Log.i("nVersion", onlineVersion + " onlineVersion");
                    Log.i("nVersion", currentVersion + " currentVersion");
                    if (!versionCurrent.equals(versionNew)) {
                        if (versionNew.compareTo(versionCurrent) > 0) {
                            appsCounter = appsCounter + 1;
                            countedApk_tv.setText("Update Count: " + appsCounter);
                            updateApksList.add(packageName);
                        }

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder yesNoDlg = new AlertDialog.Builder(this);
        //yes or no alert box
        yesNoDlg.setMessage("Do you want to stop scanning?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isCanceled = true;
                        dialog.cancel();
                        ScanningActivity.this.finish();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = yesNoDlg.create();

        dialog.show();
    }

    private void launchAct() {

        if (!isCanceled) {
            if (updateApksList.size() == 0) {
                mToast("No update found");
                finish();
            } else {
                Intent intent = new Intent(ScanningActivity.this, ScanningResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("new_updates_list", updateApksList);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void updateAppsAdapter(String appPackage, String appName, String currentVersion, String nVersion) {
        scanningApk_RV.setAdapter(SAppsAdapter);
        SAppsAdapter.setScanningData(appPackage, appName, currentVersion, nVersion);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isStartDialog != null) {
            if (!isStart) {
                isStartDialog.dismiss();
            }
        }

    }
}