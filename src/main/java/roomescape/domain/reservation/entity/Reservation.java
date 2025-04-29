package roomescape.domain.reservation.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

    private static final int MAX_NAME_LENGTH = 25;
    private final Long id;
    private final String name;
    private final LocalDate reservationDate;
    private final ReservationTime reservationTime;

    public Reservation(Long id, String name, LocalDate reservationDate, ReservationTime reservationTime) {
        this.id = id;
        this.name = name;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        validateReservation();
    }

    public static Reservation withoutId(String name, LocalDate reservationDate, ReservationTime reservationTime) {
        return new Reservation(null, name, reservationDate, reservationTime);
    }

    private void validateReservation() {
        if (name == null || reservationDate == null || reservationTime == null) {
            throw new IllegalArgumentException("Reservation field cannot be null");
        }
        validateName();
    }

    private void validateName() {
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("invalid reservation name");
        }
    }

    public boolean existId() {
        return id != null;
    }

    public Long getId() {
        return id;
    }

    public Long getReservationTimeId() {
        return reservationTime.getId();
    }

    public String getName() {
        return name;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public LocalTime getReservationStratTime() {
        return reservationTime.getStartAt();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(reservationDate, that.reservationDate) && Objects.equals(
                reservationTime, that.reservationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, reservationDate, reservationTime);
    }
}
