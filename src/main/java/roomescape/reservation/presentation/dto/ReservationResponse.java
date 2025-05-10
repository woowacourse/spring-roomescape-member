package roomescape.reservation.presentation.dto;

import java.time.LocalDate;
import roomescape.member.presentation.dto.MemberNameResponse;
import roomescape.reservation.domain.Reservation;

public class ReservationResponse {
    private Long id;
    private MemberNameResponse member;
    private ThemeResponse theme;
    private LocalDate date;
    private ReservationTimeResponse time;

    private ReservationResponse() {
    }

    public ReservationResponse(final Reservation reservation) {
        this.id = reservation.getId();
        this.member = new MemberNameResponse(reservation.getMember().getName());
        this.theme = new ThemeResponse(reservation.getTheme());
        this.date = reservation.getDate().getReservationDate();
        this.time = new ReservationTimeResponse(reservation.getReservationTime());
    }

    public Long getId() {
        return id;
    }

    public MemberNameResponse getMember() {
        return member;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTimeResponse getTime() {
        return time;
    }

    public ThemeResponse getTheme() {
        return theme;
    }

}
