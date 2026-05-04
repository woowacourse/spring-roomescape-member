package roomescape.reservation;

import java.time.LocalDate;
import roomescape.theme.Theme;
import roomescape.time.ReservationTime;

public class Reservation {

    private Long id;
    private final String userName;
    private final Theme theme;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, String userName, Theme theme, LocalDate date, ReservationTime time) {
        this.id = id;
        this.userName = userName;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Reservation(String userName, Theme theme, LocalDate date, ReservationTime time) {
        this.userName = userName;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
