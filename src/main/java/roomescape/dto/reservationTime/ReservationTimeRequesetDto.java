package roomescape.dto.reservationTime;

import java.time.format.DateTimeFormatter;

public record ReservationTimeRequesetDto(
        String startAt
) {

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
}
