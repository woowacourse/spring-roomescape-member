package roomescape.core.dto;

import roomescape.core.domain.Reservation;

public class ReservationResponseDto {
    private Long id;
    private MemberResponseDto member;
    private String date;
    private ReservationTimeResponseDto time;
    private ThemeResponseDto theme;

    public ReservationResponseDto() {
    }

    public ReservationResponseDto(final Reservation reservation) {
        this(reservation.getId(), reservation);
    }

    public ReservationResponseDto(final Long id, final Reservation reservation) {
        this.id = id;
        this.member = new MemberResponseDto(reservation.getMember());
        this.date = reservation.getDateString();
        this.time = new ReservationTimeResponseDto(reservation.getTime());
        this.theme = new ThemeResponseDto(reservation.getTheme());
    }

    public Long getId() {
        return id;
    }

    public MemberResponseDto getMember() {
        return member;
    }

    public String getDate() {
        return date;
    }

    public ReservationTimeResponseDto getTime() {
        return time;
    }

    public ThemeResponseDto getTheme() {
        return theme;
    }
}
