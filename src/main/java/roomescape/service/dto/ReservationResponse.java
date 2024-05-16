package roomescape.service.dto;

import roomescape.domain.Reservation;

public class ReservationResponse {

    private final long id;
    private final MemberResponse member;
    private final ThemeResponse theme;
    private final String date;
    private final ReservationTimeResponse time;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.member = new MemberResponse(reservation.getMember());
        this.theme = new ThemeResponse(reservation.getTheme());
        this.date = reservation.getDate().toString();
        this.time = new ReservationTimeResponse(reservation.getReservationTime());
    }

    public long getId() {
        return id;
    }

    public MemberResponse getMember() {
        return member;
    }

    public String getDate() {
        return date;
    }

    public ReservationTimeResponse getTime() {
        return time;
    }

    public ThemeResponse getTheme() {
        return theme;
    }
}
