package br.ufrn.gcmsmartparking.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import br.ufrn.gcmsmartparking.R;
import br.ufrn.gcmsmartparking.business.PreferencesUserTools;
import br.ufrn.gcmsmartparking.business.WebService;
import br.ufrn.gcmsmartparking.configs.Config;
import br.ufrn.gcmsmartparking.model.User;
import br.ufrn.gcmsmartparking.services.PushNotificationService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static TextView vacancy;
    private WebService webService;

    private WebService getInstanceWebService(){
        if(webService == null){
            this.webService = new WebService();
        }
        return webService;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.vacancy = (TextView) findViewById(R.id.vacancy);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                displayFirebaseRegId();
            }
        };

        PushNotificationService.setMainActivity(this);
        displayFirebaseRegId();

        try {
            String vaga = (String) getIntent().getExtras().get(getString(R.string.key_intent_vaga));
            if (vaga != null) {
                updateLayout(vaga);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateLayout(String vaga) {
        if (vacancy != null) {
            vacancy.setText(vaga);
        }
    }


    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString(getString(R.string.key_preference_token), null);

        String user = pref.getString(getString(R.string.key_preference_user), null);
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

}
