package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

import roomescape.domain.ReservationTime;

public record ReservationTimeStatusResponse(
        Long id,

        @JsonFormat(pattern = "HH:mm")
        LocalTime startAt,

        boolean available) {

    public static ReservationTimeStatusResponse of(ReservationTime reservationTime, boolean available) {

        return new ReservationTimeStatusResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(),
                available
        );
    }
}
