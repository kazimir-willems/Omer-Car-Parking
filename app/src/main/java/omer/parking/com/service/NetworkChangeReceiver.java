package omer.parking.com.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import omer.parking.com.consts.NetworkStateConsts;
import omer.parking.com.event.GetNotificationEvent;
import omer.parking.com.task.GetNotificationTask;
import omer.parking.com.util.NetworkUtil;
import omer.parking.com.util.SharedPrefManager;
import omer.parking.com.vo.GetNotificationResponseVo;


/**
 * Created by Caesar on 4/25/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static int oldStatus = -1;
    private Context ctx;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.ctx = context;

        int status = NetworkUtil.getConnectivityStatusString(context);
        if(oldStatus == status)
            return;
        if(status == NetworkStateConsts.NETWORK_WIFI_CONNECTED || status == NetworkStateConsts.NETWORK_MOBILE_DATA_CONNECTED) {
            Log.v("TrackingApp", "Connection Detected");
            if(SharedPrefManager.getInstance(ctx).getNoConnectionAction() == 1) {
                GetNotificationTask task = new GetNotificationTask();
                task.execute(SharedPrefManager.getInstance(ctx).getUserID(), 1);
            } else if (SharedPrefManager.getInstance(ctx).getNoConnectionAction() == 2) {
                GetNotificationTask task = new GetNotificationTask();
                task.execute(SharedPrefManager.getInstance(ctx).getUserID(), 0);
            }
        }
        oldStatus = status;
    }
}