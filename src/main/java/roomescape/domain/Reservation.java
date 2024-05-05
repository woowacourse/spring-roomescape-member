package roomescape.domain;

public class Reservation {

    private final Long id;
    private final String name;
    private final String date;
    private final ReservationTime time;
    private final ReservationTheme theme;

    public Reservation(Long id, String name, String date, ReservationTime time, ReservationTheme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
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

    public ReservationTime getTime() {
        return time;
    }

    public ReservationTheme getTheme() {
        return theme;
    }
}
