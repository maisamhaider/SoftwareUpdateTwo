package software.update.fast.apps.update_2020.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import software.update.fast.apps.update_2020.R;

public class SplashAct extends AppCompatActivity {
    Handler handler;
     private CheckBox accept_box;
    Button declineBtn;
    Button acceptBtn;
    View splash_v, terms_v;
    boolean previouslyStarted;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
         declineBtn = findViewById(R.id.decline_btn);
        acceptBtn = findViewById(R.id.accept_btn);
        accept_box = findViewById(R.id.termAndCondition_Cb);
        splash_v = findViewById(R.id.splash_v);
        terms_v = findViewById(R.id.terms_v);
        preferences = getSharedPreferences("myPref", MODE_PRIVATE  );
        editor = preferences.edit();

        previouslyStarted = preferences.getBoolean( "if_Accept",false);

        handler = new Handler();
        loadFun();
    }

    public void loadFun() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (previouslyStarted) {
                     startActivity(new Intent(SplashAct.this, MainActivity.class));
                    finish();
                } else {
                    acceptBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (accept_box.isChecked()) {
                                editor.putBoolean("if_Accept", true).commit();
                                startActivity(new Intent(SplashAct.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SplashAct.this, "Please check the box first", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    declineBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                    splash_v.setVisibility(View.GONE);
                    terms_v.setVisibility(View.VISIBLE);
                }
            }}, 3000);}


}