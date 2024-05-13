package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final LocalDate date;
    private final Member member;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(LocalDate date, Member member, ReservationTime time, Theme theme) {
        this(null, date, member, time, theme);
    }

    public Reservation(Long id, LocalDate date, Member member, ReservationTime time, Theme theme) {
        this.id = id;
        this.date = date;
        this.member = member;
        this.time = time;
        this.theme = theme;
    }

    public boolean hasSameTheme(Reservation other) {
        return this.theme.equals(other.theme);
    }

    public boolean hasSameDateTime(Reservation other) {
        return getDateTime().equals(other.getDateTime());
    }

    public boolean isBeforeNow() {
        return getDateTime().isBefore(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
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

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time.getStartAt());
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
