package roomescape.service.dto;

import roomescape.domain.Reservation;

public class ReservationResponseDto {

    private final long id;
    private final MemberResponse member;
    private final ThemeResponse theme;
    private final String date;
    private final ReservationTimeResponse time;

    public ReservationResponseDto(long id, MemberResponse member, ThemeResponse theme, String date,
                                  ReservationTimeResponse time) {
        this.id = id;
        this.member = member;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public ReservationResponseDto(Reservation reservation) {
        this(reservation.getId(),
                new MemberResponse(reservation),
                new ThemeResponse(reservation.getTheme()),
                reservation.getDate().toString(),
                new ReservationTimeResponse(reservation.getReservationTime()));
    }

    public long getId() {
        return id;
    }

    public MemberResponse getMember() {
        return member;
    }

    public ThemeResponse getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public ReservationTimeResponse getTime() {
        return time;
    }
}
