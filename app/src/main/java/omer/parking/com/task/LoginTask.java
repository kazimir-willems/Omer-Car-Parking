package omer.parking.com.task;

import android.os.AsyncTask;
import android.os.StrictMode;

import org.greenrobot.eventbus.EventBus;

import omer.parking.com.event.IncLotEvent;
import omer.parking.com.event.LoginEvent;
import omer.parking.com.proxy.IncLotProxy;
import omer.parking.com.proxy.LoginProxy;
import omer.parking.com.vo.IncLotResponseVo;
import omer.parking.com.vo.LoginResponseVo;

public class LoginTask extends AsyncTask<String, Void, LoginResponseVo> {

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected LoginResponseVo doInBackground(String... params) {
        LoginProxy simpleProxy = new LoginProxy();

        String username = params[0];
        String password = params[1];
        try {
            final LoginResponseVo responseVo = simpleProxy.run(username, password);

            return responseVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(LoginResponseVo responseVo) {
        EventBus.getDefault().post(new LoginEvent(responseVo));
    }
}