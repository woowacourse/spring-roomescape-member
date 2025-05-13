package roomescape.reservation.entity;

import roomescape.member.entity.Member;
import roomescape.theme.entity.Theme;
import roomescape.time.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private static final int NAME_LENGTH = 10;

    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, Member member, LocalDate date, ReservationTime time, Theme theme) {
        validate(member.getName(), date, time, theme);
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
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

    public static Reservation createIfDateTimeValid(Member member, LocalDate date, ReservationTime time, Theme theme) {
        validateDateTime(date, time);
        return new Reservation(null, member, date, time, theme);
    }

    public boolean isDuplicate(Reservation reservation) {
        boolean equalsDate = this.date.equals(reservation.date);
        boolean equalsTime = this.time.equalsTime(reservation.time);
        boolean equalsTheme = this.theme.equalsThemeName(reservation.theme);
        return equalsDate && equalsTime && equalsTheme;
    }

    private static void validateDateTime(LocalDate date, ReservationTime time) {
        LocalDateTime dateTime = LocalDateTime.of(date, time.getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("[ERROR] 예약이 불가능한 시간입니다: " + date);
        }
    }

    private void validate(String name, LocalDate date, ReservationTime time, Theme theme) {
        validateEmptyName(name);
        validateNameLength(name);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);
    }

    private void validateEmptyName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약자명입니다.");
        }
    }

    private void validateNameLength(String name) {
        if (name.length() > NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 예약자명의 길이가 " + NAME_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약 날짜입니다.");
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 예약 시간입니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("[ERROR] 유효하지 않은 테마입니다.");
        }
    }
}
