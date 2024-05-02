package roomescape.core.dto;

import roomescape.core.domain.Reservation;

public class ReservationResponse {
    private Long id;
    private String name;
    private String date;
    private ReservationTimeResponse time;
    private ThemeResponse theme;

    public ReservationResponse() {
    }

    public ReservationResponse(final Reservation reservation) {
        this(reservation.getId(), reservation);
    }

    public ReservationResponse(final Long id, final Reservation reservation) {
        this.id = id;
        this.name = reservation.getName();
        this.date = reservation.getDateString();
        this.time = new ReservationTimeResponse(reservation.getReservationTime());
        this.theme = new ThemeResponse(reservation.getTheme());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
