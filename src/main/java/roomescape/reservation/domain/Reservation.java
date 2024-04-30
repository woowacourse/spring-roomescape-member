package roomescape.reservation.domain;

import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Reservation {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(final Long id, final String name, final String date, final ReservationTime time) {
        validateNameExist(name);
        validateDateIsNotNull(date);
        this.id = id;
        this.name = name;
        this.date = LocalDate.parse(date);
        this.time = time;
    }

    private void validateNameExist(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("예약자명이 null 이거나 공백인 경우 저장을 할 수 없습니다.");
        }
    }

    private void validateDateIsNotNull(final String date) {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("날짜가 null인 경우 저장을 할 수 없습니다.");
        }
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
}
