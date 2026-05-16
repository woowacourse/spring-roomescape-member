package roomescape.dto.reservationTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AvailableReservationTimeRequest(
        @NotNull
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜는 YYYY-MM-DD 형식이여야 합니다.")
        String date,
        long themeId
) {
}
