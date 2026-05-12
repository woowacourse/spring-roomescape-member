package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.exception.UnprocessableException;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

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

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme, LocalDateTime now) {
        this(null, name, date, time, theme);
        validateDateTime(date, time, now);
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

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없습니다.");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("이름은 255자를 넘을 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("날짜는 비어 있을 수 없습니다.");
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new IllegalArgumentException("예약 시간은 비어있을 수 없습니다.");
        }
    }

    private void validateDateTime(LocalDate date, ReservationTime time, LocalDateTime now) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (reservationDateTime.isBefore(now)) {
            throw new UnprocessableException("지난 시간으로는 예약할 수 없습니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("테마는 비어있을 수 없습니다.");
        }
    }
}
