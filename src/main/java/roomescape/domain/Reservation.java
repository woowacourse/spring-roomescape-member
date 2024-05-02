package roomescape.domain;

import static roomescape.exception.ExceptionType.EMPTY_DATE;
import static roomescape.exception.ExceptionType.EMPTY_NAME;
import static roomescape.exception.ExceptionType.EMPTY_THEME;
import static roomescape.exception.ExceptionType.EMPTY_TIME;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.exception.RoomescapeException;

public class Reservation implements Comparable<Reservation> {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validateName(name);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new RoomescapeException(EMPTY_THEME);
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new RoomescapeException(EMPTY_TIME);
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new RoomescapeException(EMPTY_DATE);
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new RoomescapeException(EMPTY_NAME);
        }
    }

    public Reservation(long id, Reservation reservationBeforeSave) {
        this(id, reservationBeforeSave.name, reservationBeforeSave.date, reservationBeforeSave.time,
                reservationBeforeSave.theme);
    }

    @Override
    public int compareTo(Reservation other) {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        LocalDateTime otherDateTime = LocalDateTime.of(other.date, other.time.getStartAt());
        return dateTime.compareTo(otherDateTime);
    }

    public boolean isBefore(LocalDateTime base) {
        return LocalDateTime.of(date, getTime()).isBefore(base);
    }

    public LocalTime getTime() {
        return time.getStartAt();
    }

    public boolean hasSameId(long id) {
        return this.id == id;
    }

    public boolean isReservationTimeOf(long id) {
        return this.time.isIdOf(id);
    }

    public boolean isDateOf(LocalDate date) {
        return this.date.equals(date);
    }

    public boolean isThemeOf(long id) {
        return this.theme.isIdOf(id);
    }

    public boolean isSameDateTime(Reservation beforeSave) {
        return LocalDateTime.of(this.date, this.getTime())
                .equals(LocalDateTime.of(beforeSave.date, beforeSave.getTime()));
    }

    public boolean isSameTheme(Reservation reservation) {
        return this.theme.equals(reservation.theme);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Reservation that = (Reservation) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return "Reservation{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", date=" + date +
               ", time=" + time +
               '}';
    }
}
