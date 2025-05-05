package roomescape.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.domain.exception.ReservationException.InvalidReservationTimeException;
import roomescape.domain.vo.ReservationDetails;

public record Reservation(
        Long id,
        String name,
        LocalDate date,
        ReservationTime time,
        ReservationTheme theme
) {

    public Reservation {
        validateNotBlank(name, date);
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
            throw new InvalidReservationTimeException("예약시간이 과거시간이 될 수 없습니다. 미래시간으로 입력해주세요.");
        }
    }

    public Reservation assignId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("할당할 id는 null이 될 수 없습니다.");
        }
        return new Reservation(id, name, date, time, theme);
    }

    private void validateNotBlank(String name, LocalDate date) {
        if (name == null) {
            throw new IllegalArgumentException("예약자명은 null이 될 수 없습니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("예약자명은 비어 있을 수 없습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("날짜는 null이 될 수 없습니다.");
        }
    }
}
