package omer.parking.com.service;

/**
 * Created by Kazimir on 6/28/2017.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import omer.parking.com.R;
import omer.parking.com.event.GetLotEvent;
import omer.parking.com.task.GetRemainingLotTask;
import omer.parking.com.ui.LotInfoActivity;
import omer.parking.com.ui.MainActivity;
import omer.parking.com.ui.SettingsActivity;
import omer.parking.com.util.SharedPrefManager;
import omer.parking.com.vo.GetLotResponseVo;

public class GeofenceTransitionsIntentService extends IntentService {

    private static final String TAG = "GeofenceTransitions";

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, "Goefencing Error " + geofencingEvent.getErrorCode());
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
//            showNotification("Entering");
            if(!SharedPrefManager.getInstance(this).getInOffice()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        GetRemainingLotTask task = new GetRemainingLotTask();
                        task.execute(SharedPrefManager.getInstance(getApplicationContext()).getCurrentOfficeID());
                    }
                }).start();
            }
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
//            showNotification("Leaving");
            SharedPrefManager.getInstance(this).saveInOffice(false);
            if(SharedPrefManager.getInstance(this).getCameWithCar())
                showExitNotification();
        } else {
//            showNotification("Error", "Error");
        }
    }

    @Subscribe
    public void onGetLotEvent(GetLotEvent event) {
        GetLotResponseVo responseVo = event.getResponse();
        if (responseVo != null) {
            if(!SharedPrefManager.getInstance(this).getInOffice())
                showEnterNotification(responseVo.remain_lot);
        }
    }

    public void showExitNotification() {

        // 1. Create a NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // 2. Create a PendingIntent for AllGeofencesActivity
        Intent intent = new Intent(this, LotInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("enter_flag", 1);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri sound = Uri.parse(SharedPrefManager.getInstance(this).getDefaultTune());
        // 3. Create and send a notification
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.exiting_office))
                .setSound(sound)
                .setContentIntent(pendingNotificationIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getResources().getString(R.string.exiting_office)))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);
    }

    public void showEnterNotification(int remainSlot) {

        // 1. Create a NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // 2. Create a PendingIntent for AllGeofencesActivity
        Intent intent = new Intent(this, LotInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("enter_flag", 0);
        intent.putExtra("remaining_slot", remainSlot);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String soundUri = "";
        if(remainSlot == 0) {
            soundUri = SharedPrefManager.getInstance(this).getNoLotTune();
        } else {
            soundUri = SharedPrefManager.getInstance(this).getDefaultTune();
        }
        Uri sound = Uri.parse(soundUri);
        // 3. Create and send a notification
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.entering_office))
                .setSound(sound)
                .setContentIntent(pendingNotificationIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getResources().getString(R.string.entering_office)))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);
    }

    public void showNotification(String message) {

        // 1. Create a NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // 2. Create a PendingIntent for AllGeofencesActivity
        Intent intent = new Intent(this, LotInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("enter_flag", 1);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 3. Create and send a notification
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pendingNotificationIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(2, notification);
    }
}