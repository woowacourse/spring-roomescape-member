package roomescape.entity;

import java.time.LocalDate;

public record Reservation(Long id, String name, LocalDate date, ReservationTime time) {

    private static final int NAME_LENGTH = 10;

    public Reservation {
        validate(name, date, time);
    }

    public static Reservation createIfDateTimeValid(String name, LocalDate date, ReservationTime time) {
        validateDateTime(date, time);
        return new Reservation(null, name, date, time);
    }

    public boolean equalDateTime(Reservation reservation) {
        return equalsDate(reservation) && equalsTime(reservation);
    }

    private boolean equalsDate(Reservation reservation) {
        return this.date == reservation.date;
    }

    private boolean equalsTime(Reservation reservation) {
        return this.time.equalsTime(reservation.time);
    }

    private static void validateDateTime(LocalDate date, ReservationTime time) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("[ERROR] 예약이 불가능한 날짜입니다: " + date);
        }
        time.isBefore();
    }

    private void validate(String name, LocalDate date, ReservationTime time) {
        validateNameLength(name);
        validateEmptyName(name);
        validateDate(date);
        validateTime(time);
    }

    private void validateEmptyName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 이름입니다.");
        }
    }

    private void validateNameLength(String name) {
        if (name.length() > NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 이름 길이가 " + NAME_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 날짜입니다.");
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 시간입니다.");
        }
    }
}
