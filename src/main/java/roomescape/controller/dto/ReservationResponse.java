package roomescape.controller.dto;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;

public class ReservationResponse {
    private final long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTimeResponse time;
    private final Theme theme;

    public ReservationResponse(long id, String name, LocalDate date, ReservationTimeResponse time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static ReservationResponse toDto(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(),
                reservation.getDate().getDate(),
                ReservationTimeResponse.toDto(reservation.getTime()),
                reservation.getTheme());
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

    public Theme getTheme() {
        return theme;
    }
}
