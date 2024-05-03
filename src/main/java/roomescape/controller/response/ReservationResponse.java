package roomescape.controller.response;

import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

import java.time.LocalDate;

public class ReservationResponse {

    private final long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTimeResponse timeResponse;
    private final ThemeResponse themeResponse;

    private ReservationResponse(long id, String name, LocalDate date, ReservationTimeResponse timeResponse, ThemeResponse themeResponse) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeResponse = timeResponse;
        this.themeResponse = themeResponse;
    } // TODO: if private, cannot parse?

    public static ReservationResponse from(Reservation reservation) {
        ReservationTime time = reservation.getTime();
        Theme theme = reservation.getTheme();
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                new ReservationTimeResponse(time.getId(), time.getStartAt()),
                new ThemeResponse(theme.getThemeId(), theme.getName(), theme.getDescription(), theme.getThumbnail()));
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

    public ReservationTimeResponse getTimeResponse() {
        return timeResponse;
    }

    public ThemeResponse getThemeResponse() {
        return themeResponse;
    }
}
