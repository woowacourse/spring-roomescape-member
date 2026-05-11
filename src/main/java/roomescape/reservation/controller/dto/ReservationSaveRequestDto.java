package roomescape.reservation.controller.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.reservation.service.dto.ReservationSaveServiceDto;

public record ReservationSaveRequestDto(
        String name,
        LocalDate date,
        Long themeId,
        @JsonFormat(pattern = "HH:mm") LocalTime time
) {

    public ReservationSaveServiceDto toServiceDto() {
        return new ReservationSaveServiceDto(name, date, themeId, time);
    }
}
