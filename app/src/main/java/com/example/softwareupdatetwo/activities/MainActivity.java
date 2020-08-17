package com.example.softwareupdatetwo.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.softwareupdatetwo.R;
import com.example.softwareupdatetwo.fragments.AboutFragment;
import com.example.softwareupdatetwo.fragments.HomeFragment;
import com.example.softwareupdatetwo.fragments.UsageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
   private BottomNavigationView bottom_nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom_nav = findViewById(R.id.bottom_nav);

        loadFragments(HomeFragment.newInstance());
        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home_item:
                        loadFragments(HomeFragment.newInstance());
                        break;
                        case R.id.usage_item:
                        loadFragments(UsageFragment.newInstance());
                        break;
                        case R.id.about_item:
                        loadFragments(AboutFragment.newInstance());
                        break;
                }
                return true;
            }
        });
    }

    public void setHomeFragDefaultBNBItem() {
        bottom_nav.setSelectedItemId( R.id.home_item );

    }

    public void setUsageBNBItem() {
        bottom_nav.setSelectedItemId( R.id.usage_item );

    }

    public void setAboutBNBItem() {
        bottom_nav.setSelectedItemId( R.id.about_item );

    }

    public void loadFragments(Fragment fragment)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentsContainer,fragment);
        ft.commit();
    }
}