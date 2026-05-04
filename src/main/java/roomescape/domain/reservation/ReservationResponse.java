package roomescape.domain.reservation;

import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeResponse;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeResponse;

import java.time.LocalDate;

public class ReservationResponse {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTimeResponse time;
    private final ThemeResponse theme;

    private ReservationResponse(Long id, String name, LocalDate date, ReservationTimeResponse time, ThemeResponse theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static ReservationResponse from(Reservation reservation) {
        ReservationTimeResponse reservationTimeResponse = ReservationTimeResponse.from(reservation.getTime());
        ThemeResponse themeResponse = ThemeResponse.from(reservation.getTheme());
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(), reservationTimeResponse, themeResponse);
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
