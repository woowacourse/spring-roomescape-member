package roomescape.controller.dto.request;

import roomescape.service.dto.request.ReservationTimeData;

import java.time.LocalTime;

public record ReservationTimeCreateRequest(
        LocalTime startAt
) {

    public ReservationTimeData toData() {
        return new ReservationTimeData(startAt);
    }
}
