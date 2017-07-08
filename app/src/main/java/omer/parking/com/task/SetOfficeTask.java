package omer.parking.com.task;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import omer.parking.com.event.IncLotEvent;
import omer.parking.com.event.SetOfficeEvent;
import omer.parking.com.proxy.IncLotProxy;
import omer.parking.com.proxy.SetOfficeProxy;
import omer.parking.com.vo.IncLotResponseVo;
import omer.parking.com.vo.SetOfficeResponseVo;

public class SetOfficeTask extends AsyncTask<Integer, Void, SetOfficeResponseVo> {

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected SetOfficeResponseVo doInBackground(Integer... params) {
        SetOfficeProxy simpleProxy = new SetOfficeProxy();

        int userId = params[0];
        int officeId = params[1];
        try {
            final SetOfficeResponseVo responseVo = simpleProxy.run(userId, officeId);

            return responseVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(SetOfficeResponseVo responseVo) {
        EventBus.getDefault().post(new SetOfficeEvent(responseVo));
    }
}