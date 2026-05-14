package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UserReservationUpdateRequestDto(
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @NotNull Long timeId
) {
}