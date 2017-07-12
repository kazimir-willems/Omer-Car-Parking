package omer.parking.com.proxy;

import com.google.gson.Gson;

import java.io.IOException;

import omer.parking.com.util.URLManager;
import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.GetNotificationResponseVo;

public class GetNotificationProxy extends BaseProxy {

    public GetNotificationResponseVo run(int userID, int parkingTag) throws IOException {
        String param = "?user_id=" + userID + "&parking_tag=" + parkingTag;

        String contentString = getPlain(URLManager.getNotificationURL() + param);

        GetNotificationResponseVo responseVo = new Gson().fromJson(contentString, GetNotificationResponseVo.class);

        return responseVo;
    }
}
