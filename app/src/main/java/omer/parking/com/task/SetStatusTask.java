package omer.parking.com.task;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import omer.parking.com.event.SetOfficeEvent;
import omer.parking.com.event.SetStatusEvent;
import omer.parking.com.proxy.SetOfficeProxy;
import omer.parking.com.proxy.SetStatusProxy;
import omer.parking.com.vo.SetOfficeResponseVo;
import omer.parking.com.vo.SetStatusResponseVo;

public class SetStatusTask extends AsyncTask<Integer, Void, SetStatusResponseVo> {

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected SetStatusResponseVo doInBackground(Integer... params) {
        SetStatusProxy simpleProxy = new SetStatusProxy();

        int userId = params[0];
        int status = params[1];
        try {
            final SetStatusResponseVo responseVo = simpleProxy.run(userId, status);

            return responseVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(SetStatusResponseVo responseVo) {
        EventBus.getDefault().post(new SetStatusEvent(responseVo));
    }
}