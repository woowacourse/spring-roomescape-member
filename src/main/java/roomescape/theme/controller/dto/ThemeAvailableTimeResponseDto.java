package roomescape.theme.controller.dto;

import roomescape.reservation.domain.ReservationTime;

public record ThemeAvailableTimeResponseDto(Long id, String startAt) {
    public static ThemeAvailableTimeResponseDto from(ReservationTime time) {
        return new ThemeAvailableTimeResponseDto(time.getId(), time.getStartAt());
    }
}

