package roomescape.domain;

import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.regex.Pattern;

public class Reservation {

    private final Long id;
    private final MemberName memberName;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, MemberName memberName, ReservationDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.memberName = Objects.requireNonNull(memberName);
        this.date = Objects.requireNonNull(date);
        this.time = Objects.requireNonNull(time);
        this.theme = Objects.requireNonNull(theme);
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

    public static Reservation create(String name, LocalDate date, ReservationTime time, Theme theme) {
        validateNotPast(date, time.getStartAt());

        return new Reservation(null, new MemberName(name), new ReservationDate(date), time, theme);
    }

    private static void validateNotPast(LocalDate date, LocalTime time) {
        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("과거의 날짜와 시간은 예약할 수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}