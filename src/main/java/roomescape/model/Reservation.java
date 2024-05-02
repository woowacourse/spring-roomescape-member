package roomescape.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final UserName userName;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(final String name, final LocalDate date, final ReservationTime time, final Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(final Long id, final String name, final LocalDate date, final ReservationTime time, final Theme theme) {
        validateDate(date);
        this.id = id;
        this.userName = new UserName(name);
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private static void validateDate(final LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("날짜가 비어 있습니다.");
        }
    }

    public boolean isTime(final ReservationTime time) {
        return this.time.equals(time);
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return userName.getValue();
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
