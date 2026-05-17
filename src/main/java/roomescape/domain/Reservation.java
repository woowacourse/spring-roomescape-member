package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.exception.InvalidDomainException;

public class Reservation {

    private final Long id;
    private final String name;
    private final Theme theme;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, String name, Theme theme, LocalDate date, ReservationTime time) {
        validate(name, theme, date, time);
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    private void validate(String name, Theme theme, LocalDate date, ReservationTime time) {
        if (name == null || name.isBlank()) {
            throw new InvalidDomainException("예약자 이름은 비어있을 수 없습니다.");
        }
        if (theme == null) {
            throw new InvalidDomainException("테마는 필수입니다.");
        }
        if (date == null) {
            throw new InvalidDomainException("예약 날짜는 필수입니다.");
        }
        if (time == null) {
            throw new InvalidDomainException("예약 시간은 필수입니다.");
        }
    }

    public boolean isInPast(LocalDateTime currentDateTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        return reservationDateTime.isBefore(currentDateTime);
    }

    public Reservation withId(Long id) {
        return new Reservation(id, name, theme, date, time);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
