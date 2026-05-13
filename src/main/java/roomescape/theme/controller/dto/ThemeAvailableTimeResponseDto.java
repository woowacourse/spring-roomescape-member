package roomescape.theme.controller.dto;

import java.time.format.DateTimeFormatter;

import roomescape.time.domain.ReservationTime;

public record ThemeAvailableTimeResponseDto(Long id, String startAt) {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static ThemeAvailableTimeResponseDto from(ReservationTime time) {
        return new ThemeAvailableTimeResponseDto(time.getId(), time.getStartAt().format(TIME_FORMATTER));
    }
}

