package omer.parking.com.event;

import omer.parking.com.vo.GetStatusResponseVo;
import omer.parking.com.vo.SetStatusResponseVo;

public class GetStatusEvent {
    private GetStatusResponseVo responseVo;

    public GetStatusEvent(GetStatusResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public GetStatusResponseVo getResponse() {
        return responseVo;
    }
}
