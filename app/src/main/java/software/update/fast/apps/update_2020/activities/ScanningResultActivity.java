package software.update.fast.apps.update_2020.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import software.update.fast.apps.update_2020.R;
import software.update.fast.apps.update_2020.adapters.AppsAdapter;
import software.update.fast.apps.update_2020.annotations.MyAnnotation;
import software.update.fast.apps.update_2020.utils.Apps;

public class ScanningResultActivity extends BaseActivity {
    private RecyclerView updateResult_rv;
    Apps apps;
    ArrayList<String> appsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning_result);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setmContext(this);
        apps = new Apps(this);
        updateResult_rv = findViewById(R.id.updateResult_rv);
       TextView updateListSize_tv = findViewById(R.id.updateListSize_tv);
        appsList = getIntent().getStringArrayListExtra("new_updates_list");
        if (appsList.size()!=0)
        {
            updateListSize_tv.setText("Update: ("+appsList.size()+" Found)" );
            startAdapter();
        }
        else {
            updateListSize_tv.setText("Update: No app found");
            Toast.makeText(this, "No app found", Toast.LENGTH_SHORT).show();
        }

    }

    public void startAdapter() {
        LinearLayoutManager lLM = new LinearLayoutManager(this);
        lLM.setOrientation(LinearLayoutManager.VERTICAL);
        AppsAdapter appsAdapter = new AppsAdapter(this, appsList, MyAnnotation.UPDATE);
        updateResult_rv.setLayoutManager(lLM);
        updateResult_rv.setAdapter(appsAdapter);
        appsAdapter.notifyDataSetChanged();
    }
}