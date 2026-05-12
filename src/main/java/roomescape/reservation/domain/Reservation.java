package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.theme.domain.Theme;

public class Reservation {
    private Long id;
    private String name;
    private LocalDate date;
    private LocalTime time;
    private Theme theme;
    private ReservationStatus status;

    private Reservation(Long id, String name, LocalDate date, LocalTime time, Theme theme, ReservationStatus status) {
        validate(name, date, time, theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.status = status;
    }

    public static Reservation create(String name, LocalDate date, LocalTime time, Theme theme) {
        Reservation reservation = new Reservation(null, name, date, time, theme, ReservationStatus.RESERVED);
        validatePast(date, time);
        return reservation;
    }

    public static Reservation load(Long id, String name, LocalDate date, LocalTime time, Theme theme, ReservationStatus status) {
        validateId(id);
        return new Reservation(id, name, date, time, theme, status);
    }

    private static void validate(String name, LocalDate date, LocalTime time, Theme theme) {
        validateName(name);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 필수입니다.");
        }
    }

    private static void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }
    }

    private static void validateTime(LocalTime time) {
        if (time == null) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }
    }

    private static void validatePast(LocalDate date, LocalTime time) {
        if (LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("과거 날짜/시간으로는 예약할 수 없습니다.");
        }
    }

    private static void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("테마는 필수입니다.");
        }
    }

    private static void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("예약 ID는 필수입니다.");
        }
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public LocalDate date() {
        return date;
    }

    public LocalTime time() {
        return time;
    }

    public Theme theme() {
        return theme;
    }

    public ReservationStatus status() {
        return status;
    }

    public void updateStatus(ReservationStatus status) {
        this.status = status;
    }

}
