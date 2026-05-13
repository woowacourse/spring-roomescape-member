package roomescape.reservation.controller.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.reservation.service.dto.ReservationUpdateServiceDto;

public record ReservationUpdateRequestDto(
        @NotBlank String name,
        @NotNull LocalDate date,
        @NotNull Long timeId,
        @NotNull Long themeId
) {
    public ReservationUpdateServiceDto toServiceDto(Long id) {
        return new ReservationUpdateServiceDto(id, name, date, timeId, themeId);
    }
}
