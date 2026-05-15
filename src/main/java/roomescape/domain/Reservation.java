package roomescape.domain;

import lombok.Getter;
import roomescape.domain.exception.ReservationCancellationException;
import roomescape.domain.exception.ReservationModificationException;

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
        validateRequiredValues(date, time);

        this.id = id;
        this.customerName = customerName;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation create(
            final String name,
            final LocalDate date,
            final ReservationTime reservationTime,
            final Theme theme,
            final LocalDateTime now
    ) {
        final Reservation reservation = new Reservation(
                null,
                Name.from(name),
                date,
                reservationTime,
                theme
        );

        reservation.validateNotPast(now);
        return reservation;
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

    public Reservation changeSchedule(
            final LocalDate date,
            final ReservationTime time,
            final LocalDateTime now
    ) {
        final Reservation changed = new Reservation(
                id,
                customerName,
                date,
                time,
                theme
        );

        changed.validateNotPast(now);
        return changed;
    }

    public String getCustomerName() {
        return customerName.getName();
    }

    public void validateCancelableByCustomer(final LocalDate today) {
        if (!canChangeByCustomer(today)) {
            throw new ReservationCancellationException();
        }
    }

    public void validateModifiableByCustomer(final LocalDate today) {
        if (!canChangeByCustomer(today)) {
            throw new ReservationModificationException();
        }
    }

    private boolean canChangeByCustomer(final LocalDate today) {
        return date.isAfter(today);
    }

    private void validateRequiredValues(final LocalDate date, final ReservationTime time) {
        if (date == null) {
            throw new IllegalArgumentException("예약일을 입력해야 합니다.");
        }

        if (time == null) {
            throw new IllegalArgumentException("예약 시간을 입력해야 합니다.");
        }
    }

    private void validateNotPast(final LocalDateTime now) {
        if (isPast(now)) {
            throw new IllegalArgumentException("과거 시간으로는 예약할 수 없습니다.");
        }
    }

    private boolean isPast(final LocalDateTime now) {
        return reservationDateTime().isBefore(now);
    }

    private LocalDateTime reservationDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }
}
