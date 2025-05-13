package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.member.domain.Member;
import roomescape.time.domain.Time;
import roomescape.theme.domain.Theme;

public record Reservation(Long id, Member member, LocalDate date, Time time, Theme theme) {

    private static final Long NOT_SAVED_ID = 0L;

    public static Reservation createBeforeSaved(Member member, LocalDate date, Time time, Theme theme) {
        return new Reservation(NOT_SAVED_ID, member, date, time, theme);
    }

    public Reservation {
        if (id == null) {
            throw new IllegalArgumentException("[ERROR] id가 null이 되어서는 안 됩니다.");
        }
        if (member == null) {
            throw new IllegalArgumentException("[ERROR] 사용자가 null이 되어서는 안 됩니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 날짜가 null이 되어서는 안 됩니다.");
        }
        if (time == null) {
            throw new IllegalArgumentException("[ERROR] 시간이 null이 되어서는 안 됩니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("[ERROR] 테마가 null이 되어서는 안 됩니다.");
        }
    }
}
