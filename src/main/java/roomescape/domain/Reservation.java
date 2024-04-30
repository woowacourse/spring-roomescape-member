package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {
    private static final Long DEFAULT_ID_VALUE = 0L;

    private final long id;
    private final ClientName clientName;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(final ClientName clientName, final LocalDate date, final ReservationTime time) {
        this(DEFAULT_ID_VALUE, clientName, date, time);
    }

    public Reservation(final long id, final ClientName clientName, final LocalDate date, final ReservationTime time) {
        validateReservationDateAndTime(date, time.getStartAt());
        this.id = id;
        this.clientName = clientName;
        this.date = date;
        this.time = time;
    }

    private void validateReservationDateAndTime(final LocalDate date, final LocalTime time) {
        LocalDateTime reservationLocalDateTime = LocalDateTime.of(date, time);
        if (reservationLocalDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("현재 날짜보다 이전 날짜를 예약할 수 없습니다.");
        }
    }

    public Reservation initializeIndex(final long reservationId) {
        return new Reservation(reservationId, clientName, date, time);
    }

    public long getId() {
        return id;
    }

    public ClientName getClientName() {
        return clientName;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Reservation that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
