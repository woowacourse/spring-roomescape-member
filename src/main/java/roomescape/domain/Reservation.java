package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {

    private final Long id;
    private final ReservationName reservationName;
    private final Theme theme;
    private final ReservationDate date;
    private final ReservationTime time;

    public Reservation(Long id, ReservationName reservationName, Theme theme, ReservationDate date,
                       ReservationTime time) {
        this.id = id;
        this.reservationName = reservationName;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Reservation(Long id, String name, Long themeId, String themeName, String description, String thumbnail,
                       String date, Long timeId, String time) {
        this(id,
                new ReservationName(name),
                new Theme(themeId, themeName, description, thumbnail),
                new ReservationDate(date),
                new ReservationTime(timeId, time));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return reservationName.getValue();
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public LocalDate getDate() {
        return date.getDate();
    }

    public ReservationTime getReservationTime() {
        return time;
    }

    public LocalTime getLocalTime() {
        return time.getStartAt();
    }

    public Long getTimeId() {
        return time.getId();
    }

    public LocalTime getTimeStartAt() {
        return time.getStartAt();
    }
}
