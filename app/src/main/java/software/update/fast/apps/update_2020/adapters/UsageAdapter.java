package software.update.fast.apps.update_2020.adapters;

import android.annotation.SuppressLint;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import bot.box.appusage.model.AppData;
import software.update.fast.apps.update_2020.R;
import software.update.fast.apps.update_2020.annotations.MyAnnotation;
import software.update.fast.apps.update_2020.utils.Apps;
import software.update.fast.apps.update_2020.utils.MathSolver;


public class UsageAdapter extends RecyclerView.Adapter<UsageAdapter.appHolder> {

    private Context context;
    private List<AppData> apps;
     Apps ap;
    MathSolver mathSolver;


    public UsageAdapter(Context context, List<AppData> apps) {
        this.context = context;
        this.apps = apps;
        if (context != null)
        {
            context.getPackageManager();
        }
        ap = new Apps(context);
        mathSolver = new MathSolver();

    }

    public void setApps(List<AppData> apps) {
        this.apps = apps;
    }

    @NonNull
    @Override
    public appHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.apps_usage_item_xml, parent, false);

        return new appHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull appHolder holder, int position) {

        final String appPkg = apps.get(position).mPackageName;
        String appName = ap.appInformation(appPkg, MyAnnotation.APP_NAME);
         String appLastLaunchTime =new SimpleDateFormat("dd:MM:yyyy", Locale.getDefault()).format(new Date(apps.get(position).mEventTime));
        int appLaunchTimes  = apps.get(position).mCount;
        String appUsageTime  = TimeUnit.MILLISECONDS.toMinutes(apps.get(position).mUsageTime)+" m";


         holder.lnchAmount_tv.setText((appLaunchTimes)+"Times");
        holder.lLaunchTime_tv.setText(appLastLaunchTime);
        holder.appName_tv.setText(appName);
        holder.sTime_tv.setText(appUsageTime);
         Glide.with(context).load((Drawable)ap.appInformation(appPkg, MyAnnotation.APP_ICON)).placeholder(R.mipmap.ic_launcher).into(holder.usage_iv);

    }



    @Override
    public int getItemCount() {
        return apps.size();
    }



    public static class appHolder extends RecyclerView.ViewHolder {
        ImageView usage_iv;
        TextView appName_tv,lnchAmount_tv,sTime_tv,lLaunchTime_tv;
         public appHolder(@NonNull View itemView) {
            super(itemView);
             usage_iv = itemView.findViewById(R.id.usage_iv);
             lnchAmount_tv = itemView.findViewById(R.id.lnchAmount_tv);
             sTime_tv = itemView.findViewById(R.id.sTime_tv);
             lLaunchTime_tv = itemView.findViewById(R.id.lLaunchTime_tv);
             appName_tv = itemView.findViewById(R.id.appName_tv);

        }
    }


}
