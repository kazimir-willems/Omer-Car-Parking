package omer.parking.com.event;

import omer.parking.com.vo.GetLotResponseVo;
import omer.parking.com.vo.IncLotResponseVo;

public class IncLotEvent {
    private IncLotResponseVo responseVo;

    public IncLotEvent(IncLotResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public IncLotResponseVo getResponse() {
        return responseVo;
    }
}
