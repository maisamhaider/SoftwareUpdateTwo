package software.update.fast.apps.update_2020.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import software.update.fast.apps.update_2020.R;
import software.update.fast.apps.update_2020.annotations.MyAnnotation;
import software.update.fast.apps.update_2020.models.ApkModel;
import software.update.fast.apps.update_2020.utils.Apps;

public class SAppsAdapter extends RecyclerView.Adapter<SAppsAdapter.AppHolder> {

    private Context context;

    private Apps apps;
    String appPackage;
    String appName;
    String currentVersion;
    String newVersion;
    private ArrayList<ApkModel> list;


    public SAppsAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        apps = new Apps(context);

    }

    public void setScanningData(String appPackage, String appName, String currentVersion, String newVersion) {
        this.appPackage = appPackage;
        this.appName = appName;
        this.currentVersion = currentVersion;
        this.newVersion = newVersion;
        list.add(0,new ApkModel(appPackage,appName,currentVersion,newVersion));
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AppHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scaning_apps_item, parent, false);
        return new AppHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppHolder holder, int position) {
        String appPackage = list.get(position).getPackageName();
        String appName = list.get(position).getAppName();
        String currentVersion = list.get(position).getCurrentVersion();
        String newVersion = list.get(position).getNewVersion();

        Glide.with(context).load((Drawable) apps.appInformation(appPackage, MyAnnotation.APP_ICON)).placeholder(R.mipmap.ic_launcher).into(holder.app_iv);
        holder.scanningAppName_TV.setText(appName);
        holder.scanningAppCurrentVersion_TV.setText(currentVersion);
        holder.newVersion_tv.setText(newVersion);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AppHolder extends RecyclerView.ViewHolder {
        ImageView app_iv;
        TextView scanningAppName_TV, scanningAppCurrentVersion_TV, newVersion_tv;
         public AppHolder(@NonNull View itemView) {
            super(itemView);
             app_iv = itemView.findViewById(R.id.app_iv2);
            scanningAppName_TV = itemView.findViewById(R.id.scanningAppName_TV);
            scanningAppCurrentVersion_TV = itemView.findViewById(R.id.scanningAppCurrentVersion_TV);
             newVersion_tv = itemView.findViewById(R.id.newVersion_tv);

        }
    }
}
