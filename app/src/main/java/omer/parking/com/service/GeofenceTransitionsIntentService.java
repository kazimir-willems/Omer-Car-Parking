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
import omer.parking.com.event.GetStatusEvent;
import omer.parking.com.task.GetRemainingLotTask;
import omer.parking.com.task.GetStatusTask;
import omer.parking.com.task.SetStatusTask;
import omer.parking.com.ui.LotInfoActivity;
import omer.parking.com.util.SharedPrefManager;
import omer.parking.com.vo.GetLotResponseVo;
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
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.v("Notification", "Entered");

            SharedPrefManager.getInstance(this).saveInOffice(true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    GetStatusTask task = new GetStatusTask();
                    task.execute(SharedPrefManager.getInstance(getApplicationContext()).getUserID());
                }
            }).start();
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.v("Notification", "Exited");
            SharedPrefManager.getInstance(this).saveInOffice(false);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    GetStatusTask task = new GetStatusTask();
                    task.execute(SharedPrefManager.getInstance(getApplicationContext()).getUserID());
                }
            }).start();

            /*if(SharedPrefManager.getInstance(this).getCameWithCar() == 1) {     // Ask if leaving
                showExitNotification();
            } else if(SharedPrefManager.getInstance(this).getCameWithCar() == 3 || SharedPrefManager.getInstance(this).getCameWithCar() == 2) {  //No Slot or Not came with car
                SharedPrefManager.getInstance(this).saveCameWithCar(3);
            }*/
        }
    }

    @Subscribe
    public void onGetLotEvent(GetLotEvent event) {
        final GetLotResponseVo responseVo = event.getResponse();
        if (responseVo != null) {
            if (responseVo.remain_lot == 0) {
                SetStatusTask task = new SetStatusTask();
                task.execute(SharedPrefManager.getInstance(getApplicationContext()).getUserID(), 0);
                showNoSlotNotification();
                return;
            } else if (responseVo.remain_lot > 0) {
                SharedPrefManager.getInstance(getApplicationContext()).saveAction(false);

                final Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(!SharedPrefManager.getInstance(getApplicationContext()).getAction()) {
                            showEnterNotification(responseVo.remain_lot);
                            handler.postDelayed(this, 120000);
                        }
                    }
                };

                handler.post(runnable);

            }
        }

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onGetStatusEvent(GetStatusEvent event) {
        GetStatusResponseVo responseVo = event.getResponse();
        if(responseVo != null) {

            if(SharedPrefManager.getInstance(getApplicationContext()).getInOffice()) {
                if (responseVo.status == 3) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            GetRemainingLotTask task = new GetRemainingLotTask();
                            task.execute(SharedPrefManager.getInstance(getApplicationContext()).getCurrentOfficeID());
                        }
                    }).start();

                }
            } else {
                if(responseVo.status == 1) {
                    SharedPrefManager.getInstance(getApplicationContext()).saveAction(false);

                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if(!SharedPrefManager.getInstance(getApplicationContext()).getAction()) {
                                showExitNotification();
                                handler.postDelayed(this, 120000);
                            }
                        }
                    };

                    handler.post(runnable);
                } else if (responseVo.status == 3 || responseVo.status == 2 || responseVo.status == 0) {
                    SetStatusTask task = new SetStatusTask();
                    task.execute(SharedPrefManager.getInstance(getApplicationContext()).getUserID(), 3);
                }
            }
        }

        EventBus.getDefault().unregister(this);
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

    public void showEnterNotification(int remainSlot) {

        // 1. Create a NotificationManager
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // 2. Create a PendingIntent for AllGeofencesActivity
        Intent intent = new Intent(this, LotInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("enter_flag", 0);
        intent.putExtra("remaining_slot", remainSlot);
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