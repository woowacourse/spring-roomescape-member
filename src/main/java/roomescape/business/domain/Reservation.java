package roomescape.business.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {

    private Long id;

    private final String name;
    private final LocalDate date;
    private final PlayTime playTime;

    public Reservation(final String name, final LocalDate date, final PlayTime playTime) {
        this(null, name, date, playTime);
    }

    private Reservation(final Long id, final String name, final LocalDate date, final PlayTime playTime) {
        validateNonNull(name, date, playTime);
        validateNameIsNotBlank(name);
        this.id = id;
        this.name = name;
        this.date = date;
        this.playTime = playTime;
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
            final String name, final LocalDate date, final PlayTime playTime
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
    }

    public static Reservation createWithId(
            final Long id,
            final String name,
            final LocalDate date,
            final PlayTime playTime
    ) {
        if (id == null) {
            throw new IllegalArgumentException("id가 null 입니다.");
        }

        return new Reservation(id, name, date, playTime);
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

    public PlayTime getTime() {
        return playTime;
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
