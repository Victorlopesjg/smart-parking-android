package br.ufrn.gcmsmartparking.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import br.ufrn.gcmsmartparking.R;
import br.ufrn.gcmsmartparking.business.WebService;
import br.ufrn.gcmsmartparking.configs.Config;
import br.ufrn.gcmsmartparking.model.User;

/**
 * Created by Victor Oliveira on 16/05/17.
 * Email: victorlopesjg@gmail.com
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    private WebService webService;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        storeRegIdInPref(refreshedToken);

        sendRegistrationToServer(refreshedToken);

        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        Log.e(TAG, "sendRegistrationToServer: " + token);

        try {
            User user = new User();
            user.setToken(token);
            getWebServiceInstance().put(user, getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.key_preference_token), token);
        editor.commit();
    }

    private WebService getWebServiceInstance() {
        if (webService == null) {
            webService = new WebService();
        }

        return webService;
    }
}
