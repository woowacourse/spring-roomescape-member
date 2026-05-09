package roomescape.reservation.controller.dto;

import java.time.format.DateTimeFormatter;

import roomescape.time.domain.ReservationTime;

public record ReservationTimeResponseDto(Long id, String startAt, String endAt) {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static ReservationTimeResponseDto from(ReservationTime time) {
        if (time == null) {
            return null;
        }
        return new ReservationTimeResponseDto(
                time.getId(),
                time.getStartAt().format(TIME_FORMATTER),
                time.getEndAt().format(TIME_FORMATTER)
        );
    }
}
