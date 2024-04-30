package roomescape.domain;

import roomescape.exception.InvalidException;

import java.time.LocalDate;

public record Reservation(Long id, String name, LocalDate date, ReservationTime time) {

    public Reservation {
        validateEmptyName(name);
    }

    private void validateEmptyName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidException("이름은 공백일 수 없습니다.");
        }
    }

    public Reservation assignId(Long id) {
        return new Reservation(id, name, date, time);
    }

    public Reservation assignTime(ReservationTime time) {
        return new Reservation(id, name, date, time);
    }
}
