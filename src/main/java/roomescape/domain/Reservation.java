package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import roomescape.domain.util.Validator;

public record Reservation(Long id, LocalDate date, Member member, ReservationTime time, Theme theme) {

    public Reservation(LocalDate date, Member member, ReservationTime time, Theme theme) {
        this(null, date, member, time, theme);
    }

    public Reservation {
        Validator.nonNull(date, member, time, theme);
    }

    public boolean isBefore(LocalDateTime currentDateTime) {
        LocalDate currentDate = currentDateTime.toLocalDate();
        if (date.isBefore(currentDate)) {
            return true;
        }
        if (date.isAfter(currentDate)) {
            return false;
        }
        return time.isBefore(currentDateTime.toLocalTime());
    }

    public Long getMemberId() {
        return member.id();
    }

    public Long getTimeId() {
        return time.id();
    }

    public Long getThemeId() {
        return theme.id();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        if (id == null || that.id == null)
            return Objects.equals(date, that.date) && Objects.equals(time, that.time);
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        if (id == null)
            return Objects.hash(date, time);
        return Objects.hash(id);
    }
}
