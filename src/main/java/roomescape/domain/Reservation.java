package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {

    private Long id;
    private final Person person;
    private final ReservationDate date;
    private final ReservationTime reservationTime;

    public Reservation(Long id, Person person, ReservationDate date,
        ReservationTime reservationTime) {
        this.id = id;
        this.person = person;
        this.date = date;
        this.reservationTime = reservationTime;
    }

    public Reservation(Person person, ReservationDate date, ReservationTime reservationTime) {
        this(null, person, date, reservationTime);
    }

    public void validateDateTime(
        ReservationDate date,
        ReservationTime reservationTime,
        LocalDateTime currentDateTime) {
        if (date.isCurrentDay(currentDateTime.toLocalDate()) &&
            reservationTime.isBefore(currentDateTime.toLocalTime())) {
            throw new IllegalArgumentException("과거 시간으로는 예약할 수 없습니다.");
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getPersonName() {
        return person.getName();
    }

    public LocalDate getDate() {
        return date.getDate();
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public long getTimeId() {
        return reservationTime.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
