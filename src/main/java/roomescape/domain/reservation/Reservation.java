package roomescape.domain.reservation;

import roomescape.domain.member.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {
    private final Long id;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final Member member;

    public Reservation(
            final Long id,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme,
            final Member member
    ) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public boolean isBeforeThan(final LocalDateTime otherTime) {
        LocalDateTime reservationTime = LocalDateTime.of(date, time.getStartAt());
        return reservationTime.isBefore(otherTime);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return member.getName();
    }

    public long getTimeId() {
        return time.getId();
    }

    public long getThemeId() {
        return theme.getId();
    }

    public long getMemberId() {
        return member.getId();
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

    public Member getMember() {
        return member;
    }
}
