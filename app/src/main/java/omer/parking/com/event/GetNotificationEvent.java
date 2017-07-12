package omer.parking.com.event;

import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.GetNotificationResponseVo;

public class GetNotificationEvent {
    private GetNotificationResponseVo responseVo;

    public GetNotificationEvent(GetNotificationResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public GetNotificationResponseVo getResponse() {
        return responseVo;
    }
}
