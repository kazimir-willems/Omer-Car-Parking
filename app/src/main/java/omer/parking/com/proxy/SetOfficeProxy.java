package omer.parking.com.proxy;

import com.google.gson.Gson;

import java.io.IOException;

import omer.parking.com.util.URLManager;
import omer.parking.com.vo.IncLotResponseVo;
import omer.parking.com.vo.SetOfficeResponseVo;

public class SetOfficeProxy extends BaseProxy {

    public SetOfficeResponseVo run(int userId, int officeId) throws IOException {
        String param = "?user_id=" + userId + "&office_id=" + officeId;

        String contentString = getPlain(URLManager.getSetOfficeURL() + param);

        SetOfficeResponseVo responseVo = new Gson().fromJson(contentString, SetOfficeResponseVo.class);

        return responseVo;
    }
}
