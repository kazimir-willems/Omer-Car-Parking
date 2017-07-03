package omer.parking.com.task;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import omer.parking.com.event.GetOfficeListEvent;
import omer.parking.com.proxy.GetOfficeListProxy;
import omer.parking.com.vo.GetOfficeResponseVo;

public class GetOfficeListTask extends AsyncTask<String, Void, GetOfficeResponseVo> {

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected GetOfficeResponseVo doInBackground(String... params) {
        GetOfficeListProxy simpleProxy = new GetOfficeListProxy();
        try {
            final GetOfficeResponseVo responseVo = simpleProxy.run();

            return responseVo;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(GetOfficeResponseVo responseVo) {
        EventBus.getDefault().post(new GetOfficeListEvent(responseVo));
    }
}