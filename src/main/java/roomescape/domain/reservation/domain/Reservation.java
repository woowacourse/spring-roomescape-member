package roomescape.domain.reservation.domain;

import roomescape.domain.member.domain.ReservationMember;
import roomescape.domain.theme.domain.Theme;
import roomescape.global.exception.RoomEscapeException;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final ReservationMember member;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, ReservationMember member, LocalDate date, ReservationTime time, Theme theme) {
        checkNull(member, date, time, theme);
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void checkNull(ReservationMember member, LocalDate date, ReservationTime time, Theme theme) {
        try {
            Objects.requireNonNull(member, "[ERROR] 멤버는 null일 수 없습니다.");
            Objects.requireNonNull(date, "[ERROR] 날짜는 null일 수 없습니다.");
            Objects.requireNonNull(time, "[ERROR] 시간은 null일 수 없습니다.");
            Objects.requireNonNull(theme, "[ERROR] 테마는 null일 수 없습니다.");
        } catch (NullPointerException e) {
            throw new RoomEscapeException(e.getMessage());
        }
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public String getName() {
        return member.getName();
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

    public Long getReservationTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }
}
