package roomescape.dto;

import java.time.format.DateTimeFormatter;
import roomescape.domain.Reservation;

public record ReservationResponse(Long id, String name, String date, ReservationTimeResponse time) {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static ReservationResponse from(Reservation reservation) {
        ReservationTimeResponse timeResponseDto = ReservationTimeResponse.from(reservation.getTime());
        String date = reservation.getDate().format(DATE_FORMATTER);
        return new ReservationResponse(
                reservation.getId(), reservation.getName().getValue(), date, timeResponseDto
        );
    }
}
