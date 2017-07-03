package omer.parking.com.event;

import omer.parking.com.vo.GetOfficeResponseVo;

public class GetOfficeListEvent {
    private GetOfficeResponseVo responseVo;

    public GetOfficeListEvent(GetOfficeResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public GetOfficeResponseVo getResponse() {
        return responseVo;
    }
}
