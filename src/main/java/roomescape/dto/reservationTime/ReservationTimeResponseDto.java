package roomescape.dto.reservationTime;

import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;

// TODO: record 인자값 컨벤션?
public record ReservationTimeResponseDto(Long id, String startAt) {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static ReservationTimeResponseDto from(ReservationTime reservationTime) {
        Long id = reservationTime.getId();
        String startAt = TIME_FORMATTER.format(reservationTime.getStartAt());

        return new ReservationTimeResponseDto(id, startAt);
    }
}
