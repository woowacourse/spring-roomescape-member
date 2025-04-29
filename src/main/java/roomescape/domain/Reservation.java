package roomescape.domain;

import java.time.LocalDate;

public record Reservation(
        Long id,
        String name,
        LocalDate date,
        ReservationTime time
) {

    public Reservation {
        validate(name, date);
    }

    public Reservation(String name, LocalDate date, ReservationTime time) {
        this(null, name, date, time);
    }

    private void validate(String name, LocalDate date) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null이 될 수 없습니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("날짜는 null이 될 수 없습니다.");
        }
    }
}
