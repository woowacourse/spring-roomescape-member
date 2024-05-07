package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final ClientName clientName;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(
            final Long id,
            final ClientName clientName,
            final ReservationDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        this.id = id;
        this.clientName = clientName;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation createInstanceWithoutId(
            final String clientName,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        final ReservationDate reservationDate = new ReservationDate(date);
        validateReservationDateAndTime(reservationDate, time);

        return new Reservation(
                null,
                new ClientName(clientName),
                reservationDate,
                time,
                theme
        );
    }

    private static void validateReservationDateAndTime(final ReservationDate date, final ReservationTime time) {
        LocalDateTime reservationLocalDateTime = LocalDateTime.of(date.getValue(), time.getStartAt());
        if (reservationLocalDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("예약 일시는 현재 시간 이후여야 합니다.");
        }
    }

    public static Reservation createInstance(
            final Long id,
            final String clientName,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        return new Reservation(
                id,
                new ClientName(clientName),
                new ReservationDate(date),
                time,
                theme
        );
    }

    public Reservation copyWithId(final Long reservationId) {
        return new Reservation(reservationId, clientName, date, time, theme);
    }

    public Long getId() {
        return id;
    }

    public ClientName getClientName() {
        return clientName;
    }

    public ReservationDate getDate() {
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
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
