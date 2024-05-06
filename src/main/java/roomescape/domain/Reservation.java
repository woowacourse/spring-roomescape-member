package roomescape.domain;

import java.time.LocalDate;
import roomescape.exception.BadRequestException;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final RoomTheme theme;

    public Reservation(String name, LocalDate date, ReservationTime time, RoomTheme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, RoomTheme theme) {
        validateName(name);
        validateDate(date);
        validateReservationTime(time);
        validateRoomTheme(theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateRoomTheme(RoomTheme theme) {
        if (theme == null) {
            throw new BadRequestException("테마에 빈값을 입력할 수 없습니다.");
        }
    }

    private void validateReservationTime(ReservationTime time) {
        if (time == null) {
            throw new BadRequestException("시간에 빈값을 입력할 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new BadRequestException("날짜에 빈값을 입력할 수 없습니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("이름에 빈값을 입력할 수 없습니다.");
        }
    }

    public Reservation setId(Long id) {
        return new Reservation(id, name, date, time, theme);
    }

    public boolean hasDateTime(LocalDate date, ReservationTime reservationTime) {
        return this.date.equals(date)
                && this.time.getStartAt().equals(reservationTime.getStartAt());
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

    public RoomTheme getTheme() {
        return theme;
    }
}
