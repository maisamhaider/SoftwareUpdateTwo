package software.update.fast.apps.update_2020.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import software.update.fast.apps.update_2020.R;
import software.update.fast.apps.update_2020.fragments.AboutFragment;
import software.update.fast.apps.update_2020.fragments.HomeFragment;
import software.update.fast.apps.update_2020.fragments.UsageFragment;

public class MainActivity extends BaseActivity {
    private BottomNavigationView bottom_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        bottom_nav = findViewById(R.id.bottom_nav);
        int[] tabIconsSelect = new int[]{R.drawable.ic_selector_home, R.drawable.ic_selector_usage, R.drawable.ic_selector_settings};
        loadFragments(HomeFragment.newInstance());
        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home_item:
                        sNewFragmentAds(HomeFragment.newInstance());
                         break;
                    case R.id.usage_item:
                        sNewFragmentAds(UsageFragment.newInstance());
                         break;
                    case R.id.about_item:
                        sNewFragmentAds(AboutFragment.newInstance());
                         break;
                }
                return true;
            }
        });
    }

    public void loadFragments(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentsContainer, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            exitt();
        }

    }


    public void exitt() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater layoutInflater = getLayoutInflater();
            @SuppressLint("InflateParams") final View dialogView = layoutInflater.inflate(R.layout.exit_and_rateus, null);
            ConstraintLayout yes_cl = dialogView.findViewById(R.id.yes_cl);
            ConstraintLayout no_cl = dialogView.findViewById(R.id.no_cl);
            ConstraintLayout rateUs_cl = dialogView.findViewById(R.id.rateUs_cl);


            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            yes_cl.setOnClickListener(view -> {
                moveTaskToBack(true);
                alertDialog.cancel();

                finishAffinity();
            });

            no_cl.setOnClickListener(view ->
            {
//
                alertDialog.dismiss();

            });
            rateUs_cl.setOnClickListener(view ->
                    {
                        rateUs();
                    }
            );

        } catch (Exception a) {
            a.printStackTrace();
        }
    }

    public void rateUs() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
    }


}