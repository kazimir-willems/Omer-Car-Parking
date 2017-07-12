package omer.parking.com.service;

/**
 * Created by Kazimir on 6/28/2017.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import omer.parking.com.R;
import omer.parking.com.event.GetLotEvent;
import omer.parking.com.event.GetNotificationEvent;
import omer.parking.com.event.GetStatusEvent;
import omer.parking.com.task.GetNotificationTask;
import omer.parking.com.task.GetRemainingLotTask;
import omer.parking.com.task.GetStatusTask;
import omer.parking.com.task.SetStatusTask;
import omer.parking.com.ui.LotInfoActivity;
import omer.parking.com.util.SharedPrefManager;
import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.GetNotificationResponseVo;
import omer.parking.com.vo.GetStatusResponseVo;

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
        /*ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean internetConnectFlag = conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;*/

//        boolean internetConnectFlag = true;
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.v("Notification", "Entered");

            SharedPrefManager.getInstance(getApplicationContext()).saveInOffice(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    GetNotificationTask task = new GetNotificationTask();
                    task.execute(SharedPrefManager.getInstance(getApplicationContext()).getUserID(), 1);

                    SharedPrefManager.getInstance(getApplicationContext()).saveAction(false);
                }
            }).start();
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.v("Notification", "Exited");

            SharedPrefManager.getInstance(getApplicationContext()).saveInOffice(false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    GetNotificationTask task = new GetNotificationTask();
                    task.execute(SharedPrefManager.getInstance(getApplicationContext()).getUserID(), 0);

                    SharedPrefManager.getInstance(getApplicationContext()).saveAction(false);
                }
            }).start();
        }
    }

    @Subscribe
    public void onGetNotificationEvent(GetNotificationEvent event) {
        final GetNotificationResponseVo responseVo = event.getResponse();
        if (responseVo != null && responseVo.success == 1) {
            switch(responseVo.notification) {
                case 0:
                    showNoSlotNotification();
                    break;
                case 1:
                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if(!SharedPrefManager.getInstance(getApplicationContext()).getAction()) {
                                showEnterNotification();
                                handler.postDelayed(this, 120000);
                            }
                        }
                    };

                    handler.post(runnable);
                    break;
                case 2:
                    final Handler exitHandler = new Handler();
                    Runnable exitRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if(!SharedPrefManager.getInstance(getApplicationContext()).getAction()) {
                                showExitNotification();
                                exitHandler.postDelayed(this, 120000);
                            }
                        }
                    };

                    exitHandler.post(exitRunnable);
                    break;
            }
        } else {
            if(SharedPrefManager.getInstance(getApplicationContext()).getInOffice()) {
                SharedPrefManager.getInstance(getApplicationContext()).saveNoConnectionAction(1);
            } else {
                SharedPrefManager.getInstance(getApplicationContext()).saveNoConnectionAction(2);
            }
        }
    }

    public void showExitNotification() {

        // 1. Create a NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // 2. Create a PendingIntent for AllGeofencesActivity
        Intent intent = new Intent(this, LotInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    public void showEnterNotification() {

        // 1. Create a NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // 2. Create a PendingIntent for AllGeofencesActivity
        Intent intent = new Intent(this, LotInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("enter_flag", 0);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri sound = Uri.parse(SharedPrefManager.getInstance(this).getDefaultTune());
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

    public void showNoSlotNotification() {

        // 1. Create a NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // 2. Create a PendingIntent for AllGeofencesActivity
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        Uri soundUri = Uri.parse(SharedPrefManager.getInstance(this).getNoLotTune());

        // 3. Create and send a notification
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(getResources().getString(R.string.no_parking_lot))
                .setContentIntent(pendingNotificationIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getResources().getString(R.string.no_parking_lot)))
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(2, notification);
    }
}