package omer.parking.com.task;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import omer.parking.com.event.GetLotEvent;
import omer.parking.com.event.GetOfficeListEvent;
import omer.parking.com.proxy.GetLotProxy;
import omer.parking.com.proxy.GetOfficeListProxy;
import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.GetOfficeResponseVo;

public class GetRemainingLotTask extends AsyncTask<Integer, Void, GetLotResponseVo> {

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected GetLotResponseVo doInBackground(Integer... params) {
        GetLotProxy simpleProxy = new GetLotProxy();

        int id = params[0];
        try {
            final GetLotResponseVo responseVo = simpleProxy.run(id);

            return responseVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(GetLotResponseVo responseVo) {
        EventBus.getDefault().post(new GetLotEvent(responseVo));
    }
}