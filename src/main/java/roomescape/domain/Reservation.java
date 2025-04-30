package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.exception.reservation.ReservationFieldRequiredException;
import roomescape.exception.reservation.ReservationInPastException;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validate(name, date, time, theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation withId(Long id) {
        return new Reservation(id, this.name, this.date, this.time, this.theme);
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


    private void validate(String name, LocalDate date, ReservationTime time, Theme theme) {
        validateName(name);
        validateDate(date);
        validateTime(time);
        validateInPast(date, time.getStartAt());
        validateTheme(theme);
    }

    //TODO: dto단에서 검증되는 유효성 검사 빼기  (2025-04-30, 수, 11:25)
    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException();
        }
    }

    private void validateInPast(LocalDate date, LocalTime startAt) {
        if (LocalDateTime.now().isAfter(LocalDateTime.of(date, startAt))) {
            throw new ReservationInPastException();
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new ReservationFieldRequiredException("시간");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ReservationFieldRequiredException("이름");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new ReservationFieldRequiredException("날짜");
        }
    }

    public Theme getTheme() {
        return theme;
    }
}
