package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

//TODO: status 필드(ENUM) 추가
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

    public static Reservation create(String name, LocalDate date, ReservationTime time, Theme theme) {
        validate(name, date, time, theme);
        validatePast(date, time);
        return new Reservation(null, name, date, time, theme);
    }

    public static Reservation of(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validateId(id);
        return new Reservation(id, name, date, time, theme);
    }

    private static void validate(String name, LocalDate date, ReservationTime time, Theme theme) {
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

    private static void validateTime(ReservationTime time) {
        if (time == null) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }
    }

    private static void validatePast(LocalDate date, ReservationTime time) {
        if (LocalDateTime.of(date, time.startAt())
                .isBefore(LocalDateTime.now())) {
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

    public ReservationTime time() {
        return time;
    }

    public Theme theme() {
        return theme;
    }
}
