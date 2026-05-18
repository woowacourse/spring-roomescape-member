package roomescape.domain;

import roomescape.domain.exception.DomainRuleViolationException;
import roomescape.domain.vo.Name;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {
    private final Long id;
    private final Name name;
    private final LocalDate date;
    private final Time time;
    private final Theme theme;

    private Reservation(Long id, Name name, LocalDate date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation create(Name name, LocalDate date, Time time, Theme theme, LocalDateTime now) {
        Reservation reservation = new Reservation(null, name, date, time, theme);
        reservation.validate(now);
        return reservation;
    }

    public static Reservation restore(Long id, Name name, LocalDate date, Time time, Theme theme) {
        return new Reservation(id, name, date, time, theme);
    }

    public Reservation update(Name name, LocalDate date, Time time, Theme theme, LocalDateTime now) {
        Reservation updated = new Reservation(this.id, name, date, time, theme);
        updated.validate(now);
        return updated;
    }

    private void validate(LocalDateTime now) {
        if (isPast(now)) {
            throw new DomainRuleViolationException("이미 지난 시각으로는 예약할 수 없습니다.");
        }

        LocalDate maxAvailableDate = now.toLocalDate().plusDays(14);
        if (this.date.isAfter(maxAvailableDate)) {
            throw new DomainRuleViolationException("예약은 현재로부터 최대 14일 이내만 가능합니다.");
        }
    }

    public boolean isDeletable(LocalDateTime now) {
        return !isPast(now);
    }

    private boolean isPast(LocalDateTime now) {
        return LocalDateTime.of(this.date, this.time.getStartAt()).isBefore(now);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
