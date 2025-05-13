package roomescape.reservation.dto;

import java.time.LocalDate;

public record ReservationCommand(
        Long memberId,
        LocalDate date,
        Long themeId,
        Long timeId
) {

}
