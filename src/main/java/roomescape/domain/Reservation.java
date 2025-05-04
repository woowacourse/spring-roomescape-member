package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.exception.InvalidReservationException;

public class Reservation {

    private Long id;
    private final Person person;
    private final ReservationDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(Long id, Person person, ReservationDate date,
                       ReservationTime reservationTime, Theme theme) {
        this.id = id;
        this.person = person;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(Person person, ReservationDate date, ReservationTime reservationTime,
                       Theme theme) {
        this(null, person, date, reservationTime, theme);
    }

    public void validateDateTime(ReservationDate date, ReservationTime time, LocalDateTime currentDateTime) {
        LocalDate today = currentDateTime.toLocalDate();
        LocalTime todayTime = currentDateTime.toLocalTime();
        if (date.isCurrentDay(today) && time.isBefore(todayTime)) {
            throw new InvalidReservationException("과거 시간으로는 예약할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Theme getTheme() {
        return theme;
    }

    public long getTimeId() {
        return reservationTime.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
