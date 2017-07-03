package omer.parking.com.proxy;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import omer.parking.com.util.URLManager;
import omer.parking.com.vo.GetOfficeRequestVo;
import omer.parking.com.vo.GetOfficeResponseVo;

public class GetOfficeListProxy extends BaseProxy {

    public GetOfficeResponseVo run() throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();

        String contentString = getPlain(URLManager.getOfficeListURL());

        GetOfficeResponseVo responseVo = new GetOfficeResponseVo();

        try {
            JSONObject json = new JSONObject(contentString);
            responseVo.arrays = json.getString("arrays");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return responseVo;
    }
}
