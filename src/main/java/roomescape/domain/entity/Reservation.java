package roomescape.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.domain.vo.ReservationDetails;

public record Reservation(
        Long id,
        String name,
        LocalDate date,
        ReservationTime time,
        ReservationTheme theme
) {

    public Reservation {
        validate(name, date);
    }

    public Reservation(String name, LocalDate date, ReservationTime time, ReservationTheme theme) {
        this(null, name, date, time, theme);
    }

    public static Reservation create(ReservationDetails details) {
        LocalDateTime requestedDateTime = LocalDateTime.of(details.date(), details.reservationTime().startAt());
        validateFutureTime(requestedDateTime);
        return new Reservation(details.name(), details.date(), details.reservationTime(), details.reservationTheme());
    }

    private static void validateFutureTime(LocalDateTime requestedDateTime) {
        if (requestedDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 지나간 시간으로 예약할 수 없습니다.");
        }
    }

    private void validate(String name, LocalDate date) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null이 될 수 없습니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("날짜는 null이 될 수 없습니다.");
        }
    }

    public Reservation withId(Long id) {
        return new Reservation(id, name, date, time, theme);
    }
}
