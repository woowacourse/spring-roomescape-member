package roomescape.domain;

import java.util.Objects;

public class Reservation {

    private static final int MAX_NAME_SIZE = 10;
    private final Long id;
    private final String name;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(String name, ReservationDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(Long id, String name, ReservationDate date, ReservationTime time, Theme theme) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank() || name.length() > MAX_NAME_SIZE) {
            throw new IllegalArgumentException("예약자 이름은 1글자 이상 10글자 이하만 입력해주세요.");
        }
    }

    public boolean isPast() {
        return date.isBeforeNow() || date.isToday() && time.isBeforeNow();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ReservationDate getReservationDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
