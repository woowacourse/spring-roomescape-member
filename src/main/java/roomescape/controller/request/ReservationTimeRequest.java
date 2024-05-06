package roomescape.controller.request;

import roomescape.exception.BadRequestException;

import java.time.LocalTime;

public class ReservationTimeRequest {

    private LocalTime startAt;

    public ReservationTimeRequest(LocalTime startAt) {
        validateNull(startAt);
        this.startAt = startAt;
    }

    private ReservationTimeRequest() {
    }

    private void validateNull(LocalTime startAt) {
        if (startAt == null) {
            throw new BadRequestException("[ERROR] 요청된 데이터에 null 혹은 비어있는 값이 존재합니다.");
        }
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
