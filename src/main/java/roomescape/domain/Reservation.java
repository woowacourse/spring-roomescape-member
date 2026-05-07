package roomescape.domain;

import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationLocalDate;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final MemberName memberName;
    private final ReservationLocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, MemberName memberName, ReservationLocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.memberName = memberName;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, new MemberName(name), ReservationLocalDate.createForSave(date), time, theme);
    }

    public Long getId() {
        return id;
    }

    public MemberName getName() {
        return memberName;
    }

    public LocalDate getDateValue() {
        return date.value();
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Reservation withId(Long key) {
        return new Reservation(key, this.memberName, this.date, this.time, this.theme);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id != null
            && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}