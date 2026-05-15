package roomescape.domain;

import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationDate;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public ReservationDate getDate() {
        return date;
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

    public static Reservation create(MemberName name, ReservationDate date, ReservationTime time, Theme theme) {
        validateNotPast(date, time);

        return new Reservation(null, name, date, time, theme);
    }

    private static void validateNotPast(ReservationDate date, ReservationTime time) {
        if (LocalDateTime.of(date.value(), time.getStartAt()).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.PAST_DATE_RESERVATION);
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