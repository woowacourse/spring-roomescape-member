package roomescape.reservation.controller.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.reservation.domain.ReservationTime;

public record ReservationTimeResponseDto(
    Long id,
    @JsonFormat(pattern = "HH:mm") LocalTime startAt,
    @JsonFormat(pattern = "HH:mm") LocalTime endAt
) {

    public static ReservationTimeResponseDto from(ReservationTime time) {
        if (time == null) {
            return null;
        }
        return new ReservationTimeResponseDto(time.getId(), time.getStartAt(), time.getEndAt());
    }
}
