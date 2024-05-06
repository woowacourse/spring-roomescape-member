package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final ClientName clientName;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public static Reservation of(
            final String clientName,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        validateReservationTimeAndTheme(time, theme);

        final ReservationDate reservationDate = new ReservationDate(date);
        return new Reservation(
                null,
                new ClientName(clientName),
                reservationDate,
                time,
                theme
        );
    }

    private static void validateReservationTimeAndTheme(final ReservationTime reservationTime, final Theme theme) {
        if (reservationTime == null || theme == null) {
            throw new IllegalArgumentException("예약 시간과 테마 정보는 필수입니다.");
        }
    }

    public static Reservation of(
            final Long id,
            final String clientName,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        validateReservationTimeAndTheme(time, theme);

        return new Reservation(
                id,
                new ClientName(clientName),
                new ReservationDate(date),
                time,
                theme
        );
    }

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

    public Reservation initializeIndex(final Long reservationId) {
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
        return Objects.equals(id, that.id)
                && Objects.equals(clientName, that.clientName)
                && Objects.equals(date, that.date)
                && Objects.equals(time, that.time)
                && Objects.equals(theme, that.theme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientName, date, time, theme);
    }
}
