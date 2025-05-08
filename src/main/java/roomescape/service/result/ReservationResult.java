package roomescape.service.result;

import java.time.LocalDate;

public record ReservationResult(
        Long id,
        LoginMemberResult loginMemberResult,
        LocalDate date,
        ReservationTimeResult time,
        ThemeResult theme
) {
}
