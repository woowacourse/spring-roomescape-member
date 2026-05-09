package roomescape.domain;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class Reservation {

    private final Long id;
    private final Name customerName;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(final Long id, final Name customerName, final LocalDate date, final ReservationTime time, final Theme theme) {
        validateDateTime(date, time);

        this.id = id;
        this.customerName = customerName;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation create(final String name, final LocalDate date, final ReservationTime reservationTime, final Theme theme) {
        return new Reservation(
                null,
                Name.from(name),
                date,
                reservationTime,
                theme
        );
    }

    public static Reservation of(
            final Long id,
            final String name,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme) {
        return new Reservation(
                id,
                Name.from(name),
                date,
                time,
                theme
        );
    }

    public String getCustomerName() {
        return customerName.getName();
    }

    private void validateDateTime(final LocalDate date, final ReservationTime time) {
        if (date == null) {
            throw new IllegalArgumentException("예약일을 입력해야 합니다.");
        }

        validateNotPast(date, time);
    }

    private void validateNotPast(final LocalDate date, final ReservationTime time) {
        final LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());

        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("현재 이전 시간으로는 예약할 수 없습니다.");
        }
    }
}
