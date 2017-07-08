package omer.parking.com.proxy;

import com.google.gson.Gson;

import java.io.IOException;

import omer.parking.com.util.URLManager;
import omer.parking.com.vo.GetStatusResponseVo;
import omer.parking.com.vo.SetStatusResponseVo;

public class GetStatusProxy extends BaseProxy {

    public GetStatusResponseVo run(int userId) throws IOException {
        String param = "?user_id=" + userId;

        String contentString = getPlain(URLManager.getGetStatusURL() + param);

        GetStatusResponseVo responseVo = new Gson().fromJson(contentString, GetStatusResponseVo.class);

        return responseVo;
    }
}
