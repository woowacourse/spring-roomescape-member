package roomescape.reservation.controller.dto;

import java.time.LocalDate;

import roomescape.reservation.service.dto.ReservationUpdateServiceDto;

public record ReservationUpdateRequestDto(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {
    public ReservationUpdateServiceDto toServiceDto(Long id) {
        return new ReservationUpdateServiceDto(id, name, date, timeId, themeId);
    }
}
