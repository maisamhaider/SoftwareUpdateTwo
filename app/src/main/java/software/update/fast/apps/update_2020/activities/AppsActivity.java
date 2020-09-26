package software.update.fast.apps.update_2020.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.mlsdev.animatedrv.AnimatedRecyclerView;

import java.util.ArrayList;

import software.update.fast.apps.update_2020.R;
import software.update.fast.apps.update_2020.adapters.AppsAdapter;
import software.update.fast.apps.update_2020.interfaces.TrueOrFalse;
import software.update.fast.apps.update_2020.utils.Apps;

public class AppsActivity extends BaseActivity implements TrueOrFalse{
    Apps apps;
    boolean isSystem;
    private TextView search_et,noAppsFound_tv;
    AppsAdapter appsAdapter;
    ArrayList<String> appsList;
    InputMethodManager imm;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setmContext(this);
        isSystem = getIntent().getBooleanExtra("isSystemApps", false);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        apps = new Apps(this);
        AnimatedRecyclerView apk_RV = findViewById(R.id.apk_RV);
        TextView appNo_tv = findViewById(R.id.appNo_tv);
        search_et = findViewById(R.id.search_et);
        noAppsFound_tv = findViewById(R.id.noAppsFound_tv);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        search_et.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        appsList = apps.allAppPackages();
        appNo_tv.setText(appsList.size() + " Apps");
        appsAdapter = new AppsAdapter(AppsActivity.this, appsList, "");

        search_et.setOnClickListener(v -> {
            imm.showSoftInput(search_et, InputMethodManager.SHOW_FORCED);
            search_et.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            search_et.setSingleLine();
        });

        search_et.setOnEditorActionListener((v, actionId, event) -> {

            if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENDCALL) || (actionId == EditorInfo.IME_ACTION_SEARCH)) {
                imm.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
            }
            return false;
        });

        apk_RV.setLayoutManager(linearLayoutManager);
        apk_RV.setAdapter(appsAdapter);
        appsAdapter.initTrueOrFalse(this);
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                appsAdapter.getFilter().filter(s);

            }
        });


        appsAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onPause() {
        super.onPause();
        imm.hideSoftInputFromWindow(search_et.getWindowToken(), 0);
    }

    @Override
    public void isTrue(boolean isTrue) {
        if (isTrue)
        {
            noAppsFound_tv.setVisibility(View.VISIBLE);
        }
        else
        {
            noAppsFound_tv.setVisibility(View.GONE);
        }
    }
}