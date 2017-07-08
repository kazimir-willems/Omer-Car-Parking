package omer.parking.com.event;

import omer.parking.com.vo.SetOfficeResponseVo;
import omer.parking.com.vo.SetStatusResponseVo;

public class SetStatusEvent {
    private SetStatusResponseVo responseVo;

    public SetStatusEvent(SetStatusResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public SetStatusResponseVo getResponse() {
        return responseVo;
    }
}
