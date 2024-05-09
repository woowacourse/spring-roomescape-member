package roomescape.service.dto;

import roomescape.controller.request.ReservationRequest;

import java.time.LocalDate;

public class ReservationDto {

    private final LocalDate date;
    private final long timeId;
    private final long themeId;
    private final long memberId;

    public ReservationDto(LocalDate date, Long timeId, Long themeId, Long memberId) {
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
        this.memberId = memberId;
    }

    public static ReservationDto from(ReservationRequest reservationRequest) {
        return new ReservationDto(
                reservationRequest.getDate(),
                reservationRequest.getTimeId(),
                reservationRequest.getThemeId(),
                reservationRequest.getMemberId());
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

    public long getMemberId() {
        return memberId;
    }
}
