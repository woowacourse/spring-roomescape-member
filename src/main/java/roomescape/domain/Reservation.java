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
    private final Theme theme;

    public Reservation(
            final ClientName clientName,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme) {
        this(DEFAULT_ID_VALUE, clientName, date, time, theme);
    }

    public Reservation(
            final long id,
            final ClientName clientName,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme) {
        validateDate(date);
        validateReservationDateAndTime(date, time.getStartAt());

        this.id = id;
        this.clientName = clientName;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateDate(final LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("날짜 정보는 공백을 입력할 수 없습니다.");
        }
    }

    private void validateReservationDateAndTime(final LocalDate date, final LocalTime time) {
        LocalDateTime reservationLocalDateTime = LocalDateTime.of(date, time);
        if (reservationLocalDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("현재 날짜보다 이전 날짜를 예약할 수 없습니다.");
        }
    }

    public Reservation initializeIndex(final long reservationId) {
        return new Reservation(reservationId, clientName, date, time, theme);
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

    public Theme getTheme() {
        return theme;
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
