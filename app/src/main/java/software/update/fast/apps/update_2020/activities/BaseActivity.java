package software.update.fast.apps.update_2020.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

import java.net.HttpURLConnection;
import java.net.URL;

import software.update.fast.apps.update_2020.R;


public class BaseActivity extends AppCompatActivity {
    InterstitialAd mInterstitialAd;
    public AdRequest adRequest;
    public ProgressDialog showDialog;
    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adRequest = new AdRequest.Builder().build();

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial));

         loadInterstitial();
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void mToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    public boolean isAppLive(String appid) {
        try {
            HttpURLConnection conn = (HttpURLConnection) (new URL("https://play.google.com/store/apps/details?id=" + appid))
                    .openConnection();
            conn.setUseCaches(false);
            conn.connect();
            int status = conn.getResponseCode();
            conn.disconnect();
            return status == 200;
        } catch (Exception e) {
            Log.e("isAppLiveOnPlayStore", e.toString());
        }
        return false;
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (showDialog != null)
        {
            showDialog.dismiss();
        }
    }

    public void showInterstitial() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.

        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

        if (mInterstitialAd != null) {
            mInterstitialAd.setAdListener(
                    new AdListener() {
                        @Override
                        public void onAdLoaded() {
    //                                        Toast.makeText(BaseActivity.this, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                        }
                        @Override
                        public void onAdClosed() {
                            loadInterstitial();
                        }});
        }
    }

    public void loadInterstitial() {
        // Request a new ad if one isn't already loaded, hide the button, and kick off the timer.
        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

}
