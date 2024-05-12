package roomescape.dto.reservation.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record AdminReservationRequest(
        @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @NotNull @Positive Long timeId,
        @NotNull @Positive Long themeId,
        @NotNull @Positive Long memberId
) {
}
