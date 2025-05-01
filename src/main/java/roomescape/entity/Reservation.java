package roomescape.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {

    private static final int NAME_LENGTH = 10;

    public Reservation {
        validate(name, date, time, theme);
    }

    public static Reservation createIfDateTimeValid(String name, LocalDate date, ReservationTime time, Theme theme) {
        validateDateTime(date, time);
        return new Reservation(null, name, date, time, theme);
    }

    public boolean equalDateTime(Reservation reservation) {
        return equalsDate(reservation) && equalsTime(reservation);
    }

    private boolean equalsDate(Reservation reservation) {
        return this.date.equals(reservation.date);
    }

    private boolean equalsTime(Reservation reservation) {
        return this.time.equalsTime(reservation.time);
    }

    private static void validateDateTime(LocalDate date, ReservationTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time.startAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("[ERROR] 예약이 불가능한 시간입니다: " + date);
        }
    }

    private void validate(String name, LocalDate date, ReservationTime time, Theme theme) {
        validateEmptyName(name);
        validateNameLength(name);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);
    }

    private void validateEmptyName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약자명입니다.");
        }
    }

    private void validateNameLength(String name) {
        if (name.length() > NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 예약자명의 길이가 " + NAME_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약 날짜입니다.");
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약 시간입니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 테마입니다.");
        }
    }
}
