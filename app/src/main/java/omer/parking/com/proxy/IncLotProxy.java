package omer.parking.com.proxy;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import omer.parking.com.util.URLManager;
import omer.parking.com.vo.DecLotResponseVo;
import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.IncLotResponseVo;

public class IncLotProxy extends BaseProxy {

    public IncLotResponseVo run(int id) throws IOException {
        String param = "?id=" + id;

        String contentString = getPlain(URLManager.getIncLotURL() + param);

        IncLotResponseVo responseVo = new Gson().fromJson(contentString, IncLotResponseVo.class);

        return responseVo;
    }
}
