package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;
import lombok.Getter;
import roomescape.global.exception.ForbiddenException;

@Getter
public class Reservation {

    private final Long id;
    private final String name;
    private final Theme theme;
    private final LocalDate date;
    private final ReservationTime time;
    private final ReservationStatus status;

    private Reservation(Long id, String name, LocalDate date, Theme theme, ReservationTime time,
                        ReservationStatus status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
        this.status = status;
    }

    public static Reservation create(String name, LocalDate date, Theme theme, ReservationTime time) {
        validateRequiredFields(name, date, theme, time);
        validateReservableDateTime(date, time);

        return new Reservation(null, name, date, theme, time, ReservationStatus.RESERVED);
    }

    public static Reservation restore(Long id, String name, LocalDate date, Theme theme, ReservationTime time,
                                      ReservationStatus status) {
        return new Reservation(id, name, date, theme, time, status);
    }

    private static void validateReservableDateTime(LocalDate date, ReservationTime time) {
        if (!time.isAvailableAt(date)) {
            throw new IllegalArgumentException("현재보다 이전 시간대로 예약할 수 없습니다.");
        }
    }

    private static void validateRequiredFields(String name, LocalDate date, Theme theme, ReservationTime time) {
        validateName(name);
        validateTheme(theme);
        validateDateTime(date, time);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 정보는 비어있을 수 없습니다.");
        }
    }

    private static void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("테마 정보는 비어있을 수 없습니다.");
        }
    }

    private static void validateDateTime(LocalDate date, ReservationTime time) {
        if (date == null || time == null) {
            throw new IllegalArgumentException("예약 날짜 및 시간 정보는 비어있을 수 없습니다.");
        }
    }

    public Reservation update(LocalDate date, ReservationTime time) {
        validateDateTime(date, time);
        validateReservableDateTime(date, time);

        return restore(this.id, this.name, date, this.theme, time, this.status);
    }

    public Reservation cancel() {
        if (!time.isAvailableAt(date)) {
            throw new IllegalArgumentException("이미 지난 예약은 취소할 수 없습니다.");
        }

        return restore(this.id, this.name, this.date, this.theme, this.time, ReservationStatus.CANCELED);
    }

    public void validateOwner(String name) {
        if (!Objects.equals(this.name, name)) {
            throw new ForbiddenException("예약자 명이 일치하지 않습니다.");
        }
    }
}
