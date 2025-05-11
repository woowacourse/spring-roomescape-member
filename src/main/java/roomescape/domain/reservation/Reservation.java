package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.domain.member.Member;

public class Reservation {

    private final Long id;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final Member member;

    protected Reservation(Long id, LocalDate date, ReservationTime time, Theme theme, Member member) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public static Reservation of(Long id,
                                 LocalDate date,
                                 ReservationTime time,
                                 Theme theme,
                                 Member member
    ) {
        validate(date, time, theme, member);
        return new Reservation(id, date, time, theme, member);
    }

    public static Reservation createNew(LocalDate date,
                                        ReservationTime time,
                                        Theme theme,
                                        Member member
    ) {
        validateTheme(theme);
        validateMember(member);
        validateDateTimeIsAfterNow(date, time);
        return new Reservation(null, date, time, theme, member);

    }

    private static void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약 날짜입니다.");
        }
    }

    private static void validateTime(ReservationTime time) {
        if (time == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약 시간입니다.");
        }
    }

    private static void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 테마입니다.");
        }
    }

    private static void validateMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 사용자입니다.");
        }
    }

    private static void validate(LocalDate date, ReservationTime time, Theme theme, Member member) {
        validateTheme(theme);
        validateDate(date);
        validateTime(time);
        validateMember(member);
    }

    private static void validateDateTimeIsAfterNow(LocalDate date, ReservationTime time) {
        validateDate(date);
        validateTime(time);
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("[ERROR] 예약이 불가능한 시간입니다: " + date);
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

    public Theme getTheme() {
        return theme;
    }

    public Member getMember() {
        return member;
    }

    public Long getTimeId() {
        return time != null ? time.getId() : null;
    }

    public Long getThemeId() {
        return theme != null ? theme.getId() : null;
    }

    public Long getMemberId() {
        return member != null ? member.getId() : null;
    }
}
