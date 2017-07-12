package omer.parking.com.proxy;

import com.google.gson.Gson;

import java.io.IOException;

import omer.parking.com.util.URLManager;
import omer.parking.com.vo.SetOfficeResponseVo;
import omer.parking.com.vo.SetStatusResponseVo;

public class SetStatusProxy extends BaseProxy {

    public SetStatusResponseVo run(int userId, int status) throws IOException {
        String param = "?user_id=" + userId + "&response_status=" + status;

        String contentString = getPlain(URLManager.getSetStatusURL() + param);

        SetStatusResponseVo responseVo = new Gson().fromJson(contentString, SetStatusResponseVo.class);

        return responseVo;
    }
}
