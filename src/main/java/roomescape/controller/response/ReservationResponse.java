package roomescape.controller.response;

import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

import java.time.LocalDate;

public class ReservationResponse {

    private final long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTimeResponse time;
    private final ThemeResponse theme;

    private ReservationResponse(long id, String name, LocalDate date, ReservationTimeResponse time, ThemeResponse theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    } // TODO: if private, cannot parse?

    public static ReservationResponse from(Reservation reservation) {
        ReservationTime time = reservation.getTime();
        Theme theme = reservation.getTheme();
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                new ReservationTimeResponse(time.getId(), time.getStartAt()),
                new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail()));
    }

    public long getId() {
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
