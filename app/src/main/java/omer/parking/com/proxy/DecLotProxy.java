package omer.parking.com.proxy;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import omer.parking.com.util.URLManager;
import omer.parking.com.vo.DecLotResponseVo;
import omer.parking.com.vo.IncLotResponseVo;

public class DecLotProxy extends BaseProxy {

    public DecLotResponseVo run(int id) throws IOException {
        String param = "?id=" + id;

        String contentString = getPlain(URLManager.getDecLotURL() + param);

        DecLotResponseVo responseVo = new Gson().fromJson(contentString, DecLotResponseVo.class);

        return responseVo;
    }
}
