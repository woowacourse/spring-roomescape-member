package roomescape.presentation.dto.response;

import java.time.LocalDate;

public record ReservationResponseDto(
        long id,
        LocalDate date,
        MemberResponseDto member,
        ReservationTimeResponseDto time,
        ReservationThemeResponseDto theme
) {
}
