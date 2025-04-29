package roomescape.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.domain_entity.ReservationTime;

public record ReservationTimeResponseDto(
        long id, LocalTime startAt
) {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static ReservationTimeResponseDto from(ReservationTime reservationTime) {
        return new ReservationTimeResponseDto(reservationTime.getId(), reservationTime.getStartAt());
    }

    public String getStartAt() {
        return startAt.format(timeFormatter);
    }
}
