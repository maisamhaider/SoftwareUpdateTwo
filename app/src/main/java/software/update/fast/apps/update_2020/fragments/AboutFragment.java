package software.update.fast.apps.update_2020.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import software.update.fast.apps.update_2020.R;


public class AboutFragment extends Fragment {


    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);


        view.findViewById(R.id.share_cl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUs();

            }
        });
        view.findViewById(R.id.rateUs_cl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateUs();
            }
        });
        view.findViewById(R.id.about_cl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.about_dialog_layout, null, false);
                Button closebtn = view.findViewById(R.id.aboutUsMoreApp_btn);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true).setView(view);

                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                TextView appVersion_tv = view.findViewById(R.id.appVersion_tv);

                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.cancel();
                        }
                    }
                });

                try {
                    PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    String versionName = packageInfo.versionName;
                    appVersion_tv.setText(versionName);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    public void rateUs() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
    }

    public void shareUs() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + getContext().getPackageName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}