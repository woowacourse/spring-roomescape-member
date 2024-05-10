package roomescape.service.dto;

import roomescape.domain.Reservation;

public class ReservationResponseDto {

    private final long id;
    private final MemberResponseDto member;
    private final ThemeResponseDto theme;
    private final String date;
    private final ReservationTimeResponseDto time;

    public ReservationResponseDto(long id, MemberResponseDto member, ThemeResponseDto theme, String date,
                                  ReservationTimeResponseDto time) {
        this.id = id;
        this.member = member;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public ReservationResponseDto(Reservation reservation) {
        this(reservation.getId(),
                new MemberResponseDto(reservation),
                new ThemeResponseDto(reservation.getTheme()),
                reservation.getDate().toString(),
                new ReservationTimeResponseDto(reservation.getReservationTime()));
    }

    public long getId() {
        return id;
    }

    public MemberResponseDto getMember() {
        return member;
    }

    public ThemeResponseDto getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public ReservationTimeResponseDto getTime() {
        return time;
    }
}
