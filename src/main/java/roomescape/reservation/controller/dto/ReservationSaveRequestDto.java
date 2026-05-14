package roomescape.reservation.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import roomescape.reservation.service.dto.ReservationSaveServiceDto;

public record ReservationSaveRequestDto(
        @NotBlank String name,
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @NotNull Long themeId,
        @NotNull Long timeId
) {

    public ReservationSaveServiceDto toServiceDto() {
        return new ReservationSaveServiceDto(name, date, themeId, timeId);
    }
}
