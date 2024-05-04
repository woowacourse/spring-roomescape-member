package roomescape.controller.request;

import java.time.LocalTime;

import roomescape.exception.BadRequestException;

public class ReservationTimeRequest {

    private LocalTime startAt;

    public ReservationTimeRequest(LocalTime startAt) {
        validateTime(startAt);
        this.startAt = startAt;
    }

    private ReservationTimeRequest() {
    }

    private void validateTime(LocalTime startAt) {
        if (startAt == null) {
            throw new BadRequestException("시작 시간이 [%s]일 수 없습니다.".formatted(startAt));
        }
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
