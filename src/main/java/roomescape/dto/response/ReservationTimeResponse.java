package roomescape.dto.response;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(
        Long id,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        LocalTime startAt,
        boolean isBooked
) {

    public static ReservationTimeResponse of(ReservationTime reservationTime, boolean isBooked) {
        return new ReservationTimeResponse(reservationTime.id(), reservationTime.startAt(), isBooked);
    }

    public static ReservationTimeResponse withoutBook(ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.id(), reservationTime.startAt(), false);
    }
}
