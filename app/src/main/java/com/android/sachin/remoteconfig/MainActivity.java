package com.android.sachin.remoteconfig;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import static com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings.*;

public class MainActivity extends AppCompatActivity {

    FirebaseRemoteConfig remoteConfig;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.change_button);

        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        remoteConfig = FirebaseRemoteConfig.getInstance();
        remoteConfig.setConfigSettings(remoteConfigSettings);
        remoteConfig.setDefaults(R.xml.remote_config);

        fetch();

    }

    private void fetch() {
        long cacheExpiration = 3600;
        if (remoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        remoteConfig.fetch(cacheExpiration).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                    Log.d("Success", "Fetch Succeeded");
                    remoteConfig.activateFetched();
                    String buttonLabel = remoteConfig.getString("my_button");
                    button.setText(buttonLabel);
                    Log.d("Fail", "Fetch Failed");
            }

        });

    }

}
