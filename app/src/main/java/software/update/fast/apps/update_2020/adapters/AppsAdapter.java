package software.update.fast.apps.update_2020.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;

import software.update.fast.apps.update_2020.R;
import software.update.fast.apps.update_2020.activities.AppInfoActivity;
import software.update.fast.apps.update_2020.activities.BaseActivity;
import software.update.fast.apps.update_2020.annotations.MyAnnotation;
import software.update.fast.apps.update_2020.interfaces.TrueOrFalse;
import software.update.fast.apps.update_2020.utils.Apps;
import software.update.fast.apps.update_2020.utils.MathSolver;


public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.appHolder> implements Filterable {

    private Context context;
    private ArrayList<String> appsList;
    private ArrayList<String> appsFull;
    String update;
    Apps apps;
    MathSolver mathSolver;
    private TrueOrFalse trueOrFalse;

//    BaseAct baseAct;

    public AppsAdapter(Context context, ArrayList<String> appsList, String update) {
        this.context = context;
        this.appsList = appsList;
        this.update = update;
//        if (context !=null)
//        {
//            context.getPackageManager();
//        }
        apps = new Apps(context);
        mathSolver = new MathSolver();
        appsFull = new ArrayList<>();
        appsFull.addAll(appsList);
    }

    @NonNull
    @Override
    public appHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allapps_item, parent, false);
        return new appHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull appHolder holder, int position) {
//        baseAct = new BaseAct();

        final String appPkg = appsList.get(position);
        String appName = apps.appInformation(appPkg, MyAnnotation.APP_NAME);
        String appVersion = apps.appInformation(appPkg, MyAnnotation.APP_VERSION);

        if (update.matches(MyAnnotation.UPDATE)) {
            holder.update_tv.setText("Update");
            holder.appSize_tv.setVisibility(View.GONE);
        } else {
            holder.update_tv.setText("Open");
            holder.appSize_tv.setVisibility(View.VISIBLE);
        }
        holder.appName_tv.setText(appName);
        holder.appVersion_tv.setText(appVersion);

        Glide.with(context).load((Drawable) apps.appInformation(appPkg, MyAnnotation.APP_ICON)).into(holder.app_iv);
        try {
            String appSize = mathSolver.getCalculatedDataSizeWithPrefix((float) apps.apkSize(context, appPkg));
            holder.appSize_tv.setText(appSize);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        holder.update_tv.setOnClickListener(v -> {


            if (update.matches(MyAnnotation.UPDATE)) {
                context.startActivity(new
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPkg)));
            } else {
//                Intent intent = new Intent(context, AppInfoActivity.class);
//                intent.putExtra("app_package", appPkg);
//                context.startActivity(intent);
//            }
                if (context instanceof BaseActivity) {
                    BaseActivity baseAct = (BaseActivity) context;
                    baseAct.sNewActivityAds(new AppInfoActivity(), appPkg);
                } } });
    }

    @Override
    public int getItemCount() {
        return appsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class appHolder extends RecyclerView.ViewHolder {
        ImageView app_iv;
        TextView appName_tv, appVersion_tv, appSize_tv, update_tv;

        public appHolder(@NonNull View itemView) {
            super(itemView);
            update_tv = itemView.findViewById(R.id.update_tv);
            app_iv = itemView.findViewById(R.id.app_iv2);
            appName_tv = itemView.findViewById(R.id.appName_tv2);
            appVersion_tv = itemView.findViewById(R.id.appVersion_tv2);
            appSize_tv = itemView.findViewById(R.id.appSize_tv2);
        }
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<String> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(appsFull);
            } else {
                String string = constraint.toString().toLowerCase();
                for (int i = 0; i < appsFull.size(); i++) {
                    String app = apps.appInformation(appsFull.get(i), MyAnnotation.APP_NAME);
                    if (app.toLowerCase().startsWith(string)) {
                        filteredList.add(appsFull.get(i));
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
                return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            appsList.clear();
            appsList.addAll((Collection<? extends String>) results.values);
            if (appsList.isEmpty())
            {
                setTrueOrFalse(true);
            }
            else
            {
                setTrueOrFalse(false);
            }
            notifyDataSetChanged();
        }
    };

    public void initTrueOrFalse(TrueOrFalse trueOrFalse) {
        this.trueOrFalse = trueOrFalse;
    }

    private void setTrueOrFalse(boolean isTrue) {
        trueOrFalse.isTrue(isTrue);
    }
}
