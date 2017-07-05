package omer.parking.com.proxy;

import com.google.gson.Gson;

import java.io.IOException;

import omer.parking.com.util.URLManager;
import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.LoginResponseVo;

public class LoginProxy extends BaseProxy {

    public LoginResponseVo run(String username, String password) throws IOException {
        String param = "?name=" + username + "&password=" + password;

        String contentString = getPlain(URLManager.getLoginURL() + param);

        LoginResponseVo responseVo = new Gson().fromJson(contentString, LoginResponseVo.class);

        return responseVo;
    }
}
