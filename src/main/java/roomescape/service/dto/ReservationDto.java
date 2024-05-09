package roomescape.service.dto;

import roomescape.controller.request.ReservationRequest;
import roomescape.model.LoginMember;

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

    public static ReservationDto from(ReservationRequest reservationRequest, LoginMember member) {
        LocalDate date = reservationRequest.getDate();
        long timeId = reservationRequest.getTimeId();
        long themeId = reservationRequest.getThemeId();
        if (member == null) {
            return new ReservationDto(date, timeId, themeId, reservationRequest.getMemberId());
        }
        return new ReservationDto(date, timeId, themeId, member.getId());
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
