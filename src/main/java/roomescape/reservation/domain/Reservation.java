package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.member.domain.Member;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public class Reservation {

    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, Member member, LocalDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation createWithId(Long id) {
        return new Reservation(id, this.member, this.date, this.time, this.theme);
    }

    public void validateNotPast(LocalDate nowDate, LocalTime nowTime) {
        if (this.date.isBefore(nowDate)) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }
        if (this.date.isEqual(nowDate) && this.time.isPastTime(nowTime)) {
            throw new IllegalArgumentException("지난 시각은 예약할 수 없습니다.");
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

    public Long getTimeId() {
        return time.getId();
    }

    public boolean isSameId(Long id) {
        return this.id.equals(id);
    }

}
