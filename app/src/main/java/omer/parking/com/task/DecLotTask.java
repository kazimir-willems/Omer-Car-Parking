package omer.parking.com.task;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import omer.parking.com.event.DecLotEvent;
import omer.parking.com.event.IncLotEvent;
import omer.parking.com.proxy.DecLotProxy;
import omer.parking.com.proxy.IncLotProxy;
import omer.parking.com.vo.DecLotResponseVo;
import omer.parking.com.vo.IncLotResponseVo;

public class DecLotTask extends AsyncTask<Integer, Void, DecLotResponseVo> {

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected DecLotResponseVo doInBackground(Integer... params) {
        DecLotProxy simpleProxy = new DecLotProxy();

        int id = params[0];
        try {
            final DecLotResponseVo responseVo = simpleProxy.run(id);

            return responseVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(DecLotResponseVo responseVo) {
        EventBus.getDefault().post(new DecLotEvent(responseVo));
    }
}