package roomescape.controller.time;

import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;

public record TimeResponse(Long id, String startAt, Boolean booked) {

    // TODO: from 함수 오버로딩 제거
    public static TimeResponse from(final ReservationTime time) {
        return TimeResponse.from(time, false);
    }

    public static TimeResponse from(final ReservationTime time, final Boolean booked) {
        return new TimeResponse(
                time.getId(),
                time.getStartAt().format(DateTimeFormatter.ofPattern("HH:mm")),
                booked
        );
    }
}
