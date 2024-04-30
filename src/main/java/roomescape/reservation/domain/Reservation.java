package roomescape.reservation.domain;

import roomescape.exception.InvalidNameException;
import roomescape.exception.NullPointDateException;
import roomescape.exception.PastDateReservationException;
import roomescape.exception.PastTimeReservationException;
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
        this.date = parseDate(date, time);
        this.time = time;
    }

    private void validateNameExist(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new InvalidNameException("예약자명이 null 이거나 공백인 경우 저장을 할 수 없습니다.");
        }
    }

    private void validateDateIsNotNull(final String date) {
        if (Objects.isNull(date)) {
            throw new NullPointDateException("날짜가 null인 경우 저장을 할 수 없습니다.");
        }
    }

    private LocalDate parseDate(final String date, final ReservationTime time) {
        LocalDate localDate = LocalDate.parse(date, DATE_FORMAT);
        validateNoReservationsForPastDates(localDate, time);
        return localDate;
    }

    private void validateNoReservationsForPastDates(final LocalDate localDate, final ReservationTime time) {
        if (localDate.isBefore(LocalDate.now())) {
            throw new PastDateReservationException("날짜가 과거인 경우 모든 시간에 대한 예약이 불가능 합니다.");
        }
        if (localDate.equals(LocalDate.now()) && time.checkPastTime()) {
            throw new PastTimeReservationException("날짜가 오늘인 경우 지나간 시간에 대한 예약이 불가능 합니다.");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(date, that.date) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, time);
    }
}
