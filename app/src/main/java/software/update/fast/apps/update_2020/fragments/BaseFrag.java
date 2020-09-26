package software.update.fast.apps.update_2020.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import software.update.fast.apps.update_2020.R;

public class BaseFrag extends Fragment {

    InterstitialAd mInterstitialAd;
    Context mContext;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

        loadInterstitial();
        MobileAds.initialize(mContext, initializationStatus -> {
        });
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void loadInterstitial() {
        mInterstitialAd = new InterstitialAd(getContext());
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
            Intent intent = new Intent(getContext(), activity.getClass());
            startActivity(intent);

        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                reqNewInterstitial();
                Intent intent = new Intent(getContext(), activity.getClass());
                startActivity(intent);

            }
        });
    }
    public void sNewActivityAds(Activity activity, String key , boolean isTrue) {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Intent intent = new Intent(getContext(), activity.getClass());
            intent.putExtra(key,isTrue);
            startActivity(intent);

        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                reqNewInterstitial();
                Intent intent = new Intent(getContext(), activity.getClass());
                intent.putExtra(key,isTrue);
                startActivity(intent);

            }
        });
    }

    @SuppressLint("ResourceType")
    public void adView(AdView adView) {

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int error) {
                adView.setVisibility(View.GONE);
            }

        });
    }


}
