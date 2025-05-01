package roomescape.business.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {

    private Long id;

    private final String name;
    private final LocalDate date;
    private final PlayTime playTime;
    private final Theme theme;

    public Reservation(
            final String name,
            final LocalDate date,
            final PlayTime playTime,
            final Theme theme
    ) {
        this(null, name, date, playTime, theme);
    }

    private Reservation(
            final Long id,
            final String name,
            final LocalDate date,
            final PlayTime playTime,
            final Theme theme
    ) {
        validateNonNull(name, date, playTime, theme);
        validateNameIsNotBlank(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.playTime = playTime;
        this.theme = theme;
    }

    public boolean isBefore(final LocalDateTime dateTime) {
        final LocalDateTime reservationDateTime = LocalDateTime.of(date, playTime.getStartAt());

        return reservationDateTime.isBefore(dateTime);
    }

    private void validateNameIsNotBlank(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("name이 empty 입니다.");
        }
    }

    private void validateNonNull(
            final String name,
            final LocalDate date,
            final PlayTime playTime,
            final Theme theme
    ) {
        if (name == null) {
            throw new IllegalArgumentException("name이 null 입니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("date가 null 입니다.");
        }
        if (playTime == null) {
            throw new IllegalArgumentException("time이 null 입니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("theme가 null 입니다.");
        }
    }

    public static Reservation createWithId(
            final Long id,
            final String name,
            final LocalDate date,
            final PlayTime playTime,
            final Theme theme
    ) {
        if (id == null) {
            throw new IllegalArgumentException("id가 null 입니다.");
        }

        return new Reservation(id, name, date, playTime, theme);
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

    public PlayTime getPlayTime() {
        return playTime;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
               && Objects.equals(date, that.date) && Objects.equals(playTime, that.playTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, playTime);
    }
}
