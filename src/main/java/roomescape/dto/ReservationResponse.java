package roomescape.dto;

import java.time.LocalDate;
import roomescape.model.Reservation;

public class ReservationResponse {
    private Long id;
    private String name;
    private LocalDate date;
    private TimeResponse time;
    private ThemeResponse theme;

    public ReservationResponse(Long id, String name, LocalDate date, TimeResponse time, ThemeResponse theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                TimeResponse.from(reservation.getTime()), ThemeResponse.from(reservation.getTheme()));
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

    public TimeResponse getTime() {
        return time;
    }
    public ThemeResponse getTheme() {
        return theme;
    }
}
