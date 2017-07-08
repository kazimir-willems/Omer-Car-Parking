package omer.parking.com.task;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import omer.parking.com.event.GetStatusEvent;
import omer.parking.com.event.SetStatusEvent;
import omer.parking.com.proxy.GetStatusProxy;
import omer.parking.com.proxy.SetStatusProxy;
import omer.parking.com.vo.GetStatusResponseVo;
import omer.parking.com.vo.SetStatusResponseVo;

public class GetStatusTask extends AsyncTask<Integer, Void, GetStatusResponseVo> {

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected GetStatusResponseVo doInBackground(Integer... params) {
        GetStatusProxy simpleProxy = new GetStatusProxy();

        int userId = params[0];
        try {
            final GetStatusResponseVo responseVo = simpleProxy.run(userId);

            return responseVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(GetStatusResponseVo responseVo) {
        EventBus.getDefault().post(new GetStatusEvent(responseVo));
    }
}