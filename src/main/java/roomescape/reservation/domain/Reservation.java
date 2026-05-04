package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.time.domain.ReservationTime;

public class Reservation {
    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    private Reservation(Long id, String name, LocalDate date, ReservationTime time) {
        validate(name, date, time);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public static Reservation create(String name, LocalDate date, ReservationTime time) {
        validatePast(date, time);
        return new Reservation(null, name, date, time);
    }

    public static Reservation of(Long id, String name, LocalDate date, ReservationTime time) {
        validateId(id);
        return new Reservation(id, name, date, time);
    }

    private static void validate(String name, LocalDate date, ReservationTime time) {
        validateName(name);
        validateDate(date);
        validateTime(time);
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

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Reservation that)) {
            return false;
        }

        if (that.id == null || this.id == null) {
            return false;
        }

        return Objects.equals(this.id, that.id);
    }
}
