package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.reservationtime.ReservationTime;

public class Reservation {

    private Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime reservationTime;

    public Reservation(final Long id, final String name, final LocalDate date, final ReservationTime reservationTime) {
        validateNull(name, date, reservationTime);
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
    }

    private void validateNull(String name, LocalDate date, ReservationTime reservationTime) {
        if (name == null || name.isBlank()) {
            throw new NullPointerException("이름은 빈 값일 수 없습니다.");
        }
        if (date == null) {
            throw new NullPointerException("예약일은 null일 수 없습니다.");
        }
        if (reservationTime == null) {
            throw new NullPointerException("예약시간은 null일 수 없습니다.");
        }
    }

    public static Reservation createWithoutId(final String name, final LocalDate date,
                                              final ReservationTime reservationTime) {
        validateDateTime(date, reservationTime);
        return new Reservation(null, name, date, reservationTime);
    }

    private static void validateDateTime(LocalDate date, ReservationTime reservationTime) {
        LocalDateTime dateTime = LocalDateTime.of(date, reservationTime.getStartAt());
        if (LocalDateTime.now().isAfter(dateTime)) {
            throw new IllegalArgumentException("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }
}
