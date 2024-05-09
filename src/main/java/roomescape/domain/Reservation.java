package roomescape.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Member member, LocalDate date, ReservationTime time, Theme theme) {
        this(null, member, date, time, theme);
    }

    public Reservation(Long id, Member member, LocalDate date, ReservationTime time, Theme theme) {
        validate(member, date, time, theme);

        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validate(Member member, LocalDate date, ReservationTime time, Theme theme) {
        if (member == null) {
            throw new IllegalArgumentException("member는 필수 값입니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("date는 필수 값입니다.");
        }
        if (time == null) {
            throw new IllegalArgumentException("time은 필수 값입니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("theme은 필수 값입니다.");
        }
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

    public Long getTimeId() {
        return time.getId();
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getThemeId() {
        return theme.getId();
    }
}
