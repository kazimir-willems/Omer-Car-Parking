package omer.parking.com.task;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import omer.parking.com.event.GetLotEvent;
import omer.parking.com.event.GetNotificationEvent;
import omer.parking.com.proxy.GetLotProxy;
import omer.parking.com.proxy.GetNotificationProxy;
import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.GetNotificationResponseVo;

public class GetNotificationTask extends AsyncTask<Integer, Void, GetNotificationResponseVo> {

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected GetNotificationResponseVo doInBackground(Integer... params) {
        GetNotificationProxy simpleProxy = new GetNotificationProxy();

        int userId = params[0];
        int parkingTag = params[1];
        try {
            final GetNotificationResponseVo responseVo = simpleProxy.run(userId, parkingTag);

            return responseVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(GetNotificationResponseVo responseVo) {
        EventBus.getDefault().post(new GetNotificationEvent(responseVo));
    }
}