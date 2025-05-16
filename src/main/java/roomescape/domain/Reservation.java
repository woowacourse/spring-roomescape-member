package roomescape.domain;

import java.time.LocalDate;

public record Reservation(
        Long id,
        Member member,
        LocalDate date,
        ReservationTime time,
        ReservationTheme theme
) {

    public Reservation {
        if (date == null) {
            throw new IllegalArgumentException("날짜는 null이 될 수 없습니다.");
        }
    }

    public Reservation(Member member, LocalDate date, ReservationTime time, ReservationTheme theme) {
        this(null, member, date, time, theme);
    }

    public Reservation withId(Long id) {
        return new Reservation(id, member, date, time, theme);
    }
}
