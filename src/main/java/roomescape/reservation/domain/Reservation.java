package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.Objects;
import roomescape.member.domain.Member;

public class Reservation {

    private final Long id;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final Member member;

    public Reservation(Long id, LocalDate date, ReservationTime time, Theme theme, Member member) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
        validate();
    }

    private void validate() {
        Objects.requireNonNull(date, "예약 날짜는 null일 수 없습니다.");
        Objects.requireNonNull(time, "예약 시간은 null일 수 없습니다.");
        Objects.requireNonNull(theme, "테마는 null일 수 없습니다.");
        Objects.requireNonNull(member, "사용자는 null일 수 없습니다.");
    }

    public Reservation withId(Long id) {
        return new Reservation(id, this.date, this.time, this.theme, this.member);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Member getMember() {
        return member;
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public Long getMemberId() {
        return member.getId();
    }

    public boolean hasTimeId(Long timeId) {
        return time.isSameId(timeId);
    }

    public boolean hasThemeId(Long themeId) {
        return theme.isSameId(themeId);
    }

    public boolean hasMemberId(Long memberId) {
        return member.isSameId(memberId);
    }

    public boolean hasSameDate(LocalDate date) {
        return this.date.isEqual(date);
    }

    public boolean isSameOrAfter(LocalDate from) {
        return !this.date.isBefore(from);
    }

    public boolean isSameOrBefore(LocalDate from) {
        return !this.date.isAfter(from);
    }

}
