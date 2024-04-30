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
        this.date = parseDate(date);
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

    private LocalDate parseDate(final String date) {
        LocalDate localDate = LocalDate.parse(date);
        validateNoReservationsForPastDates(localDate);
        return localDate;
    }

    private void validateNoReservationsForPastDates(LocalDate localDate) {
        if (localDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("날짜가 과거인 경우 모든 시간에 대한 예약이 불가능 합니다.");
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
