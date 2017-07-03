package omer.parking.com.event;

import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.GetOfficeResponseVo;

public class GetLotEvent {
    private GetLotResponseVo responseVo;

    public GetLotEvent(GetLotResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public GetLotResponseVo getResponse() {
        return responseVo;
    }
}
