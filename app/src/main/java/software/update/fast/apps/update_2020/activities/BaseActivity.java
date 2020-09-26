package software.update.fast.apps.update_2020.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.net.HttpURLConnection;
import java.net.URL;

import software.update.fast.apps.update_2020.R;


public class BaseActivity extends AppCompatActivity {
    InterstitialAd mInterstitialAd;
    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadInterstial();
        MobileAds.initialize(mContext, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }});}
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

    public void loadInterstial() {
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial));
        try {
            if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mInterstitialAd.loadAd(adRequest);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        reqNewInterstitial();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    public void reqNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public void sNewActivityAds(Activity activity) {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Intent intent = new Intent(mContext, activity.getClass());
            startActivity(intent);

        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                reqNewInterstitial();
                Intent intent = new Intent(mContext, activity.getClass());
                startActivity(intent);

            }
        });

    }

    public void sNewFragmentAds(Fragment fragment) {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            loadmyfrag(fragment);

        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                reqNewInterstitial();
                loadmyfrag(fragment);

            }
        });
    }

    public void loadmyfrag(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentsContainer, fragment);
        fragmentTransaction.commit();
    }

    public void sNewActivityAds(Activity activity, String string) {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Intent intent = new Intent(mContext, activity.getClass());
            intent.putExtra("appPackage", string);
            startActivity(intent);
        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                reqNewInterstitial();
                Intent intent = new Intent(mContext, activity.getClass());
                intent.putExtra("appPackage", string);
                startActivity(intent);
            }
        });

    }

    public void sNewActivityAds(String string) {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + string)));
        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                reqNewInterstitial();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + string)));
            }
        });

    }

    public void sNewActivityAds(Activity activity, boolean isSend) {
        Intent intent = new Intent(mContext, activity.getClass());
        intent.putExtra("isSend", isSend);
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            startActivity(intent);

        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                reqNewInterstitial();
                startActivity(intent);
            }
        });
    }

    public void adView(AdView adView) {

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError var1) {
                adView.setVisibility(View.GONE);
            }

        });
    }


}
