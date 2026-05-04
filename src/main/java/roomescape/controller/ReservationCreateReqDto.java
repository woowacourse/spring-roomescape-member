package roomescape.controller;

import java.time.LocalDate;

public record ReservationCreateReqDto(
        String name,
        LocalDate date,
        Long timeId
) {
}
