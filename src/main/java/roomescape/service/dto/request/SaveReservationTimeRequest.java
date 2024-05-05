package roomescape.service.dto.request;

import roomescape.domain.ReservationTime;

import java.time.LocalTime;

public record SaveReservationTimeRequest(LocalTime startAt) {

    public static ReservationTime toEntity(SaveReservationTimeRequest request) {
        return new ReservationTime(request.startAt());
    }
}
