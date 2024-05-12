package roomescape.reservation.domain;

import roomescape.global.exception.RoomEscapeException;
import roomescape.member.domain.ReservationMember;
import roomescape.theme.theme.domain.Theme;

import java.time.LocalDate;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static roomescape.global.exception.ExceptionMessage.*;

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
            Objects.requireNonNull(member, MEMBER_CANNOT_NULL.getMessage());
            Objects.requireNonNull(date, DATE_CANNOT_NULL.getMessage());
            Objects.requireNonNull(time, TIME_CANNOT_NULL.getMessage());
            Objects.requireNonNull(theme, THEME_CANNOT_NULL.getMessage());
        } catch (NullPointerException e) {
            throw new RoomEscapeException(BAD_REQUEST, e.getMessage());
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
