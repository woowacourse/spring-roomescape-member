package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final MemberName memberName;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, MemberName memberName, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.memberName = memberName;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this(id, new MemberName(name), date, time, theme);
    }

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, new MemberName(name), date, time, theme);
    }

    public boolean isSameDate(LocalDate localDate) {
        return date.isEqual(localDate);
    }

    public boolean isSameTime(ReservationTime reservationTime) {
        return time.equals(reservationTime);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return memberName.getValue();
    }

    public LocalDate getReservationDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    @Override
    public boolean equals(Object o) {
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
        return Objects.hashCode(id);
    }
}
