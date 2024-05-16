package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.domain.member.MemberInfo;

public class Reservation {

    private final Long id;
    private final LocalDate date;
    private final ReservationTime time;
    private final ReservationTheme theme;
    private final MemberInfo member;

    public Reservation(Long id, LocalDate date, ReservationTime time, ReservationTheme theme,
                       MemberInfo member, Purpose purpose) {
        if (purpose.isCreate()) {
            validateIsPastDate(date, time.getStartAt());
        }
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Reservation(LocalDate date, ReservationTime time, ReservationTheme theme, MemberInfo member,
                       Purpose purpose) {
        this(null, date, time, theme, member, purpose);
    }

    private void validateIsPastDate(LocalDate date, LocalTime time) {
        LocalDateTime input = LocalDateTime.of(date, time);

        if (input.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지난 시간으로는 예약할 수 없습니다.");
        }
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

    public ReservationTheme getTheme() {
        return theme;
    }

    public MemberInfo getMember() {
        return member;
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
}
