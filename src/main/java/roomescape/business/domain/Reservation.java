package roomescape.business.domain;

import java.time.LocalDate;

public final class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final ReservationTheme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, ReservationTheme theme) {
        if (name == null || date == null || time == null || theme == null) {
            throw new IllegalArgumentException("예약 정보는 null일 수 없습니다.");
        }
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime time, ReservationTheme theme) {
        this(null, name, date, time, theme);
    }

    public boolean isSameReservation(Reservation otherReservation) {
        return this.date.equals(otherReservation.date)
                && this.time.getId().equals(otherReservation.time.getId())
                && this.theme.getId().equals(otherReservation.theme.getId());
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

    public ReservationTime getTime() {
        return time;
    }

    public ReservationTheme getTheme() {
        return theme;
    }
}
