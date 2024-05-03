package roomescape.domain;

import java.time.LocalDate;

public class Reservation {
    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final ReservationTime time;
    private final RoomTheme theme;

    public Reservation(Name name, LocalDate date, ReservationTime time, RoomTheme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(Long id, Name name, LocalDate date, ReservationTime time, RoomTheme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation withId(Long id) {
        return new Reservation(id, name, date, time, theme);
    }

    public boolean hasSameDateTimeAndTheme(LocalDate date, ReservationTime reservationTime, Long roomThemeId) {
        return this.date.equals(date)
                && this.time.getStartAt().equals(reservationTime.getStartAt())
                && this.theme.getId().equals(roomThemeId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public RoomTheme getTheme() {
        return theme;
    }
}
