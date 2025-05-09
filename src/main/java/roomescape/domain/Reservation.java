package roomescape.domain;

import java.time.LocalDate;

public record Reservation(
        Long id,
        User user,
        LocalDate date,
        ReservationTime time,
        ReservationTheme theme
) {

    public Reservation {
        if (date == null) {
            throw new IllegalArgumentException("날짜는 null이 될 수 없습니다.");
        }
    }

    public Reservation(User user, LocalDate date, ReservationTime time, ReservationTheme theme) {
        this(null, user, date, time, theme);
    }

    public Reservation withId(Long id) {
        return new Reservation(id, user, date, time, theme);
    }
}
