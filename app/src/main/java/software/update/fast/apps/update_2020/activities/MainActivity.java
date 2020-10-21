package software.update.fast.apps.update_2020.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import software.update.fast.apps.update_2020.R;
import software.update.fast.apps.update_2020.fragments.AboutFragment;
import software.update.fast.apps.update_2020.fragments.HomeFragment;
import software.update.fast.apps.update_2020.fragments.UsageFragment;

public class MainActivity extends BaseActivity {
    private BottomNavigationView bottom_nav;
    private MeowBottomNavigation meow_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        bottom_nav = findViewById(R.id.bottom_nav);
        meow_navigation = findViewById(R.id.meow_navigation);
        meow_navigation.show(1, true);
        showInterstitial();
        loadFragments(HomeFragment.newInstance());

        meow_navigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home_select));
        meow_navigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_usage));
        meow_navigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_about));

        meow_navigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // your codes
                switch (item.getId()) {
                    case 1:
                        loadFragments(HomeFragment.newInstance());
                        break;
                    case 2:
                        loadFragments(UsageFragment.newInstance());
                        break;
                    case 3:
                        loadFragments(AboutFragment.newInstance());
                        break;
                }
            }
        });


        meow_navigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        meow_navigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        //        loadFragments(HomeFragment.newInstance());
//        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                switch (item.getItemId()) {
//                    case R.id.home_item:
//                        sNewFragmentAds(HomeFragment.newInstance());
//                         break;
//                    case R.id.usage_item:
//                        sNewFragmentAds(UsageFragment.newInstance());
//                         break;
//                    case R.id.about_item:
//                        sNewFragmentAds(AboutFragment.newInstance());
//                         break;
//                }
//                return true;
//            }
//        });
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