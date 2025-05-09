package roomescape.service.result;

import java.time.LocalDate;

public record ReservationResult(
        Long id,
        MemberResult memberResult,
        LocalDate date,
        ReservationTimeResult time,
        ThemeResult theme
) {
}
