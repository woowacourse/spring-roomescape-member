package roomescape.dto.reservation;

import java.time.LocalDate;

public record ReservationRequest(
        LocalDate date,
        Long timeId,
        Long themeId,
        Long memberId
) {

}
