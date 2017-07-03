package omer.parking.com.task;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import omer.parking.com.event.IncLotEvent;
import omer.parking.com.proxy.GetLotProxy;
import omer.parking.com.proxy.IncLotProxy;
import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.IncLotResponseVo;

public class IncLotTask extends AsyncTask<Integer, Void, IncLotResponseVo> {

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected IncLotResponseVo doInBackground(Integer... params) {
        IncLotProxy simpleProxy = new IncLotProxy();

        int id = params[0];
        try {
            final IncLotResponseVo responseVo = simpleProxy.run(id);

            return responseVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(IncLotResponseVo responseVo) {
        EventBus.getDefault().post(new IncLotEvent(responseVo));
    }
}