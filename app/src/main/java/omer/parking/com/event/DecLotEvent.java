package omer.parking.com.event;

import omer.parking.com.vo.DecLotResponseVo;
import omer.parking.com.vo.IncLotResponseVo;

public class DecLotEvent {
    private DecLotResponseVo responseVo;

    public DecLotEvent(DecLotResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public DecLotResponseVo getResponse() {
        return responseVo;
    }
}
