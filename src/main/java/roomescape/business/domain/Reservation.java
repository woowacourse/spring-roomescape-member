package roomescape.business.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final PlayTime playTime;
    private final Theme theme;

    public Reservation(final Long id, final String name, final LocalDate date, final PlayTime playTime,
                       final Theme theme) {
        validateName(name);
        validateDate(date);

        this.id = id;
        this.name = name;
        this.date = date;
        this.playTime = playTime;
        this.theme = theme;
    }

    public Reservation(final String name, final LocalDate date, final PlayTime playTime, final Theme theme) {
        this(null, name, date, playTime, theme);
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name이 null 또는 empty 입니다.");
        }
    }

    private void validateDate(final LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("date가 null 입니다.");
        }
    }

    public boolean isBefore(final LocalDateTime dateTime) {
        final LocalDateTime reservationDateTime = LocalDateTime.of(date, playTime.getStartAt());

        return reservationDateTime.isBefore(dateTime);
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
}
