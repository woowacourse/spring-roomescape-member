package roomescape.reservation.domain;

import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public record Reservation(Long id, String name, LocalDate date, ReservationTime time) {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Reservation {
        validateNameExist(name);
    }

    public Reservation(final Long id, final String name, final String date, final ReservationTime time) {
        this(id, name, LocalDate.parse(date, DATE_FORMAT), time);
    }

    private void validateNameExist(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("예약자명이 null 이거나 공백인 경우 저장을 할 수 없습니다.");
        }
    }
}
