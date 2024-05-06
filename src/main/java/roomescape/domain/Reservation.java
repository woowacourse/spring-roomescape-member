package roomescape.domain;

import roomescape.exception.BadRequestException;

import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validate(name, date, time, theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validate(String name, LocalDate date, ReservationTime time, Theme theme) {
        validateNullField(name, date, time, theme);
        validateName(name);
    }

    private void validateNullField(String name, LocalDate date, ReservationTime time, Theme theme) {
        if (name == null || date == null || time == null || theme == null) {
            throw new BadRequestException("예약 필드에는 빈 값이 들어올 수 없습니다.");
        }
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new BadRequestException("이름은 공백일 수 없습니다.");
        }
    }

    public Reservation(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        this(null, name, date, reservationTime, theme);
    }

    public Reservation(Long id, Reservation reservation) {
        this(id, reservation.name, reservation.date, reservation.time, reservation.theme);
    }

    public boolean isDuplicated(Reservation other) {
        return date.equals(other.date)
                && time.getId().equals(other.time.getId())
                && theme.getId().equals(other.theme.getId());
    }

    public boolean isSameUser(Reservation other) {
        return name.equals(other.getName());
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
}
