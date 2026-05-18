package roomescape.reservation.domain;

import roomescape.exception.DomainRuleViolationException;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        if (name == null || name.isBlank()) {
            throw new DomainRuleViolationException("예약자 이름은 비어 있을 수 없습니다.");
        }
        if (date == null) {
            throw new DomainRuleViolationException("예약 날짜는 비어 있을 수 없습니다.");
        }
        if (time == null) {
            throw new DomainRuleViolationException("예약 시간은 비어 있을 수 없습니다.");
        }
        if (theme == null) {
            throw new DomainRuleViolationException("예약 테마는 비어 있을 수 없습니다.");
        }
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
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

    public Theme getTheme() {
        return theme;
    }
}