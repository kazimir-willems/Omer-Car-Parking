package omer.parking.com.proxy;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import omer.parking.com.util.URLManager;
import omer.parking.com.vo.DecLotResponseVo;
import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.GetOfficeResponseVo;

public class GetLotProxy extends BaseProxy {

    public GetLotResponseVo run(int id) throws IOException {
        String param = "?office_id=" + id;

        String contentString = getPlain(URLManager.getRemainingLotURL() + param);

        GetLotResponseVo responseVo = new Gson().fromJson(contentString, GetLotResponseVo.class);

        return responseVo;
    }
}
