package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
