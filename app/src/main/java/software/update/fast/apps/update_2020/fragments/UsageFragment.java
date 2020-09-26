package software.update.fast.apps.update_2020.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bot.box.appusage.contract.UsageContracts;
import bot.box.appusage.handler.Monitor;
import bot.box.appusage.model.AppData;
import bot.box.appusage.utils.Duration;
import software.update.fast.apps.update_2020.R;
import software.update.fast.apps.update_2020.adapters.UsageAdapter;

import static bot.box.appusage.datamanager.DataManager.init;


public class UsageFragment extends Fragment implements UsageContracts.View {

     List<AppData> apps;
    private RecyclerView appsUsage_rv;
    private TextView allowPermission_btn;
    private ConstraintLayout usagePermission_cl;
    private Context context;
    public static UsageFragment newInstance( ) {
        return new UsageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usage, container, false);
        appsUsage_rv = view.findViewById(R.id.appsUsage_rv);
        usagePermission_cl = view.findViewById(R.id.usagePermission_cl);
        allowPermission_btn = view.findViewById(R.id.allowPermission_btn);
        apps = new ArrayList<>();
     context = getContext();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Monitor.hasUsagePermission())
        {
            appsUsage_rv.setVisibility(View.VISIBLE);
            allowPermission_btn.setVisibility(View.GONE);
            usagePermission_cl.setVisibility(View.GONE);
            Monitor.scan().getAppLists(this).fetchFor(Duration.TODAY);
            init();
        }
        else
        {
            appsUsage_rv.setVisibility(View.GONE);
            allowPermission_btn.setVisibility(View.VISIBLE);
            usagePermission_cl.setVisibility(View.VISIBLE);
            allowPermission_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Monitor.requestUsagePermission();
                }
            });
        }
    }

    @Override
    public void getUsageData(List<AppData> usageData, long mTotalUsage, int duration) {
        apps.addAll(usageData);

        if (context != null) {
            List<AppData> usedApps = new ArrayList<>(usageData);
            Collections.sort(usedApps, new Comparator<AppData>() {
                public int compare(AppData obj1, AppData obj2) {
                    long dataOne = obj1.mEventTime;
                    long dataTwo = obj2.mEventTime;

                    if (dataOne == 0 || dataTwo == 0)
                        return 0;
                    return dataTwo > dataOne ? 1 : -1; // To compare Date values
                }
            });
            List<AppData> usedAppsFinal = new ArrayList<>(usedApps);
            UsageAdapter adapter = new UsageAdapter(getContext(), usedAppsFinal);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            appsUsage_rv.setLayoutManager(layoutManager);
            appsUsage_rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}