package br.ufrn.gcmsmartparking.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import br.ufrn.gcmsmartparking.activities.MainActivity;
import br.ufrn.gcmsmartparking.R;

/**
 * Created by Victor Oliveira on 16/05/17.
 * Email: victorlopesjg@gmail.com
 */

public class PushNotificationService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private ObjectMapper objectMapper = null;
    private static MainActivity mMainActivity = null;

    public static void setMainActivity(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        Log.d(TAG, "Message: " + remoteMessage.getData());
        String message = "";
        if(remoteMessage.getData().get("errors") != null && !remoteMessage.getData().get("error").equals("")){
            message = remoteMessage.getData().get("errors");
            sendNotification("Indisponível,", message);
        } else {
            message = remoteMessage.getData().get("number");
            sendNotification("Sua vaga é: " + remoteMessage.getData().get("number"), remoteMessage.getData().get("number"));
        }

        if (mMainActivity != null) {
            final String finalMessage = message;
            mMainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMainActivity.updateLayout(finalMessage);
                }
            });

        }

    }

    private ObjectMapper getObjectMapperInstance() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return objectMapper;
    }


    private void sendNotification(String messageBody, String vaga) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(getString(R.string.key_intent_vaga), vaga);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Smart Parking")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
