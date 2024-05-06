package roomescape.reservation.domain;

import java.util.Objects;
import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.reservation.handler.exception.CustomException;
import roomescape.reservation.handler.exception.CustomBadRequest;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validateName(name);

        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateName(String name) {
        if (name.isEmpty() || name.length() > 10) {
            throw new CustomException(CustomBadRequest.INVALID_NAME_LENGTH);
        }
    }

    public boolean isSameDateTime(Reservation reservation) {
        return date.isEqual(reservation.date) && time.equals(reservation.time);
    }

    public boolean isBefore(LocalDateTime localDateTime) {
        return LocalDateTime.of(date, time.getStartAt())
                .isBefore(localDateTime);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
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
    public int hashCode() {
        return Objects.hash(id);
    }
}
