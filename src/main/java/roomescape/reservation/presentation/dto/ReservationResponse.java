package roomescape.reservation.presentation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;

public class ReservationResponse {
    private Long id;
    private String name;
    private ThemeResponse theme;
    private LocalDate date;
    private ReservationTimeResponse time;

    private ReservationResponse() {
    }

    public ReservationResponse(final Reservation reservation) {
        this.id = reservation.getId();
        this.name = reservation.getName().getName();
        this.theme = new ThemeResponse(reservation.getTheme());
        this.date = reservation.getDate().getReservationDate();
        this.time = new ReservationTimeResponse(reservation.getReservationTime());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
