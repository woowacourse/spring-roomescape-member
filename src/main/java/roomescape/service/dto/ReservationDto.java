package roomescape.service.dto;

import roomescape.controller.request.ReservationRequest;

import java.time.LocalDate;

public class ReservationDto {

    private final String name;
    private final LocalDate date;
    private final long timeId;
    private final long themeId;

    public ReservationDto(String name, LocalDate date, Long timeId, Long themeId) {
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public static ReservationDto from(ReservationRequest reservationRequest) {
        return new ReservationDto(reservationRequest.getName(),
                reservationRequest.getDate(),
                reservationRequest.getTimeId(),
                reservationRequest.getThemeId());
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public long getTimeId() {
        return timeId;
    }

    public long getThemeId() {
        return themeId;
    }
}
