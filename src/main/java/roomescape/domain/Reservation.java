package roomescape.domain;

import roomescape.domain.exception.InvalidRequestBodyFieldException;
import roomescape.domain.exception.InvalidReservationTimeException;

import java.time.LocalDate;

public class Reservation {

    private Long id;
    private Member member;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;

    public Reservation() {
    }

    public Reservation(Member member, LocalDate date, ReservationTime time, Theme theme) {
        this(null, member, date, time, theme);
    }

    public Reservation(Long id, Member member, LocalDate date, ReservationTime time, Theme theme) {
        validateNotNull(member, date, time, theme);
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateNotNull(Member member, LocalDate date, ReservationTime time, Theme theme) {
        if (member == null || date == null || time == null) {
            throw new InvalidRequestBodyFieldException("예약 필드값이 null 입니다.");
        }
    }

    public void validateDifferentDateTime(LocalDate requestDate, ReservationTime requestTime) {
        if (date.isEqual(requestDate) && time.isSameTime(requestTime)) {
            throw new InvalidReservationTimeException("예약 날짜와 예약 시간이 중복될 수 없습니다.");
        }
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
}
