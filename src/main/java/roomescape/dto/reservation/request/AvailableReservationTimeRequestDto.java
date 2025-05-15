package roomescape.dto.reservation.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AvailableReservationTimeRequestDto(

        @NotNull(message = "날짜는 필수입니다.")
        LocalDate date,

        @NotNull(message = "themeId는 필수 입니다.")
        Long themeId) {
}
