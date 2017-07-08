package omer.parking.com.event;

import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.SetOfficeResponseVo;

public class SetOfficeEvent {
    private SetOfficeResponseVo responseVo;

    public SetOfficeEvent(SetOfficeResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public SetOfficeResponseVo getResponse() {
        return responseVo;
    }
}
